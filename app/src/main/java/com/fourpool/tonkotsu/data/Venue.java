package com.fourpool.tonkotsu.data;

import java.util.List;

public class Venue {
    private final String id;
    private final String name;
    private final List<Category> categories;
    private final Photos photos;
    private final float rating;
    private final String ratingColor;

    public Venue(String id, String name, List<Category> categories, Photos photos, float rating, String ratingColor) {
        this.id = id;
        this.name = name;
        this.categories = categories;
        this.photos = photos;
        this.rating = rating;
        this.ratingColor = ratingColor;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public float getRating() {
        return rating;
    }

    public String getRatingColor() {
        return ratingColor;
    }

    public Photos getPhotos() {
        return photos;
    }

    public String getPhotoUrl() {
        Photo photo = getPhotos() == null ? null
                : getPhotos().getGroups() == null ? null
                : getPhotos().getGroups().isEmpty() ? null
                : getPhotos().getGroups().get(0).getItems() == null ? null
                : getPhotos().getGroups().get(0).getItems().isEmpty() ? null
                : getPhotos().getGroups().get(0).getItems().get(0);

        return photo == null ? null
                : photo.getPrefix() + photo.getWidth() + "x" + photo.getHeight() + photo.getSuffix();
    }

    public String getCategoryIconUrl() {
        Icon icon = getCategories() == null ? null
                : getCategories().isEmpty() ? null
                : getCategories().get(0).getIcon();

        return icon == null ? null
                : icon.getPrefix() + "64" + icon.getSuffix();
    }
}
