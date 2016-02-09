package com.atguigu.ems.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Employee {
	// id
	private Integer employeeId;
	// 登录名
	private String loginName;

	// 员工名称
	private String employeeName;
	// 密码
	private String password;

	// 角色集合
	private Set<Role> roles = new HashSet<>();
	// 用户是否可用. 1 代表可用, 0 代表不可用
	private Integer enabled;

	// 所属部门
	private Department department;
	// 生日
	private Date birth;

	// 性别
	private String gender;
	// Email
	private String email;

	// 电话
	private String mobilePhone;
	// 访问次数
	private int visitedTimes;

	// 是否被删除. 1 代表已经被删除, 0 代表没有被删除
	private int isDeleted;
	// 简历
	private Resume resume;

	// 录入人
	private Employee editor;

	public Employee() {
	}

	public Employee(String loginName, String employeeName, Set<Role> roles,
			Integer enabled, Department department, String gender, String email) {
		super();
		this.loginName = loginName;
		this.employeeName = employeeName;
		this.roles = roles;
		this.enabled = enabled;
		this.department = department;
		this.gender = gender;
		this.email = email;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		// if(loginName != null)
		// loginName = loginName.trim();

		this.loginName = loginName;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public int getVisitedTimes() {
		return visitedTimes;
	}

	public void setVisitedTimes(int visitedTimes) {
		this.visitedTimes = visitedTimes;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Resume getResume() {
		return resume;
	}

	public void setResume(Resume resume) {
		this.resume = resume;
	}

	public Employee getEditor() {
		return editor;
	}

	public void setEditor(Employee editor) {
		this.editor = editor;
	}

	// 工具方法，不需要进行持久化映射
	// 返回角色名对应的字符串，例如：A,B,C
	public String getRoleNames() {
		StringBuilder roleNames = new StringBuilder();
		for (Role role : roles) {
			roleNames.append(role.getRoleName()).append(",");
		}
		if (roleNames.length() > 0) {
			roleNames.replace(roleNames.length() - 1, roleNames.length(), "");
		}
		return roleNames.toString();
	}

	// 和页面roles2字段对应的setter，目标是给roles属性赋值
	public void setRoles2(String[] ids) {
		if (ids != null && ids.length > 0) {
			for (String id : ids) {
				this.roles.add(new Role(Integer.parseInt(id)));
			}
		}
	}

	public List<String> getRoles2() {
		List<String> roleIds = new ArrayList<>();
		for (Role role : this.roles) {
			roleIds.add(role.getRoleId() + "");
		}
		return roleIds;
	}

	@Override
	public String toString() {
		return "Employee [employeeId=" + employeeId + ", loginName="
				+ loginName + ", employeeName=" + employeeName + ", password="
				+ password + "]";
	}
}
