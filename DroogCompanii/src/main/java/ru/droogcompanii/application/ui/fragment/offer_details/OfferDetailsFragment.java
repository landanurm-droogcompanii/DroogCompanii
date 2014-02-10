package ru.droogcompanii.application.ui.fragment.offer_details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.util.ImageDownloader;
import ru.droogcompanii.application.util.Keys;

/**
 * Created by ls on 10.02.14.
 */
public class OfferDetailsFragment extends Fragment {

    private final ImageDownloader imageDownloader = new ImageDownloader();

    private ImageView image;
    private TextView shortDescription;
    private TextView fullDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_offer_details, container, false);
        findViews(root);
        return root;
    }

    private void findViews(View root) {
        shortDescription = (TextView) root.findViewById(R.id.shortDescription);
        image = (ImageView) root.findViewById(R.id.image);
        fullDescription = (TextView) root.findViewById(R.id.fullDescription);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            init();
        }
    }

    private void init() {
        Bundle args = getArguments();
        Offer offer = (Offer) args.getSerializable(Keys.offer);
        init(offer);
    }

    private void init(Offer offer) {
        shortDescription.setText(offer.getShortDescription());
        fullDescription.setText(offer.getFullDescription());
        imageDownloader.download(offer.getImageUrl(), image);
    }
}
