package com.atguigu.ems.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Role {

	// id
	private Integer roleId;
	// 角色名
	private String roleName;

	// 角色拥有的权限集合
	private Set<Authority> authorities = new HashSet<>();

	public Role() {
		// TODO Auto-generated constructor stub
	}

	public Role(Integer roleId) {
		super();
		this.roleId = roleId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	public void setAuthorities2(String[] authorities) {
		for (String authId : authorities) {
			this.authorities.add(new Authority(Integer.parseInt(authId)));
		}
	}

	public List<String> getAuthorities2() {
		List<String> ids = new ArrayList<>();

		for (Authority authority : authorities) {
			ids.add("" + authority.getId());
		}

		return ids;
	}

	// 返回一个字符串: 当前角色所有的权限对应的父权限使用 , 分割开来.
	public String getDisplayName() {
		StringBuilder content = new StringBuilder();

		Set<String> parentAuthorityDisplayNames = new HashSet<>();

		System.out.println("1.-->" + authorities.getClass().getName());
		System.out.println("2.-->" + authorities.size());
		System.out.println("3.-->" + authorities.getClass().getName());

		for (Authority authority : authorities) {
			String displayName = authority.getParentAuthority()
					.getDisplayName();
			parentAuthorityDisplayNames.add(displayName);
		}

		for (String parentAuthorityDisplayName : parentAuthorityDisplayNames) {
			content.append(parentAuthorityDisplayName).append(",");
		}

		// 去除最后一个 ,
		if (content.length() > 0)
			content.replace(content.length() - 1, content.length(), "");

		return content.toString();
	}

}
