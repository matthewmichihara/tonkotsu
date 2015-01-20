package com.fourpool.tonkotsu.data;

import java.util.List;

public class GroupItem {
    private final List<Item> items;

    public GroupItem(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }
}
