package ru.droogcompanii.application.ui.screens.main.map.notifier_location_service;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.util.LocationSettingsScreenOpener;

/**
 * Created by ls on 31.03.14.
 */
public class NotifierLocationServiceDialog extends Dialog {

    public static interface Callbacks {
        void onNeedToCloseDialog();
    }


    private final Callbacks callbacks;

    public NotifierLocationServiceDialog(Context context, Callbacks callbacks) {
        super(context, R.style.DialogWithoutTitle);
        this.callbacks = callbacks;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_notifier_location_service);
        initView();
    }

    private void initView() {
        initCheckBox();
        initButtonGoToLocationSettings();
        initCancelButton();
    }

    private void initCheckBox() {
        CheckBox checkBoxDoNotShowAgain = (CheckBox) findViewById(R.id.checkBoxDoNotShowAgain);
        boolean doNotShowAgain = FlagDoNotShowAgain.read(getContext());
        checkBoxDoNotShowAgain.setChecked(doNotShowAgain);
        checkBoxDoNotShowAgain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checkedDoNotShowAgain) {
                FlagDoNotShowAgain.share(getContext(), checkedDoNotShowAgain);
            }
        });
    }

    private void initButtonGoToLocationSettings() {
        findViewById(R.id.buttonGoToLocationSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationSettingsScreenOpener.open(getContext());
                callbacks.onNeedToCloseDialog();
            }
        });
    }

    private void initCancelButton() {
        findViewById(R.id.buttonCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callbacks.onNeedToCloseDialog();
            }
        });
    }
}
