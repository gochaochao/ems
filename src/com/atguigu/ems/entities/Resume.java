package com.atguigu.ems.entities;

public class Resume {

	// id
	private Integer resumeId;
	// 该简历属于哪个员工
	private Employee owner;

	// 地址信息
	private Address address;
	// 最终学历信息
	private School school;

	// 专业
	private String major;
	// 等级: 高中、专科、本科、研究生、博士
	private int level;
	
	//还可以再添加工作经历...

	public Integer getResumeId() {
		return resumeId;
	}

	public void setResumeId(Integer resumeId) {
		this.resumeId = resumeId;
	}

	public Employee getOwner() {
		return owner;
	}

	public void setOwner(Employee owner) {
		this.owner = owner;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
