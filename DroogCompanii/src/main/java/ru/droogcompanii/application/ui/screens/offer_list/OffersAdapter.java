package ru.droogcompanii.application.ui.screens.offer_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.offers.CalendarRange;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.util.ui.view.ImageDownloader;
import ru.droogcompanii.application.util.CalendarUtils;

/**
 * Created by ls on 10.02.14.
 */
public class OffersAdapter extends ArrayAdapter<Offer> {

    private static final int ROW_RESOURCE_ID = R.layout.view_offer_row;

    private final ImageDownloader imageDownloader;

    public OffersAdapter(Context context, List offers) {
        super(context, ROW_RESOURCE_ID, offers);
        imageDownloader = new ImageDownloader();
    }

    public View getView(int position, View view, ViewGroup parent) {
        View rowView = (view != null) ? view : createView(parent);
        fillRow(rowView, getItem(position));
        return rowView;
    }

    private View createView(ViewGroup parent) {
        View view = inflateView(parent.getContext());
        view.setPadding(6, 6, 6, 6);
        return view;
    }

    private View inflateView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(ROW_RESOURCE_ID, null);
    }

    void fillRow(View rowView, Offer offer) {
        new RowViewFiller(rowView, offer).fill();
    }

    private class RowViewFiller {
        private final View rowView;
        private final Offer offer;

        RowViewFiller(View rowView, Offer offer) {
            this.rowView = rowView;
            this.offer = offer;
        }

        void fill() {
            fillShortDescription();
            fillDuration();
            fillImage();
        }

        private void fillShortDescription() {
            TextView shortDescription = (TextView) rowView.findViewById(R.id.shortDescription);
            shortDescription.setText(offer.getShortDescription());
        }

        private void fillDuration() {
            TextView duration = (TextView) rowView.findViewById(R.id.duration);
            duration.setText(prepareDurationText());
        }

        private String prepareDurationText() {
            if (offer.isSpecial()) {
                return getContext().getString(R.string.special_offer);
            }
            CalendarRange duration = offer.getDuration();
            String from = CalendarUtils.convertToString(duration.from());
            String to = CalendarUtils.convertToString(duration.to());
            return from + " - " + to;
        }

        private void fillImage() {
            ImageView imageView = (ImageView) rowView.findViewById(R.id.image);
            imageDownloader.download(offer.getImageUrl(), imageView);
        }
    }

}
