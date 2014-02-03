package ru.droogcompanii.application.ui.fragment.partner_points_info_panel_fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.helpers.Caller;

/**
 * Created by ls on 03.02.14.
 */
public class MultiPhonesCallerDialog extends Dialog {
    private final List<String> phones;

    public MultiPhonesCallerDialog(Context context, List<String> phones) {
        super(context);
        this.phones = phones;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.titleOfMultiPhonesCallerDialog);
        setContentView(prepareContentView());
    }

    private View prepareContentView() {
        Context context = getContext();
        ListView listView = new ListView(context);
        final ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, phones);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String phone = adapter.getItem(position);
                onNeedToCall(phone);
            }
        });
        return listView;
    }

    private void onNeedToCall(String phone) {
        Caller.call(getContext(), phone);
        dismiss();
    }
}
