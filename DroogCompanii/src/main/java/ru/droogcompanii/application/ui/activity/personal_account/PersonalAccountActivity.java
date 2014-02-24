package ru.droogcompanii.application.ui.activity.personal_account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.activity.able_to_start_task.ActivityAbleToStartTask;
import ru.droogcompanii.application.ui.activity.able_to_start_task.TaskResultReceiver;
import ru.droogcompanii.application.ui.activity.base_menu_helper.MenuHelper;
import ru.droogcompanii.application.ui.activity.base_menu_helper.MenuHelperItemsProvider;
import ru.droogcompanii.application.ui.activity.base_menu_helper.menu_item_helper.MenuItemHelper;
import ru.droogcompanii.application.ui.activity.base_menu_helper.menu_item_helper.MenuItemHelpers;
import ru.droogcompanii.application.ui.activity.login.AuthenticationToken;
import ru.droogcompanii.application.ui.activity.login.LoginActivity;
import ru.droogcompanii.application.ui.fragment.login.LoginFragment;
import ru.droogcompanii.application.ui.fragment.personal_details.PersonalDetailsFragment;

/**
 * Created by ls on 31.01.14.
 */
public class PersonalAccountActivity extends ActivityAbleToStartTask {

    private static final int REQUEST_CODE_LOGIN = 141;

    public static interface Callbacks extends TaskResultReceiver {
        void onTokenReceived(AuthenticationToken token);
    }

    private static final String TAG_FRAGMENT = "TAG_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        if (savedInstanceState == null) {
            placePersonalDetailsFragmentOnLayout();
        }
    }

    private void placePersonalDetailsFragmentOnLayout() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        PersonalDetailsFragment fragment = new PersonalDetailsFragment();
        transaction.add(R.id.containerOfFragment, fragment, TAG_FRAGMENT);
        transaction.commit();
    }

    @Override
    protected String getTagOfTaskResultReceiverFragment() {
        return TAG_FRAGMENT;
    }

    public void requestToken() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, REQUEST_CODE_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOGIN) {
            onReturningFromLoginActivity(resultCode, data);
        }
    }

    private void onReturningFromLoginActivity(int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }
        AuthenticationToken token = (AuthenticationToken) data.getSerializableExtra(LoginFragment.KEY_TOKEN);
        getCallbacks().onTokenReceived(token);
    }

    private Callbacks getCallbacks() {
        return (Callbacks) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
    }

    @Override
    protected MenuHelper getMenuHelper() {
        final MenuItemHelper logout = MenuItemHelpers.LOGOUT.withAction(new MenuItemHelper.Action() {
            @Override
            public void run(Activity activity) {
                getPersonalDetailsFragment().onLogOut();
            }
        });
        return new MenuHelperItemsProvider(this) {
            @Override
            protected MenuItemHelper[] getMenuItemHelpers() {
                return new MenuItemHelper[] {
                        logout
                };
            }
        };
    }

    private PersonalDetailsFragment getPersonalDetailsFragment() {
        return (PersonalDetailsFragment) findFragment();
    }

    private Fragment findFragment() {
        return getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
    }
}
