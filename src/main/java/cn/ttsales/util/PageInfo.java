package cn.ttsales.util;

import static cn.ttsales.util.Constant.*;
/**
 * Created by 露青 on 2016/10/20.
 */
public class PageInfo {
    private int page;
    private int size;
    private String sortName;

    public PageInfo(int page, int size, String sortName) {
        this.page = page;
        this.size = size;
        this.sortName = sortName;
    }

    public PageInfo(int page, String sortName) {
        this(page, PAGE_SIZE, sortName);
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

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }
}
