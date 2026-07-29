package org.killeryt.killerCoreAPI.utils;

import java.util.List;

public class PaginationHelper<T> {

    private final List<T> items;
    private final int pageSize;

    public PaginationHelper(List<T> items, int pageSize) {
        this.items = items;
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return (int) Math.ceil((double) items.size() / pageSize);
    }

    public List<T> getPage(int page) {
        int from = page * pageSize;
        if (from >= items.size()) return List.of();
        int to = Math.min(from + pageSize, items.size());
        return items.subList(from, to);
    }
}