package com.aradata.plaidapp.model.content.response;

import java.util.Iterator;
import java.util.List;

public class PagedResponse<T> implements Iterable<T>{

	private List<T> content;
	private int page;
	private int size;
	private long totalElements;
	private int totalPages;

	public PagedResponse(List<T> content, int page, int size, long totalElements, int totalPages) {
		this.content = content;
		this.page = page;
		this.size = size;
		this.totalElements = totalElements;
		this.totalPages = totalPages;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	@Override
	public Iterator<T> iterator() {
		return content.iterator();
	}
}
