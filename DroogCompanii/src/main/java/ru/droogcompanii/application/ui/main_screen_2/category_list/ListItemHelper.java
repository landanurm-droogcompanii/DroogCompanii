package ru.droogcompanii.application.ui.main_screen_2.category_list;

import android.content.Context;
import android.view.View;

/**
 * Created by ls on 24.03.14.
 */
interface ListItemHelper {
    View makeView(Context context, View convertView);
    String getTitle(Context context);
    String getConditionToReceivePartners();
}
