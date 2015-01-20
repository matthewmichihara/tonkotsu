package com.fourpool.tonkotsu.data;

import java.util.List;

public class VenuePhotosGroup {
    private final List<Photo> items;

    public VenuePhotosGroup(List<Photo> items) {
        this.items = items;
    }

    public List<Photo> getItems() {
        return items;
    }
}
