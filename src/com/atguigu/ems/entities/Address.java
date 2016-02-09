package com.atguigu.ems.entities;

public class Address {
	// id
	private Integer addressId;

	// 市, City 中可以包含 Province, 所以 Address 中就不需要再包含 Province
	private City city;
	// 邮编
	private String postCode;

	// 具体住址
	private String details;

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}
