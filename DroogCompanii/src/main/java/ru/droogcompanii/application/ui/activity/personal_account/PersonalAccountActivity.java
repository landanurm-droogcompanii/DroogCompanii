package ru.droogcompanii.application.ui.activity.personal_account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.personal_details.bank_card.BankCard;
import ru.droogcompanii.application.ui.activity.able_to_start_task.ActivityAbleToStartTask;
import ru.droogcompanii.application.ui.activity.able_to_start_task.TaskResultReceiver;
import ru.droogcompanii.application.ui.activity.base_menu_helper.MenuHelper;
import ru.droogcompanii.application.ui.activity.base_menu_helper.MenuHelperItemsProvider;
import ru.droogcompanii.application.ui.activity.base_menu_helper.menu_item_helper.MenuItemHelper;
import ru.droogcompanii.application.ui.activity.base_menu_helper.menu_item_helper.MenuItemHelpers;
import ru.droogcompanii.application.ui.activity.signin.AuthenticationToken;
import ru.droogcompanii.application.ui.activity.signin.SignInActivity;
import ru.droogcompanii.application.ui.fragment.bank_card_details.BankCardDetailsFragment;
import ru.droogcompanii.application.ui.fragment.personal_details.OnBankCardSelectedListener;
import ru.droogcompanii.application.ui.fragment.personal_details.PersonalDetailsFragment;
import ru.droogcompanii.application.ui.fragment.signin.SignInFragment;
import ru.droogcompanii.application.util.LogUtils;

/**
 * Created by ls on 31.01.14.
 */
public class PersonalAccountActivity extends ActivityAbleToStartTask implements OnBankCardSelectedListener {

    public static final String KEY_BANK_CARD = "BANK_CARD";


    private static final int REQUEST_CODE_LOGIN = 141;

    public static interface Callbacks extends TaskResultReceiver {
        void onTokenReceived(AuthenticationToken token);
    }

    private static final String TAG_PERSONAL_DETAILS_FRAGMENT = "PERSONAL_DETAILS";
    private static final String TAG_BANK_CARD_DETAILS_FRAGMENT = "BANK_CARD_DETAILS";

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
        transaction.add(R.id.containerOfFragment, fragment, TAG_PERSONAL_DETAILS_FRAGMENT);
        transaction.commit();
    }

    public void setDefaultTitle() {
        setTitle(R.string.titleOfPersonalAccountActivity);
    }

    @Override
    protected String getTagOfTaskResultReceiverFragment() {
        return TAG_PERSONAL_DETAILS_FRAGMENT;
    }

    public void requestToken() {
        Intent intent = new Intent(this, SignInActivity.class);
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
        AuthenticationToken token = (AuthenticationToken) data.getSerializableExtra(SignInFragment.KEY_TOKEN);
        getCallbacks().onTokenReceived(token);
    }

    private Callbacks getCallbacks() {
        return (Callbacks) getSupportFragmentManager().findFragmentByTag(TAG_PERSONAL_DETAILS_FRAGMENT);
    }

    @Override
    protected MenuHelper getMenuHelper() {
        final MenuItemHelper signOutItemHelper = MenuItemHelpers.SIGNOUT.withAction(new MenuItemHelper.Action() {
            @Override
            public void run(Activity activity) {
                onSignOut();
            }
        });
        return new MenuHelperItemsProvider(this) {
            @Override
            protected MenuItemHelper[] getMenuItemHelpers() {
                return new MenuItemHelper[] {
                        signOutItemHelper
                };
            }
        };
    }

    private void onSignOut() {
        LogUtils.debug("onSignOut().begin:");
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(TAG_BANK_CARD_DETAILS_FRAGMENT);
        if (fragment != null) {
            LogUtils.debug("fragment != null");
            getSupportFragmentManager().popBackStackImmediate();
        }
        PersonalDetailsFragment personalDetailsFragment =
                (PersonalDetailsFragment) fragmentManager.findFragmentByTag(TAG_PERSONAL_DETAILS_FRAGMENT);
        personalDetailsFragment.onSignOut();
        LogUtils.debug("onSignOut().end.");
    }

    @Override
    public void onBankCardSelected(BankCard bankCard) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_BANK_CARD, (Serializable) bankCard);
        BankCardDetailsFragment bankCardDetailsFragment = new BankCardDetailsFragment();
        bankCardDetailsFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.containerOfFragment, bankCardDetailsFragment, TAG_BANK_CARD_DETAILS_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
