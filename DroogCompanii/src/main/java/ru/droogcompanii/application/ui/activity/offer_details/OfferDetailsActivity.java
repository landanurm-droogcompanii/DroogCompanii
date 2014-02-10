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
import ru.droogcompanii.application.ui.helpers.ActionBarActivityWithUpButton;
import ru.droogcompanii.application.util.Keys;

/**
 * Created by ls on 10.02.14.
 */
public class OfferDetailsActivity extends ActionBarActivityWithUpButton {

    public static void start(Context context, Offer offer) {
        Intent intent = new Intent(context, OfferDetailsActivity.class);
        Bundle args = new Bundle();
        args.putSerializable(Keys.offer, (Serializable) offer);
        intent.putExtra(Keys.args, args);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);
        if (savedInstanceState == null) {
            prepareFragment();
        }
    }

    private void prepareFragment() {
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
        return intent.getBundleExtra(Keys.args);
    }
}
