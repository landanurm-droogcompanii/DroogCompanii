package ru.droogcompanii.application.ui.fragment.partner_point_list;

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
    public View getView(int position, View itemView, ViewGroup parent) {
        if (itemView == null) {
            PartnerPointDetailsViewInflater inflater = new PartnerPointDetailsViewInflater(getContext());
            itemView = inflater.inflate();
        }
        PartnerPointDetailsFiller.fill(itemView, getItem(position));
        return itemView;
    }
}
