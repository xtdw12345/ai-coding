package com.example.ticket.util;

import java.util.List;

/**
 * 分页结果封装类
 * 
 * @param <T> 数据类型
 * @author Project Alpha Team
 */
public class PageResult<T> {
    private List<T> data;        // 数据列表
    private int total;            // 总记录数
    private int page;             // 当前页码
    private int pageSize;         // 每页大小
    private int totalPages;      // 总页数

    public PageResult() {
    }

    public PageResult(List<T> data, int total, int page, int pageSize) {
        this.data = data;
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
        this.totalPages = (total + pageSize - 1) / pageSize; // 向上取整
    }

    // Getters and Setters
    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
        // 重新计算总页数
        this.totalPages = (total + pageSize - 1) / pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        // 重新计算总页数
        this.totalPages = (total + pageSize - 1) / pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * 是否有上一页
     */
    public boolean hasPrevious() {
        return page > 1;
    }

    /**
     * 是否有下一页
     */
    public boolean hasNext() {
        return page < totalPages;
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "data=" + (data != null ? data.size() : 0) + " items" +
                ", total=" + total +
                ", page=" + page +
                ", pageSize=" + pageSize +
                ", totalPages=" + totalPages +
                '}';
    }
}

