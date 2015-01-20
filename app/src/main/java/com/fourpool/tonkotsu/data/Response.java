package com.fourpool.tonkotsu.data;

import java.util.List;

public class Response {
    private final List<GroupItem> groups;

    public Response(List<GroupItem> groups) {
        this.groups = groups;
    }

    public List<GroupItem> getGroups() {
        return groups;
    }
}
