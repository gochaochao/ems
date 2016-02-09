package com.atguigu.ems.security;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.atguigu.ems.entities.Authority;
import com.atguigu.ems.entities.Resource;
import com.atguigu.ems.services.ResourceService;
import com.atguigu.ems.utils.ReflectionUtils;

@Component
public class FilterInvocationSecurityMetadataSourceMapBuilderImpl implements
		FilterInvocationSecurityMetadataSourceMapBuilder {
	@Autowired
	private ResourceService resourceService;

	@Override
	public LinkedHashMap<String, List<String>> buildSrcMap() {
		LinkedHashMap<String, List<String>> srcMap = new LinkedHashMap<>();
		List<Resource> resources = resourceService.getAll();
		for (Resource resource : resources) {
			Set<Authority> authorities = resource.getAuthorities();
			srcMap.put(resource.getUrl(), ReflectionUtils
					.fetchElementPropertyToList(authorities, "name"));
		}
		return srcMap;
	}
}
