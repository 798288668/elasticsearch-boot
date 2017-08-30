package com.bdfint.es.common;

/**
 * @author fengcheng
 * @version 2017/8/30
 */
public class BaseParam {

    private int pageNo = 1;
    private int pageSize = 10;
    private String searchContent;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSearchContent() {
        return searchContent;
    }

    public void setSearchContent(String searchContent) {
        this.searchContent = searchContent;
    }
}
