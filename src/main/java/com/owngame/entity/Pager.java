package com.owngame.entity;

import java.io.Serializable;
import java.util.List;

public class Pager<T> implements Serializable {

    private static final long serialVersionUID = -8741766802354222579L;

    private int pageSize; // 每页显示多少条记录

    private int targetPage; //目标第几页数据

    private int totalRecords; // 一共多少条记录

    private int totalPages; // 一共多少页记录

    private List<T> dataList; //要显示的数据

    public Pager(int targetPage, int pageSize, int totalRecords, List<T> sourceList) {
        if (sourceList == null || sourceList.isEmpty()) {
            return;
        }

        // 总记录条数
        this.totalRecords = totalRecords;

        // 每页显示多少条记录
        this.pageSize = pageSize;

        //获取总页数
        this.totalPages = this.totalRecords / this.pageSize;
        if (this.totalRecords % this.pageSize != 0) {
            this.totalPages = this.totalPages + 1;
        }

        // 当前第几页数据
        this.targetPage = this.totalPages < targetPage ? this.totalPages : targetPage;

//		// 起始索引
//		int fromIndex	= this.pageSize * (this.targetPage -1);
//
//		// 结束索引
//		int toIndex  = this.pageSize * this.targetPage > this.totalRecords ? this.totalRecords : this.pageSize * this.targetPage;

        this.dataList = sourceList;
    }

    public Pager() {

    }

    public Pager(int pageSize, int targetPage, int totalRecord, int totalPage,
                 List<T> dataList) {
        super();
        this.pageSize = pageSize;
        this.targetPage = targetPage;
        this.totalRecords = totalRecord;
        this.totalPages = totalPage;
        this.dataList = dataList;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTargetPage() {
        return targetPage;
    }

    public void setTargetPage(int targetPage) {
        this.targetPage = targetPage;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "Pager{" +
                "pageSize=" + pageSize +
                ", targetPage=" + targetPage +
                ", totalRecords=" + totalRecords +
                ", totalPages=" + totalPages +
                ", dataList=" + dataList +
                '}';
    }
}
