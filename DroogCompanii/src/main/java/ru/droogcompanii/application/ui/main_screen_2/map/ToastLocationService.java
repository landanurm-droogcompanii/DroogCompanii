package ru.droogcompanii.application.ui.main_screen_2.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.util.SharedPreferencesProvider;
import ru.droogcompanii.application.util.view.CustomToast;

/**
 * Created by ls on 28.03.14.
 */
public class ToastLocationService extends CustomToast {

    private static final int LAYOUT_ID = R.layout.view_toast_location_service;

    public ToastLocationService(final Activity activity) {
        super(activity, R.id.mainContent, LAYOUT_ID, new ViewInitializer() {
            @Override
            public void init(CustomToast customToast, View view) {
                view.findViewById(R.id.buttonLocation).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openLocationSettingsScreen(activity);
                    }
                });
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBoxDoNotShowAgain);
                initCheckBox(activity, customToast, checkBox);

            }
        });
    }

    private static void openLocationSettingsScreen(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        activity.startActivity(intent);
    }

    private static void initCheckBox(final Context context,
                 final CustomToast customToast, CheckBox checkBoxDoNotShowAgain) {
        checkBoxDoNotShowAgain.setChecked(DoNotShowAgain.read(context));
        checkBoxDoNotShowAgain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checkedDoNotShowAgain) {
                DoNotShowAgain.share(context, checkedDoNotShowAgain);
                if (checkedDoNotShowAgain) {
                    customToast.hide();
                } else {
                    customToast.stopHiding();
                }
            }
        });
    }

    private static class DoNotShowAgain {
        private static final boolean DEFAULT_DO_NOT_SHOW_AGAIN = false;
        private static final String KEY_DO_NOT_SHOW_AGAIN = DoNotShowAgain.class.getName();

        public static void share(Context context, boolean doNotShowAgain) {
            SharedPreferences sharedPreferences = SharedPreferencesProvider.get(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(KEY_DO_NOT_SHOW_AGAIN, doNotShowAgain);
            editor.commit();
        }

        public static boolean read(Context context) {
            SharedPreferences sharedPreferences = SharedPreferencesProvider.get(context);
            return sharedPreferences.getBoolean(KEY_DO_NOT_SHOW_AGAIN, DEFAULT_DO_NOT_SHOW_AGAIN);
        }
    }

    @Override
    public void show(int duration) {
        boolean doNotShowAgain = DoNotShowAgain.read(getActivity());
        if (doNotShowAgain) {
            return;
        }
        super.show(duration);
    }

}
