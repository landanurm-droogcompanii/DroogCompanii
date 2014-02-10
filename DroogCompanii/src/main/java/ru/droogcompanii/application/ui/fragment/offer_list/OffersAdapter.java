package ru.droogcompanii.application.ui.fragment.offer_list;

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
import ru.droogcompanii.application.util.ImageDownloader;

/**
 * Created by ls on 10.02.14.
 */
public class OffersAdapter extends ArrayAdapter<Offer> {

    private static final int ROW_RESOURCE_ID = R.layout.view_offer_row;

    private final ImageDownloader imageDownloader;

    public OffersAdapter(Context context, List<Offer> offers) {
        super(context, ROW_RESOURCE_ID, offers);
        imageDownloader = new ImageDownloader();
    }

    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = createView(parent);
        }
        initView(view, getItem(position));
        return view;
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

    private void initView(View view, Offer offer) {
        TextView shortDescription = (TextView) view.findViewById(R.id.shortDescription);
        shortDescription.setText(offer.getShortDescription());
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        imageDownloader.download(offer.getImageUrl(), imageView);
    }

    public ImageDownloader getImageDownloader() {
        return imageDownloader;
    }


}
