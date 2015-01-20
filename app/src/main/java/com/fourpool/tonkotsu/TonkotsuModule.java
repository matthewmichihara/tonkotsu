package com.fourpool.tonkotsu;

import android.location.Location;
import android.util.Log;

import com.fourpool.tonkotsu.data.GroupItem;
import com.fourpool.tonkotsu.data.Item;
import com.fourpool.tonkotsu.data.Venue;
import com.fourpool.tonkotsu.data.VenuesExploreResponseWrapper;
import com.fourpool.tonkotsu.data.VenuesService;
import com.fourpool.tonkotsu.location.LocationObservable;
import com.google.android.gms.location.LocationRequest;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import rx.Observable;
import rx.functions.Func1;

@Module(
        injects = {
                MainActivity.class
        }
)
public final class TonkotsuModule {
    private final TonkotsuApplication app;

    public TonkotsuModule(TonkotsuApplication app) {
        this.app = app;
    }

    @Provides
    @Singleton
    VenuesService provideVenueService() {
        final RequestInterceptor interceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addQueryParam("client_id", "RO05FQUOXGU5JIY5JP0ZTP1MCOL0AOCUZ1BFXYQLRXNWE3VE");
                request.addQueryParam("client_secret", "RD40MWT4YZDTJYAJDJLRQPKZG4QBFYVZM0SOZVALLVIHKMDV");
                request.addQueryParam("v", "20141022");
            }
        };

        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.foursquare.com")
                .setRequestInterceptor(interceptor)
                .build();

        return restAdapter.create(VenuesService.class);
    }

    @Provides
    @Singleton
    @Named("current location")
    Observable<Location> provideCurrentLocation() {
        final LocationRequest request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setFastestInterval(1000)
                .setInterval(1000);

        return LocationObservable.createObservable(app, request)
                .filter(new Func1<Location, Boolean>() {
                    @Override
                    public Boolean call(Location location) {
                        return location.getAccuracy() < 30;
                    }
                })
                .first();
    }

    @Provides
    @Singleton
    @Named("best nearby venues")
    Observable<List<Venue>> provideBestNearbyList(@Named("current location") Observable<Location> currentLocation, final VenuesService venuesService) {
        return currentLocation
                .flatMap(new Func1<Location, Observable<VenuesExploreResponseWrapper>>() {
                    @Override
                    public Observable<VenuesExploreResponseWrapper> call(Location location) {
                        String ll = location.getLatitude() + "," + location.getLongitude();
                        float llAcc = location.getAccuracy();
                        return venuesService.getBestNearby("ramen", ll, llAcc);
                    }
                })
                .flatMap(new Func1<VenuesExploreResponseWrapper, Observable<List<Venue>>>() {
                    @Override
                    public Observable<List<Venue>> call(VenuesExploreResponseWrapper venuesExploreResponseWrapper) {
                        List<Venue> venues = new ArrayList<>();
                        List<GroupItem> groups = venuesExploreResponseWrapper.getResponse().getGroups();
                        for (GroupItem group : groups) {
                            for (Item item : group.getItems()) {
                                venues.add(item.getVenue());
                            }
                        }
                        return Observable.just(venues);
                    }
                });
    }
}
