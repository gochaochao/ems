package com.atguigu.ems.pages;

import com.atguigu.ems.entities.Department;

public class EmployeeCrieriaFormBean {

	private String filter_LIKES_loginName;
	private String filter_LIKES_employeeName;
	private String filter_EQS_gender;

	private String filter_EQI_enabled;
	private String filter_LIKES_email;
	private Department filter_EQI_department;

	public String getFilter_LIKES_loginName() {
		return filter_LIKES_loginName;
	}

	public void setFilter_LIKES_loginName(String filter_LIKES_loginName) {
		this.filter_LIKES_loginName = filter_LIKES_loginName;
	}

	public String getFilter_LIKES_employeeName() {
		return filter_LIKES_employeeName;
	}

	public void setFilter_LIKES_employeeName(String filter_LIKES_employeeName) {
		this.filter_LIKES_employeeName = filter_LIKES_employeeName;
	}

	public String getFilter_EQS_gender() {
		return filter_EQS_gender;
	}

	public void setFilter_EQS_gender(String filter_EQS_gender) {
		this.filter_EQS_gender = filter_EQS_gender;
	}

	public String getFilter_EQI_enabled() {
		return filter_EQI_enabled;
	}

	public void setFilter_EQI_enabled(String filter_EQI_enabled) {
		this.filter_EQI_enabled = filter_EQI_enabled;
	}

	public String getFilter_LIKES_email() {
		return filter_LIKES_email;
	}

	public void setFilter_LIKES_email(String filter_LIKES_email) {
		this.filter_LIKES_email = filter_LIKES_email;
	}

	public Department getFilter_EQI_department() {
		return filter_EQI_department;
	}

	public void setFilter_EQI_department(Department filter_EQI_department) {
		this.filter_EQI_department = filter_EQI_department;
	}

}
