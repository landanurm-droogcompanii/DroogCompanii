package ru.droogcompanii.application.ui.fragment.offer_details;

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
import ru.droogcompanii.application.ui.activity.offer_details.OfferDetailsActivity;
import ru.droogcompanii.application.ui.activity.partner_details.PartnerAndPartnerPointsProviderByPartner;
import ru.droogcompanii.application.ui.activity.partner_details.PartnerDetailsActivity;
import ru.droogcompanii.application.ui.util.ImageDownloader;

/**
 * Created by ls on 10.02.14.
 */
public class OfferDetailsFragment extends Fragment {

    private final ImageDownloader imageDownloader = new ImageDownloader();

    private ImageView image;
    private TextView shortDescription;
    private TextView fullDescription;
    private View goToPartner;

    private Offer offer;

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        offer = extractOffer(savedInstanceState);
        init();
    }

    private Offer extractOffer(Bundle savedInstanceState) {
        Bundle bundle = (savedInstanceState == null) ? getArguments() : savedInstanceState;
        return (Offer) bundle.getSerializable(OfferDetailsActivity.Key.OFFER);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(OfferDetailsActivity.Key.OFFER, (Serializable) offer);
    }

    private void init() {
        shortDescription.setText(offer.getShortDescription());
        fullDescription.setText(offer.getFullDescription());
        imageDownloader.download(offer.getImageUrl(), image);
        goToPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PartnerDetailsActivity.start(getActivity(), new PartnerAndPartnerPointsProviderByOffer(offer));
            }
        });
    }

    private static class PartnerAndPartnerPointsProviderByOffer
            extends PartnerAndPartnerPointsProviderByPartner implements Serializable {

        public PartnerAndPartnerPointsProviderByOffer(Offer offer) {
            super(offer.getPartnerId());
        }

    }
}
