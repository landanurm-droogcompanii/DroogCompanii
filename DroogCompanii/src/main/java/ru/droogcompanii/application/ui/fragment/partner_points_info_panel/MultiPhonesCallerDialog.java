package ru.droogcompanii.application.ui.fragment.partner_points_info_panel;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.helpers.Caller;

/**
 * Created by ls on 03.02.14.
 */
public class MultiPhonesCallerDialog extends Dialog implements PhonesListViewMaker.OnNeedToCallListener {

    private final List<String> phones;


    public MultiPhonesCallerDialog(Context context, List<String> phones) {
        super(context);
        this.phones = phones;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mustBeCalledBefore_SetContentView();

        setContentView(prepareContentView());
    }

    private void mustBeCalledBefore_SetContentView() {
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
    }

    private View prepareContentView() {
        return PhonesListViewMaker.make(getContext(), phones, this);
    }

    @Override
    public void show() {
        super.show();
        mustBeCalledAfter_Show();
    }

    private void mustBeCalledAfter_Show() {
        setTitleIcon(R.drawable.ic_caller);
    }

    private void setTitleIcon(int iconId) {
        setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, iconId);
    }

    @Override
    public void onNeedToCall(String phone) {
        Caller.call(getContext(), phone);
        dismiss();
    }
}
