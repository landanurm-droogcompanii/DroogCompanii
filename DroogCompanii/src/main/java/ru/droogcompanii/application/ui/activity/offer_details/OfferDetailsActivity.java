package ru.droogcompanii.application.ui.activity.offer_details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.ui.fragment.offer_details.OfferDetailsFragment;
import ru.droogcompanii.application.ui.util.activity.ActionBarActivityWithUpButton;

/**
 * Created by ls on 10.02.14.
 */
public class OfferDetailsActivity extends ActionBarActivityWithUpButton {

    public static class Key {
        public static final String ARGS = "args";
        public static final String OFFER = "offer";
    }

    public static void start(Context context, Offer offer) {
        Intent intent = new Intent(context, OfferDetailsActivity.class);
        Bundle args = new Bundle();
        args.putSerializable(Key.OFFER, (Serializable) offer);
        intent.putExtra(Key.ARGS, args);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);
        if (savedInstanceState == null) {
            startFragment();
        }
    }

    private void startFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.containerOfFragment, createFragment());
        transaction.commit();
    }

    private Fragment createFragment() {
        OfferDetailsFragment fragment = new OfferDetailsFragment();
        fragment.setArguments(getPassedArguments());
        return fragment;
    }

    private Bundle getPassedArguments() {
        Intent intent = getIntent();
        return intent.getBundleExtra(Key.ARGS);
    }
}
