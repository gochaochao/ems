package com.atguigu.ems.security;

import java.util.LinkedHashMap;
import java.util.List;

public interface FilterInvocationSecurityMetadataSourceMapBuilder {
	// 返回受保护的资源和能访问该资源的权限的名字的集合
	public LinkedHashMap<String, List<String>> buildSrcMap();
}
