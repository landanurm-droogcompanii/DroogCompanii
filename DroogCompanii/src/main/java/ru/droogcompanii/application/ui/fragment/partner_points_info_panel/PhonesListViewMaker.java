package ru.droogcompanii.application.ui.fragment.partner_points_info_panel;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * Created by ls on 03.02.14.
 */
class PhonesListViewMaker {

    static interface OnNeedToCallListener {
        void onNeedToCall(String phone);
    }

    public static ListView make(
            Context context, List<String> phones, final OnNeedToCallListener onNeedToCallListener) {

        ListView listView = new ListView(context);
        final ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, phones);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String phone = adapter.getItem(position);
                onNeedToCallListener.onNeedToCall(phone);
            }
        });
        return listView;
    }
}
