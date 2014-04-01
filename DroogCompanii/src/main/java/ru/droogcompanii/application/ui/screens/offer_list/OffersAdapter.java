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
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.util.ui.view.ImageDownloader;

/**
 * Created by ls on 10.02.14.
 */
public class OffersAdapter extends ArrayAdapter<Offer> {

    private static final int ROW_RESOURCE_ID = R.layout.view_offer_row;

    private final ImageDownloader imageDownloader;
    private final OfferDurationTextMaker offerDurationTextMaker;

    public OffersAdapter(Context context, List offers) {
        super(context, ROW_RESOURCE_ID, offers);
        imageDownloader = new ImageDownloader();
        offerDurationTextMaker = new OfferDurationTextMaker(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = (convertView != null) ? convertView : inflateItemView();
        Offer item = getItem(position);
        ItemViewFiller itemViewFiller = new ItemViewFiller();
        itemViewFiller.fill(itemView, item);
        return itemView;
    }

    private View inflateItemView() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        return layoutInflater.inflate(ROW_RESOURCE_ID, null);
    }

    private class ItemViewFiller {
        private  View itemView;
        private Offer offer;

        void fill(View itemView, Offer offer) {
            this.itemView = itemView;
            this.offer = offer;
            fillShortDescription();
            fillDuration();
            fillImage();
        }

        private void fillShortDescription() {
            TextView shortDescription = (TextView) itemView.findViewById(R.id.shortDescription);
            shortDescription.setText(offer.getShortDescription());
        }

        private void fillDuration() {
            TextView duration = (TextView) itemView.findViewById(R.id.duration);
            duration.setText(offerDurationTextMaker.makeFrom(offer));
        }

        private void fillImage() {
            ImageView imageView = (ImageView) itemView.findViewById(R.id.image);
            imageDownloader.download(offer.getImageUrl(), imageView);
        }
    }

}
