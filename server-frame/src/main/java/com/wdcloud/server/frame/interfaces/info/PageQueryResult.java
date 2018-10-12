package com.wdcloud.server.frame.interfaces.info;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询结果
 *
 * @author csf
 * @Date 2015/7/24.
 */
public class PageQueryResult<T> {
    public final long total;
    public final List<T> list;
    public int pageIndex;
    public int pageSize;

    public PageQueryResult() {
        this.total = 0;
        this.list = new ArrayList<>();
    }

    public PageQueryResult(long total, List<T> list) {
        this.total = total;
        this.list = list;
    }

    public PageQueryResult(long total, List<T> list, int pageSize, int pageIndex) {
        this.total = total;
        this.list = list;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

}
