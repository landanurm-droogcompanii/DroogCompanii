package ru.droogcompanii.application.ui.screens.partner_list;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;

/**
 * Created by ls on 14.01.14.
 */
class PartnerListAdapter extends ArrayAdapter<Partner> {
    private final PartnerListItemViewMaker itemViewMaker;

    public PartnerListAdapter(Context context, List<Partner> partners) {
        super(context, PartnerListItemViewMaker.ROW_LAYOUT_ID, partners);
        itemViewMaker = new PartnerListItemViewMaker(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return itemViewMaker.make(convertView, getItem(position));
    }
}
