package ru.droogcompanii.application.ui.fragment.partner_list;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;

/**
 * Created by ls on 14.01.14.
 */
class PartnerListAdapter extends ArrayAdapter<Partner> {

    private static final int ROW_LAYOUT_ID = R.layout.view_partner_list_item;

    private final PartnerListItemViewMaker itemViewMaker;


    public PartnerListAdapter(Context context, List<Partner> partners) {
        super(context, ROW_LAYOUT_ID, partners);
        itemViewMaker = new PartnerListItemViewMaker(context, ROW_LAYOUT_ID);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return itemViewMaker.make(convertView, getItem(position));
    }
}
