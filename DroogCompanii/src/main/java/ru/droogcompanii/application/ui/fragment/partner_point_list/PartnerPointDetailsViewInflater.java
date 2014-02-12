package ru.droogcompanii.application.ui.fragment.partner_point_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import ru.droogcompanii.application.R;

/**
 * Created by ls on 15.01.14.
 */
public class PartnerPointDetailsViewInflater {
    private Context context;

    public PartnerPointDetailsViewInflater(Context context) {
        this.context = context;
    }

    public View inflate() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.view_partner_point_list_item, null, false);
    }
}
