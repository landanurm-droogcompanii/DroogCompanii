package ru.droogcompanii.application.ui.screens.main.map;

import android.content.Context;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.util.ui.view.ToastUtils;

/**
 * Created by ls on 11.03.14.
 */
class NotifierAboutBaseLocationChanges {

    public static void notify(Context context) {
        ToastUtils.SHORT.show(context, R.string.message_on_custom_base_location_changed);
    }
}
