package com.atguigu.ems.services;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.atguigu.ems.daos.BaseDao;
import com.atguigu.ems.pages.Page;
import com.atguigu.ems.pages.PropertyFilter;

public class BaseService<T> {
	@Autowired
	protected BaseDao<T> baseDao;

	// 带查询条件的分页
	@Transactional(readOnly = true)
	public Page<T> getPage(Page<T> page, Map<String, Object> params) {
		List<PropertyFilter> filters = PropertyFilter
				.parseParamsToFilters(params);
		page = baseDao.getPage(page, filters);
		return page;
	}

	@Transactional(readOnly = true)
	public List<T> getByIsNotNull(String propertyName) {
		return baseDao.getByIsNotNull(propertyName);
	}

	@Transactional(readOnly = true)
	public List<T> getByIsNull(String propertyName) {
		return baseDao.getByIsNull(propertyName);
	}

	@Transactional
	public List<T> getByForCollection(String propertyName, Object propertyValue) {
		return baseDao.getByForList(propertyName, propertyValue);
	}

	@Transactional(readOnly = true)
	public T getBy(String propertyName, Object propertyValue) {
		return baseDao.getBy(propertyName, propertyValue);
	}

	@Transactional(readOnly = true)
	public List<T> getAll() {
		return baseDao.getAll();
	}

	@Transactional(readOnly = true)
	public T get(Integer id) {
		return baseDao.get(id);
	}

	@Transactional
	public void save(T entity) {
		baseDao.save(entity);
	}

	@Transactional(readOnly = true)
	public Page<T> getPage(Page<T> page) {
		return baseDao.getPage(page);
	}
}
