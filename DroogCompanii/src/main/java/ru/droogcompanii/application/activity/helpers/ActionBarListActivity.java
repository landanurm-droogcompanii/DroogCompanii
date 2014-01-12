package ru.droogcompanii.application.activity.helpers;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import ru.droogcompanii.application.R;

/**
 * Created by ls on 24.12.13.
 */
public class ActionBarListActivity extends ActionBarActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_list_view);
    }

    protected ListView getListView() {
        if (listView == null) {
            listView = (ListView) findViewById(android.R.id.list);
        }
        return listView;
    }

    protected void setListAdapter(ListAdapter adapter) {
        getListView().setAdapter(adapter);
    }

    protected ListAdapter getListAdapter() {
        ListAdapter adapter = getListView().getAdapter();
        if (adapter instanceof HeaderViewListAdapter) {
            return ((HeaderViewListAdapter)adapter).getWrappedAdapter();
        } else {
            return adapter;
        }
    }
}