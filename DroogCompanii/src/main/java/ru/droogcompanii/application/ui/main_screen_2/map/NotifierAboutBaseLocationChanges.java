package ru.droogcompanii.application.ui.main_screen_2.map;

import android.app.Activity;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.util.view.ToastUtils;

/**
 * Created by ls on 11.03.14.
 */
public class NotifierAboutBaseLocationChanges {

    public static void notify(Activity activity) {
        ToastUtils.SHORT.show(activity, R.string.message_on_custom_base_location_changed);
    }
}
