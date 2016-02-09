package com.atguigu.ems.actions;

import java.io.InputStream;
import java.util.Map;

import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.atguigu.ems.pages.Page;
import com.atguigu.ems.services.BaseService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class BaseAction<T> extends ActionSupport implements RequestAware,
		SessionAware, ModelDriven<T>, Preparable, ParameterAware {

	private static final long serialVersionUID = 1L;

	@Autowired
	protected BaseService<T> service;

	protected Page<T> page = new Page<>();

	public void setPage(Page<T> page) {
		this.page = page;
	}

	public Page<T> getPage() {
		return page;
	}

	protected Integer id;

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	// 文件下载相关的属性
	protected String contentType;
	protected long contentLength;
	protected String fileName;
	protected InputStream inputStream;

	public String getContentType() {
		return contentType;
	}

	public long getContentLength() {
		return contentLength;
	}

	public String getContentDisposition() {
		return "attachment;filename=" + fileName;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	@Override
	public void prepare() throws Exception {
	}

	protected T model;

	@Override
	public T getModel() {
		return model;
	}

	protected Map<String, Object> session;

	@Override
	public void setSession(Map<String, Object> arg0) {
		this.session = arg0;
	}

	protected Map<String, Object> request;

	@Override
	public void setRequest(Map<String, Object> arg0) {
		this.request = arg0;
	}

	protected Map<String, String[]> parameters;

	@Override
	public void setParameters(Map<String, String[]> arg0) {
		this.parameters = arg0;
	}
}
