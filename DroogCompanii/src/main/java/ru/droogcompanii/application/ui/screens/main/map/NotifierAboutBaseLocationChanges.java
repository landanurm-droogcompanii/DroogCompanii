package ru.droogcompanii.application.ui.screens.main.map;

import android.app.Activity;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.util.ui.view.ToastUtils;

/**
 * Created by ls on 11.03.14.
 */
public class NotifierAboutBaseLocationChanges {

    public static void notify(Activity activity) {
        ToastUtils.SHORT.show(activity, R.string.message_on_custom_base_location_changed);
    }
}
