package com.fourpool.tonkotsu.data;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface VenuesService {
    @GET("/v2/venues/explore?limit=50&venuePhotos=1&sortByDistance=1")
    Observable<VenuesExploreResponseWrapper> getBestNearby(@Query("query") String query, @Query("ll") String ll, @Query("llAcc") float llAcc);
}
