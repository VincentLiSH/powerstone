package org.powerstone.smartpagination.common;

import java.util.List;

public class PageResult {
	private List pageData;
	private int totalRecordsNumber;
	private int pageAmount;

	public PageResult(List data, int recordsNumber, int pageAmount) {
		super();
		this.pageData = data;
		this.totalRecordsNumber = recordsNumber;
		this.pageAmount = pageAmount;
	}

	public PageResult() {
	}

	public List getPageData() {
		return pageData;
	}

	public void setPageData(List data) {
		this.pageData = data;
	}

	public int getTotalRecordsNumber() {
		return totalRecordsNumber;
	}

	public void setTotalRecordsNumber(int recordsNumber) {
		this.totalRecordsNumber = recordsNumber;
	}

	public int getPageAmount() {
		return pageAmount;
	}

	public void setPageAmount(int pageAmount) {
		this.pageAmount = pageAmount;
	}
}
