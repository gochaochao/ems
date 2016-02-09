package com.atguigu.ems.entities;

import java.util.HashSet;
import java.util.Set;

public class Province {

	// id
	private Integer provinceId;
	// 省的名称
	private String provinceName;
	
	// 省里有哪些市
	private Set<City> cities = new HashSet<>();

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public Set<City> getCities() {
		return cities;
	}

	public void setCities(Set<City> cities) {
		this.cities = cities;
	}

}
