package com.ftn.eventhopper.shared.responses;

import java.util.List;

public class PagedResponse<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;

    // Getteri i setteri
    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
}
