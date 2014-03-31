package ru.droogcompanii.application.util.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.util.constants.ApiVersionUtils;

/**
 * Created by ls on 25.03.14.
 */
public class RadioGroupMaker {
    private static final int WRAP_CONTENT = RadioGroup.LayoutParams.WRAP_CONTENT;
    private static final RadioGroup.LayoutParams LAYOUT_PARAMS =
            new RadioGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 1.0f);

    public static interface RadioButtonsMaker {
        List<RadioButton> make(Context context);
    }

    private final Context context;

    public RadioGroupMaker(Context context) {
        this.context = context;
    }

    public RadioGroup make(int radioGroupLayoutId, RadioButtonsMaker radioButtonsMaker) {
        List<RadioButton> radioButtons = radioButtonsMaker.make(context);
        return prepareRadioGroup(radioGroupLayoutId, radioButtons);
    }

    private RadioGroup prepareRadioGroup(int radioGroupLayoutId, List<RadioButton> radioButtons) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        RadioGroup radioGroup = (RadioGroup) layoutInflater.inflate(radioGroupLayoutId, null);
        radioGroup.setWeightSum(radioButtons.size());
        setSpaceBetweenRadioButtons(radioGroup);
        for (RadioButton each : radioButtons) {
            radioGroup.addView(each, LAYOUT_PARAMS);
        }
        return radioGroup;
    }

    @SuppressLint("NewApi")
    private static void setSpaceBetweenRadioButtons(RadioGroup radioGroup) {
        if (ApiVersionUtils.isCurrentVersionLowerThan(14)) {
            return;
        }
        radioGroup.setShowDividers(RadioGroup.SHOW_DIVIDER_MIDDLE);
        Resources res = radioGroup.getResources();
        int dividerPadding = res.getDimensionPixelSize(R.dimen.dividerPaddingOfRadioGroup);
        radioGroup.setDividerPadding(dividerPadding);
    }


}
