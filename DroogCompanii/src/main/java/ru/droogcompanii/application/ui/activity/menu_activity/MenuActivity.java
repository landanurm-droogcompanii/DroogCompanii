package ru.droogcompanii.application.ui.activity.menu_activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.helpers.ActionBarActivityWithUpButton;

/**
 * Created by ls on 31.01.14.
 */
public class MenuActivity extends ActionBarActivityWithUpButton implements AdapterView.OnItemClickListener {

    private ListView listView;
    private MenuListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        adapter = new MenuListAdapter(this);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        MenuListItem item = adapter.getItem(position);
        item.action.launch(this);
    }
}
