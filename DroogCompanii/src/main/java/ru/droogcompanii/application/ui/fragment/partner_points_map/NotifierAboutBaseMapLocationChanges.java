package ru.droogcompanii.application.ui.fragment.partner_points_map;

import android.app.Activity;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.util.view.ToastUtils;

/**
 * Created by ls on 11.03.14.
 */
public class NotifierAboutBaseMapLocationChanges {

    public static void notify(Activity activity) {
        ToastUtils.SHORT.show(activity, R.string.message_on_custom_base_location_changed);
    }
}
