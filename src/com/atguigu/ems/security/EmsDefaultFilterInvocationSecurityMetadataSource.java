package com.atguigu.ems.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.AntPathRequestMatcher;
import org.springframework.security.web.util.RequestMatcher;
import org.springframework.stereotype.Component;

@Component
public class EmsDefaultFilterInvocationSecurityMetadataSource implements
		FactoryBean<DefaultFilterInvocationSecurityMetadataSource> {

	@Autowired
	private FilterInvocationSecurityMetadataSourceMapBuilder builder;

	@SuppressWarnings("unused")
	@Override
	public DefaultFilterInvocationSecurityMetadataSource getObject()
			throws Exception {
		LinkedHashMap<String, List<String>> srcMap = builder.buildSrcMap();

		LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<>();

		RequestMatcher matcher = null;
		Collection<ConfigAttribute> attributes = null;

		if (srcMap != null && srcMap.size() > 0) {
			for (Map.Entry<String, List<String>> entry : srcMap.entrySet()) {
				matcher = new AntPathRequestMatcher(entry.getKey());
				attributes = new ArrayList<>();

				for (String roleName : entry.getValue()) {
					attributes.add(new SecurityConfig(roleName));
				}
				requestMap.put(matcher, attributes);
			}
		}

		DefaultFilterInvocationSecurityMetadataSource metadataSource = new DefaultFilterInvocationSecurityMetadataSource(
				requestMap);
		return metadataSource;
	}

	@Override
	public Class<?> getObjectType() {
		return DefaultFilterInvocationSecurityMetadataSource.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
