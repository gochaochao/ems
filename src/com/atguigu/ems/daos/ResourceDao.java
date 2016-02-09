package com.atguigu.ems.daos;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.atguigu.ems.entities.Resource;

@Repository
public class ResourceDao extends BaseDao<Resource> {
	@Override
	public List<Resource> getAll() {
		String hql = "SELECT r FROM Resource r LEFT OUTER JOIN FETCH r.authorities a";
		Query query = getSession().createQuery(hql);
		return query.list();
	}
}
