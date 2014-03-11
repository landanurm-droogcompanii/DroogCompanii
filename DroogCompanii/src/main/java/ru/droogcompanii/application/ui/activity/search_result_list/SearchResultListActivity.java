package ru.droogcompanii.application.ui.activity.search_result_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.fragment.search_result_list.SearchResultListFragment;
import ru.droogcompanii.application.ui.util.ActionBarActivityWithUpButtonAndGoToMapItem;
import ru.droogcompanii.application.ui.util.FragmentUtils;

/**
 * Created by ls on 11.03.14.
 */
public class SearchResultListActivity extends ActionBarActivityWithUpButtonAndGoToMapItem {

    private static final String TAG_SEARCH_RESULT_LIST_FRAGMENT = "TAG_SEARCH_RESULT_LIST_FRAGMENT";

    private static final String KEY_QUERY = "KEY_QUERY";


    private String query;


    public static void start(Context context, String query) {
        Intent intent = new Intent(context, SearchResultListActivity.class);
        intent.putExtra(KEY_QUERY, query);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_list);
        if (savedInstanceState == null) {
            initStateByDefault();
        } else {
            restoreState(savedInstanceState);
        }
        setTitle(query);
    }

    private void initStateByDefault() {
        query = getIntent().getStringExtra(KEY_QUERY);
        placeFragmentOnLayout();
    }

    private void placeFragmentOnLayout() {
        Fragment fragment = SearchResultListFragment.newInstance(query);
        FragmentUtils.placeFragmentOnLayout(this,
                R.id.containerOfSearchResultList, fragment, TAG_SEARCH_RESULT_LIST_FRAGMENT);
    }

    private void restoreState(Bundle savedInstanceState) {
        query = savedInstanceState.getString(KEY_QUERY);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveStateInto(outState);
    }

    private void saveStateInto(Bundle outState) {
        outState.putString(KEY_QUERY, query);
    }

    @Override
    protected boolean isGoToMapItemVisible() {
        return true;
    }

}