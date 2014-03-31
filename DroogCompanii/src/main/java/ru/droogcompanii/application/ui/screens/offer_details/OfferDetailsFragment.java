package ru.droogcompanii.application.ui.screens.offer_details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.ui.screens.partner_details.PartnerDetailsActivity;
import ru.droogcompanii.application.util.StateManager;
import ru.droogcompanii.application.util.ui.view.ImageDownloader;

/**
 * Created by ls on 10.02.14.
 */
public class OfferDetailsFragment extends Fragment {

    private static class Key extends OfferDetailsActivity.Key {
        // inherits static fields
    }

    private final ImageDownloader imageDownloader = new ImageDownloader();

    private ImageView image;
    private TextView shortDescription;
    private TextView fullDescription;
    private View goToPartner;

    private boolean areOffersByOnePartner;
    private Offer offer;

    private final StateManager STATE_MANAGER = new StateManager() {
        @Override
        public void initStateByDefault() {
            offer = (Offer) getArguments().getSerializable(Key.OFFER);
            areOffersByOnePartner = getArguments().getBoolean(Key.ARE_OFFERS_BY_ONE_PARTNER);
        }

        @Override
        public void restoreState(Bundle savedInstanceState) {
            offer = (Offer) savedInstanceState.getSerializable(Key.OFFER);
            areOffersByOnePartner = savedInstanceState.getBoolean(Key.ARE_OFFERS_BY_ONE_PARTNER);
        }

        @Override
        public void saveState(Bundle outState) {
            outState.putSerializable(Key.OFFER, (Serializable) offer);
            outState.putBoolean(Key.ARE_OFFERS_BY_ONE_PARTNER, areOffersByOnePartner);
        }
    };

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
        goToPartner = root.findViewById(R.id.goToPartnerButton);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        STATE_MANAGER.initState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        STATE_MANAGER.saveState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        shortDescription.setText(offer.getShortDescription());
        fullDescription.setText(offer.getFullDescription());
        imageDownloader.download(offer.getImageUrl(), image);
        if (areOffersByOnePartner) {
            goToPartner.setVisibility(View.INVISIBLE);
        } else {
            goToPartner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToPartner();
                }
            });
        }
    }

    private void goToPartner() {
        PartnerDetailsActivity.startWithoutFilters(getActivity(), offer);
    }

}
