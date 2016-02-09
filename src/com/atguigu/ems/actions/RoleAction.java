package com.atguigu.ems.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.atguigu.ems.entities.Role;
import com.atguigu.ems.services.AuthorityService;

@Controller
public class RoleAction extends BaseAction<Role> {

	private static final long serialVersionUID = 1L;
	@Autowired
	private AuthorityService authorityService;

	public String input() {
		request.put("parentAuthroities",
				authorityService.getByIsNull("parentAuthority"));
		request.put("authorities",
				authorityService.getByIsNotNull("parentAuthority"));
		return "input";
	}
}
