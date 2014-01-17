package ru.droogcompanii.application.view.activity_3.partner_activity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;

/**
 * Created by ls on 15.01.14.
 */
public class PartnerPointListAdapter extends ArrayAdapter<PartnerPoint> {

    public PartnerPointListAdapter(Context context, List<PartnerPoint> partnerPoints) {
        super(context, R.layout.view_partner_point_list_item, partnerPoints);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            PartnerPointListItemViewInflater inflater = new PartnerPointListItemViewInflater(getContext());
            itemView = inflater.inflate();
        }
        PartnerPointInfoFiller.fill(itemView, getItem(position));
        return itemView;
    }
}
