package ru.droogcompanii.application.view.activity_3.filter_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.view.fragment.filter_fragment.FilterFragment;

/**
 * Created by ls on 15.01.14.
 */

public class FilterActivity extends FragmentActivity {

    public static final int REQUEST_CODE = 14235;

    private Bundle args;
    private FilterFragment filterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3_filter);

        if (savedInstanceState == null) {
            args = getIntent().getExtras();
        } else {
            args = savedInstanceState.getBundle(Keys.args);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        filterFragment = new FilterFragment();
        filterFragment.setArguments(args);

        if (savedInstanceState == null) {
            fragmentTransaction.add(R.id.containerOfFilterFragment, filterFragment);
        } else {
            fragmentTransaction.replace(R.id.containerOfFilterFragment, filterFragment);
        }

        fragmentTransaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(Keys.args, args);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Keys.filters, filterFragment.getFilters());
        setResult(RESULT_OK, returnIntent);
        super.onBackPressed();
    }
}
