package ru.droogcompanii.application.ui.activity.learn_maps_utility.marker_clustering;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.fragment.learn_maps_utility.marker_clustering.LearnMarkerClusteringFragment;
import ru.droogcompanii.application.util.activity.ActionBarActivityWithUpButton;

/**
 * Created by ls on 14.03.14.
 */
public class LearnMarkerClusteringActivity extends ActionBarActivityWithUpButton {

    private static final String TAG_FRAGMENT = "TAG_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_marker_clustering);

        if (savedInstanceState == null) {
            placeFragmentOnLayout();
        }
    }

    private void placeFragmentOnLayout() {
        Fragment fragment = new LearnMarkerClusteringFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.rootLayout, fragment, TAG_FRAGMENT);
        transaction.commit();
    }
}
