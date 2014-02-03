package ru.droogcompanii.application.ui.activity.menu_activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import ru.droogcompanii.application.R;

/**
 * Created by ls on 31.01.14.
 */
public class MenuActivity extends FragmentActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private MenuListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        adapter = new MenuListAdapter(this);
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        MenuListItem item = adapter.getItem(position);
        MenuListItem.Action action = item.action;
        action.run(this);
    }
}