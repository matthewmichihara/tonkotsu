package com.fourpool.tonkotsu.location;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;


public class LocationObservable implements Observable.OnSubscribe<Location> {
    private final Context context;
    private final LocationRequest locationRequest;
    private LocationListener listener;

    public static Observable<Location> createObservable(Context ctx, LocationRequest locationRequest) {
        return Observable.create(new LocationObservable(ctx, locationRequest));
    }

    protected LocationObservable(Context context, LocationRequest locationRequest) {
        this.context = context;
        this.locationRequest = locationRequest;
    }

    @Override
    public void call(Subscriber<? super Location> subscriber) {
        final LocationConnectionCallbacks connectionCallbacks = new LocationConnectionCallbacks(subscriber);

        final GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(connectionCallbacks)
                .addApi(LocationServices.API)
                .build();

        connectionCallbacks.setClient(googleApiClient);

        try {
            googleApiClient.connect();
        } catch (Throwable ex) {
            subscriber.onError(ex);
        }

        subscriber.add(Subscriptions.create(new Action0() {
            @Override
            public void call() {
                if (googleApiClient.isConnected() || googleApiClient.isConnecting()) {
                    onUnsubscribed(googleApiClient);
                    googleApiClient.disconnect();
                }
            }
        }));
    }

    protected void onLocationClientReady(GoogleApiClient googleApiClient, final Observer<? super Location> observer) {
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                observer.onNext(location);
            }
        };
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, listener);
    }

    protected void onUnsubscribed(GoogleApiClient googleApiClient) {
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, listener);
        }
    }

    protected void onLocationClientDisconnected(Observer<? super Location> observer) {
        observer.onCompleted();
    }

    private class LocationConnectionCallbacks implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
        final private Observer<? super Location> observer;
        private GoogleApiClient googleApiClient;

        private LocationConnectionCallbacks(Observer<? super Location> observer) {
            this.observer = observer;
        }

        @Override
        public void onConnected(Bundle bundle) {
            try {
                onLocationClientReady(googleApiClient, observer);
            } catch (Throwable ex) {
                observer.onError(ex);
            }
        }

        @Override
        public void onConnectionSuspended(int i) {
            try {
                onLocationClientDisconnected(observer);
            } catch (Throwable ex) {
                observer.onError(ex);
            }
        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            observer.onError(new LocationConnectionException("Error connecting to LocationClient.", connectionResult));
        }

        public void setClient(GoogleApiClient client) {
            this.googleApiClient = client;
        }
    }
}
