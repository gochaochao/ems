package com.atguigu.ems.daos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.beans.factory.annotation.Autowired;

import com.atguigu.ems.pages.Page;
import com.atguigu.ems.pages.PropertyFilter;
import com.atguigu.ems.pages.PropertyFilter.MatchType;
import com.atguigu.ems.utils.ReflectionUtils;

public class BaseDao<T> {
	@Autowired
	private SessionFactory sessionFactory;
	private Class<T> entityClass;

	public BaseDao() {
		entityClass = ReflectionUtils.getSuperGenericType(getClass());
	}

	public Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	// 批量插入
	public void batchSave(List<T> entities) {
		for (int i = 0; i < entities.size(); i++) {
			getSession().save(entities.get(i));
			if ((i + 1) % 50 == 0) {
				getSession().flush();
				getSession().clear();
			}
		}
	}

	public List<T> getAll() {
		Criteria criteria = getSession().createCriteria(entityClass);
		return criteria.list();
	}

	public void evict(T entity) {
		getSession().evict(entity);
	}

	public void save(T entity) {
		getSession().saveOrUpdate(entity);
	}

	public T get(Integer id) {
		return (T) getSession().get(entityClass, id);
	}

	// 给page赋值
	public Page<T> getPage(Page<T> page) {
		// 1.查询总的记录数
		long totalRecord = getTotalRecord();
		page.setTotalRecord(totalRecord);
		// 2.查询当前页面的content
		List<T> content = getContent(page);
		page.setContent(content);
		return page;
	}

	// 带查询条件的分页
	public Page<T> getPage(Page<T> page, List<PropertyFilter> filters) {
		List<Criterion> criterions = parsePropertyFilterToCriterions(filters);
		// 1. 查询总的记录数
		long totalRecord = getTotalRecord(criterions);
		page.setTotalRecord(totalRecord);

		// 2. 查询当前页面的 content.
		List<T> content = getContent(page, criterions);
		page.setContent(content);

		return page;
	};

	// 带查询条件的方法
	private List<T> getContent(Page<T> page, List<Criterion> criterions) {
		// 使用 Criteria 完成表连接.
		Criteria criteria = getSession().createCriteria(entityClass);
		if (criterions != null && criterions.size() > 0) {
			for (Criterion criterion : criterions) {
				criteria.add(criterion);
			}
		}

		// 设置分页的参数
		int firstResult = (page.getPageNumber() - 1) * page.getPageSize();
		int maxResults = page.getPageSize();
		criteria.setFirstResult(firstResult).setMaxResults(maxResults);

		return criteria.list();
	}

	// 带查询条件的方法
	private long getTotalRecord(List<Criterion> criterions) {
		Criteria criteria = getSession().createCriteria(entityClass);
		if (criterions != null && criterions.size() > 0) {
			for (Criterion criterion : criterions) {
				criteria.add(criterion);
			}
		}

		// 设置统计查询: 统计 employeeId 的 count.
		criteria.setProjection(Projections.count(getIdName()));

		return (long) criteria.uniqueResult();
	}

	private List<Criterion> parsePropertyFilterToCriterions(
			List<PropertyFilter> filters) {
		List<Criterion> criterions = new ArrayList<>();
		if (filters != null && filters.size() > 0) {
			for (PropertyFilter filter : filters) {
				String propertyName = filter.getPropertyName();
				MatchType matchType = filter.getMatchType();
				Class propertyType = filter.getPropertyType();
				Object propertyValue = filter.getPropertyVal();

				Criterion criterion = null;
				if (propertyValue == null
						|| propertyValue.toString().trim().equals("")) {
					if (MatchType.ISNULL.equals(matchType)) {
						criterion = Restrictions.isNull(propertyName);
						criterions.add(criterion);
					}
					continue;
				}
				propertyValue = ReflectionUtils.convertValue(propertyValue,
						propertyType);
				switch (matchType) {
				case EQ:
					criterion = Restrictions.eq(propertyName, propertyValue);
					break;
				case GE:
					criterion = Restrictions.ge(propertyName, propertyValue);
					break;
				case GT:
					criterion = Restrictions.gt(propertyName, propertyValue);
					break;
				case LE:
					criterion = Restrictions.le(propertyName, propertyValue);
					break;
				case LT:
					criterion = Restrictions.lt(propertyName, propertyValue);
					break;
				case LIKE:
					criterion = Restrictions.like(propertyName,
							(String) propertyValue, MatchMode.ANYWHERE);
				}
				if (criterion != null) {
					criterions.add(criterion);
				}
			}
		}
		return criterions;
	}

	// 查询当前页的内容
	// setResultTransformer结果集转换器，去掉重复的部门
	private List<T> getContent(Page<T> page) {
		Criteria criteria = getSession().createCriteria(entityClass);
		// .createAlias("department", "d", JoinType.LEFT_OUTER_JOIN)
		// .createAlias("roles", "r", JoinType.LEFT_OUTER_JOIN)
		// .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		// 设置分页的参数
		int firstResult = (page.getPageNumber() - 1) * page.getPageSize();// 当前页数-1乘以每页条数
		int maxResult = page.getPageSize();
		criteria.setFirstResult(firstResult).setMaxResults(maxResult);
		return criteria.list();
	}

	// 查询总记录数
	private long getTotalRecord() {
		Criteria criteria = getSession().createCriteria(entityClass);
		// 设置统计查询，统计employeeId的count
		criteria.setProjection(Projections.count(getIdName()));
		return (long) criteria.uniqueResult();
	}

	// 获取id的属性名
	public String getIdName() {
		ClassMetadata cmd = this.sessionFactory.getClassMetadata(entityClass);
		return cmd.getIdentifierPropertyName();
	}

	public T getBy(String propertyName, Object propertyValue) {
		// QBC
		Criteria criteria = getSession().createCriteria(entityClass);
		Criterion criterion = Restrictions.eq(propertyName, propertyValue);
		criteria.add(criterion);
		return (T) criteria.uniqueResult();
	}

	public List<T> getByForList(String propertyName, Object propertyValue) {
		Criteria criteria = getSession().createCriteria(entityClass);
		Criterion criterion = Restrictions.eqOrIsNull(propertyName,
				propertyValue);
		criteria.add(criterion);
		return criteria.list();
	}

	public List<T> getByIsNotNull(String propertyName) {
		Criteria criteria = getSession().createCriteria(entityClass);
		Criterion criterion = Restrictions.isNotNull(propertyName);
		criteria.add(criterion);

		return criteria.list();
	}

	public List<T> getByIsNull(String propertyName) {
		Criteria criteria = getSession().createCriteria(entityClass);
		Criterion criterion = Restrictions.isNull(propertyName);
		criteria.add(criterion);

		return criteria.list();
	}

	public Collection<T> getByForSet(String string, String[] split) {
		Criteria criteria = getSession().createCriteria(entityClass);

		Criterion criterion = Restrictions.in(string, split);
		criteria.add(criterion);

		return criteria.list();
	}
}
