package com.atguigu.ems.security;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.stereotype.Component;

@Component
public class EmsSecurityBeanPostProcessor implements BeanPostProcessor {
	private FilterSecurityInterceptor filterSecurityInterceptor;
	private EmsDefaultFilterInvocationSecurityMetadataSource metadataSource;
	private boolean isSetter = false;

	@Override
	public Object postProcessAfterInitialization(Object arg0, String arg1)
			throws BeansException {
		// 1.先获取FilterSecurityInterceptor对应的bean
		if (arg0 instanceof FilterSecurityInterceptor) {
			this.filterSecurityInterceptor = (FilterSecurityInterceptor) arg0;
		}
		// 2.再获取MyDefaultFilterInvocationSecurityMetadataSource2对应的bean
		if (arg0 instanceof EmsDefaultFilterInvocationSecurityMetadataSource) {
			this.metadataSource = (EmsDefaultFilterInvocationSecurityMetadataSource) arg0;
		}
		// 3.若这两个bean都获取到了，则替换属性
		if (this.filterSecurityInterceptor != null
				&& this.metadataSource != null && !isSetter) {
			try {
				filterSecurityInterceptor
						.setSecurityMetadataSource(metadataSource.getObject());
				isSetter = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return arg0;
	}

	@Override
	public Object postProcessBeforeInitialization(Object arg0, String arg1)
			throws BeansException {
		return arg0;
	}
}
