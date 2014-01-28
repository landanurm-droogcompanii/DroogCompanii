package ru.droogcompanii.application.ui.activity_3.search_result_activity;

import android.content.Context;

import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.ui.helpers.SimpleArrayAdapter;

/**
 * Created by ls on 14.01.14.
 */
public class PartnerListAdapter extends SimpleArrayAdapter<Partner> {
    public PartnerListAdapter(Context context, List<Partner> partners) {
        super(context, partners, new ItemToTitleConvertor<Partner>() {
            @Override
            public String getTitle(Partner item) {
                return item.title;
            }
        });
    }
}
