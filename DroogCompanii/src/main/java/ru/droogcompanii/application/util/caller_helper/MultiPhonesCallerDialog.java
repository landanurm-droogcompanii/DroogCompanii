package ru.droogcompanii.application.util.caller_helper;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;

import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.helpers.Caller;
import ru.droogcompanii.application.util.CopierToClipboard;

/**
 * Created by ls on 03.02.14.
 */
public class MultiPhonesCallerDialog extends Dialog implements PhonesListViewMaker.OnNeedToCallListener {

    private static final String TAG_CONTENT_VIEW = "Content View";
    private static final int MENU_ITEM_ID_COPY_TO_CLIPBOARD = 12;

    private final List<String> phones;


    public MultiPhonesCallerDialog(Context context, List<String> phones) {
        super(context);
        this.phones = phones;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mustBeCalledBefore_SetContentView();

        View contentView = prepareContentView();
        contentView.setTag(TAG_CONTENT_VIEW);

        setContentView(contentView);

        registerForContextMenu(contentView);
    }

    private void mustBeCalledBefore_SetContentView() {
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
    }

    private View prepareContentView() {
        return PhonesListViewMaker.make(getContext(), phones, this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        if (!isContentView(view)) {
            return;
        }
        final String phone = phones.get(getPositionFrom(menuInfo));
        MenuItem item = menu.add(Menu.NONE, MENU_ITEM_ID_COPY_TO_CLIPBOARD, 0, R.string.copyToClipboard);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                onNeedToCopyNumberToClipboard(phone);
                return true;
            }
        });
    }

    private void onNeedToCopyNumberToClipboard(String phone) {
        CopierToClipboard copierToClipboard = new CopierToClipboard(getContext());
        copierToClipboard.copyToClipboard(phone);
    }

    private static boolean isContentView(View view) {
        return TAG_CONTENT_VIEW.equals(view.getTag());
    }

    private static int getPositionFrom(ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        return info.position;
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
