package com.fourpool.tonkotsu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewTreeObserver;

import com.fourpool.tonkotsu.data.Venue;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends ActionBarActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final long ANIM_DURATION_TOOLBAR = 500;
    private static final long ANIM_START_DELAY_TOOLBAR = 300;

    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.refresh) SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.venues) RecyclerView venuesRecyclerView;

    @Inject @Named("best nearby venues") Observable<List<Venue>> bestNearbyVenues;

    private boolean pendingIntroAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TonkotsuApplication.get(this).inject(this);
        ButterKnife.inject(this);

        // Set up animation flag.
        if (savedInstanceState == null) {
            startIntroAnimation();
        }

        venuesRecyclerView.setHasFixedSize(true);

        final RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        venuesRecyclerView.setLayoutManager(lm);

        final List<Venue> venues = new ArrayList<>();
        final VenueAdapter.OnVenueClickListener clickListener = new VenueAdapter.OnVenueClickListener() {
            @Override
            public void onVenueClick(Venue venue) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.foursquare.com/v/" + venue.getId()));
                startActivity(intent);
            }
        };

        final RecyclerView.Adapter adapter = new VenueAdapter(this, venues, clickListener);

        venuesRecyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList(bestNearbyVenues, venues, adapter, swipeRefreshLayout);
            }
        });

        refreshList(bestNearbyVenues, venues, adapter, swipeRefreshLayout);
    }

    public void startIntroAnimation() {
        toolbar.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                toolbar.getViewTreeObserver().removeOnPreDrawListener(this);
                int actionBarSize = toolbar.getHeight();
                toolbar.setTranslationY(-actionBarSize);

                toolbar.animate().translationY(0).setStartDelay(ANIM_START_DELAY_TOOLBAR).setDuration(ANIM_DURATION_TOOLBAR);
                return true;
            }
        });
    }

    private void refreshList(final Observable<List<Venue>> bestNearbyVenues,
                             final List<Venue> venues,
                             final RecyclerView.Adapter adapter,
                             final SwipeRefreshLayout swipeRefreshLayout) {
        bestNearbyVenues
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Venue>>() {
                    @Override
                    public void call(List<Venue> venueList) {
                        Log.d(TAG, "Refreshing list.");
                        venues.clear();
                        venues.addAll(venueList);
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }
}
