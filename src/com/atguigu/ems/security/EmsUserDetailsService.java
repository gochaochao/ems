package com.atguigu.ems.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.atguigu.ems.entities.Authority;
import com.atguigu.ems.entities.Employee;
import com.atguigu.ems.entities.Role;
import com.atguigu.ems.services.EmployeeService;

@Component
public class EmsUserDetailsService implements UserDetailsService {
	@Autowired
	private EmployeeService employeeService;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		Employee employee = employeeService.getBy("loginName", username);
		String password = employee.getPassword();
		boolean enabled = (employee.getEnabled() == 1);
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		Set<String> authorityNames = new HashSet<>();
		for (Role role : employee.getRoles()) {
			for (Authority authority : role.getAuthorities()) {
				authorityNames.add(authority.getName());
			}
		}
		for (String authorityName : authorityNames) {
			authorities.add(new GrantedAuthorityImpl(authorityName));
		}
		User user = new User(username, password, enabled, accountNonExpired,
				credentialsNonExpired, accountNonLocked, authorities);
		return user;
	}
}
