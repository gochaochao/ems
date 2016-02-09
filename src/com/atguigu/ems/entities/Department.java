package com.atguigu.ems.entities;

import java.util.HashSet;
import java.util.Set;

public class Department {

	//id
	private Integer departmentId;
	//部门名称
	private String departmentName;
	
	//部门经理
	private Employee manager;
	//员工
	private Set<Employee> employees = new HashSet<>();
	
	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Employee getManager() {
		return manager;
	}

	public void setManager(Employee manager) {
		this.manager = manager;
	}

	public Set<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}
	
}
