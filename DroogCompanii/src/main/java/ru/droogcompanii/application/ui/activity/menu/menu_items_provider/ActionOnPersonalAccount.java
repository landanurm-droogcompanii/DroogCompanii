package ru.droogcompanii.application.ui.activity.menu.menu_items_provider;

import android.content.Context;
import android.content.Intent;

import ru.droogcompanii.application.ui.activity.menu.MenuListItem;
import ru.droogcompanii.application.ui.activity.personal_account.PersonalAccountActivity;

/**
 * Created by ls on 31.01.14.
 */
class ActionOnPersonalAccount implements MenuListItem.Action {
    @Override
    public void launch(Context context) {
        Intent intent = new Intent(context, PersonalAccountActivity.class);
        context.startActivity(intent);
    }
}
