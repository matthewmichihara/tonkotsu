package com.fourpool.tonkotsu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourpool.tonkotsu.data.Venue;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class VenueAdapter extends RecyclerView.Adapter<VenueAdapter.ViewHolder> {
    private final Context context;
    private final List<Venue> venues;
    private final OnVenueClickListener clickListener;

    public interface OnVenueClickListener {
        void onVenueClick(Venue venue);
    }

    public VenueAdapter(Context context, List<Venue> venues, OnVenueClickListener clickListener) {
        this.context = context;
        this.venues = venues;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_venue, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final Venue venue = venues.get(i);

        Picasso.with(context).load(venue.getCategoryIconUrl()).into(viewHolder.venueIcon);
        Picasso.with(context).load(venue.getPhotoUrl()).fit().centerCrop().into(viewHolder.venueBackground);

        viewHolder.venueName.setText(venue.getName());

        String rating;
        if (venue.getRating() == 0) {
            rating = "-";
        } else {
            rating = String.valueOf(venue.getRating());
        }
        viewHolder.venueRating.setText(rating);

        String ratingColor = venue.getRatingColor();
        String ratingColorHex;
        if (TextUtils.isEmpty(ratingColor)) {
            ratingColorHex = "#ffffff";
        } else {
            ratingColorHex = "#" + ratingColor;
        }
        viewHolder.venueRating.setTextColor(Color.parseColor(ratingColorHex));

        viewHolder.venueOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onVenueClick(venue);
            }
        });
    }

    @Override
    public int getItemCount() {
        return venues.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        final ImageView venueBackground;
        final ImageView venueOverlay;
        final ImageView venueIcon;
        final TextView venueName;
        final TextView venueRating;

        public ViewHolder(View itemView) {
            super(itemView);

            venueBackground = (ImageView) itemView.findViewById(R.id.venue_background);
            venueOverlay = (ImageView) itemView.findViewById(R.id.venue_overlay);
            venueIcon = (ImageView) itemView.findViewById(R.id.venue_icon);
            venueName = (TextView) itemView.findViewById(R.id.venue_name);
            venueRating = (TextView) itemView.findViewById(R.id.venue_rating);
        }
    }
}
