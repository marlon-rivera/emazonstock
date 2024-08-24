package com.emazon.stock.domain.model;

import java.util.List;

public class PaginationInfo<T> {

    private final List<T> list;
    private final int currentPage;
    private final int pageSize;
    private final long totalElements;
    private final int totalPages;
    private final boolean hasNextPage;
    private final boolean hasPreviousPage;

    public PaginationInfo(List<T> list, int currentPage, int pageSize, long totalElements, int totalPages, boolean hasNextPage, boolean hasPreviousPage) {
        this.list = list;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.hasNextPage = hasNextPage;
        this.hasPreviousPage = hasPreviousPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public List<T> getList() {
        return list;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

}
