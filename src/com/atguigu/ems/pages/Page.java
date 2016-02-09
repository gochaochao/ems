package com.atguigu.ems.pages;

import java.util.List;

public class Page<T> {
	private int pageNumber = 1;
	private int pageSize = 5;
	private long totalRecord;
	private List<T> content;

	// 是否有前一页
	public boolean isHasPrev() {
		return (this.pageNumber > 1);
	}

	// 是否有后一页
	public boolean isHasNext() {
		return (this.pageNumber < getTotalPages());
	}

	// 计算总页数
	public int getTotalPages() {
		int totalPages = (int) (totalRecord / pageSize);
		if (totalPages % pageSize != 0) {
			totalPages++;
		}
		return totalPages;
	}

	// 验证pageNumber是否大于0
	public int getPageNumber() {
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotalRecord() {
		return totalRecord;
	}

	// 2. 先查询总的记录数. 并获取总页数, 并验证 pageNo 的合法性
	public void setTotalRecord(long totalRecord) {
		this.totalRecord = totalRecord;
		if ((this.pageNumber) > getTotalPages()) {
			this.pageNumber = getTotalPages();
		}
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}
}
