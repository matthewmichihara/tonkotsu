package com.fourpool.tonkotsu.data;

import java.util.List;

public class Photos {
    private final List<VenuePhotosGroup> groups;

    public Photos(List<VenuePhotosGroup> groups) {
        this.groups = groups;
    }

    public List<VenuePhotosGroup> getGroups() {
        return groups;
    }
}
