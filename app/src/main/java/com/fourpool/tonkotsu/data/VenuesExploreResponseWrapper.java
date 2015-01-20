package com.fourpool.tonkotsu.data;

public class VenuesExploreResponseWrapper {
    private final Response response;

    public VenuesExploreResponseWrapper(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
