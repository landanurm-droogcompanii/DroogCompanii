package ru.droogcompanii.application.ui.activity.personal_account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.google.common.base.Optional;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.personal_details.AccountOwner;
import ru.droogcompanii.application.data.personal_details.BankCard;
import ru.droogcompanii.application.ui.util.able_to_start_task.TaskNotBeInterrupted;
import ru.droogcompanii.application.ui.activity.base_menu_helper.MenuHelper;
import ru.droogcompanii.application.ui.activity.base_menu_helper.MenuHelperItemsProvider;
import ru.droogcompanii.application.ui.activity.base_menu_helper.menu_item_helper.MenuItemHelper;
import ru.droogcompanii.application.ui.activity.base_menu_helper.menu_item_helper.MenuItemHelpers;
import ru.droogcompanii.application.ui.activity.base_menu_helper.menu_item_helper.MenuItemIds;
import ru.droogcompanii.application.ui.activity.signin.AuthenticationToken;
import ru.droogcompanii.application.ui.activity.signin.AuthenticationTokenSaverLoader;
import ru.droogcompanii.application.ui.activity.signin.SignInActivity;
import ru.droogcompanii.application.ui.fragment.bank_card_details.BankCardDetailsFragment;
import ru.droogcompanii.application.ui.fragment.personal_details.OnBankCardSelectedListener;
import ru.droogcompanii.application.ui.fragment.personal_details.PersonalDetailsFragment;
import ru.droogcompanii.application.ui.fragment.signin.SignInFragment;
import ru.droogcompanii.application.ui.util.ActionBarActivityWithUpButton;
import ru.droogcompanii.application.util.Predicate;
import ru.droogcompanii.application.util.Snorlax;

/**
 * Created by ls on 25.02.14.
 */
public class PersonalAccountActivity extends ActionBarActivityWithUpButton implements OnBankCardSelectedListener {

    public static final String KEY_BANK_CARD = "BANK_CARD";

    private static final int REQUEST_CODE_SIGNIN = 141;

    private static final int TASK_REQUEST_CODE_RECEIVE_TOKEN_FROM_DB = 100;
    private static final int TASK_REQUEST_CODE_RECEIVE_DETAILS = 111;
    private static final int TASK_REQUEST_CODE_SIGN_OUT = 122;
    private static final int TASK_REQUEST_CODE_SAVE_TOKEN_TO_DB = 133;

    private static final String KEY_DETAILS = "DETAILS";
    private static final String KEY_TOKEN = "TOKEN";
    private static final String KEY_IS_DETAILS_REQUESTED = "IS_DETAILS_REQUESTED";
    private static final String KEY_IS_SIGN_OUT_ENABLED = "KEY_IS_SIGN_OUT_ENABLED";

    private static final String TAG_PERSONAL_DETAILS_FRAGMENT = "PERSONAL_DETAILS";
    private static final String TAG_BANK_CARD_DETAILS_FRAGMENT = "BANK_CARD_DETAILS";


    private boolean isDetailsRequested;
    private boolean isSignOutEnabled;
    private Optional<AccountOwner> optionalDetails;
    private Optional<AuthenticationToken> optionalToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_personal_details);

        if (savedInstanceState == null) {
            initStateByDefault();
            placePersonalDetailsFragmentOnLayout();
        } else {
            restoreState(savedInstanceState);
        }
    }

    private void initStateByDefault() {
        isDetailsRequested = false;
        isSignOutEnabled = false;
        optionalDetails = Optional.absent();
        optionalToken = Optional.absent();
    }

    private void restoreState(Bundle savedInstanceState) {
        isDetailsRequested = savedInstanceState.getBoolean(KEY_IS_DETAILS_REQUESTED);
        isSignOutEnabled = savedInstanceState.getBoolean(KEY_IS_SIGN_OUT_ENABLED);
        optionalDetails = (Optional<AccountOwner>) savedInstanceState.getSerializable(KEY_DETAILS);
        optionalToken = (Optional<AuthenticationToken>) savedInstanceState.getSerializable(KEY_TOKEN);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveStateInto(outState);
    }

    private void saveStateInto(Bundle outState) {
        outState.putBoolean(KEY_IS_DETAILS_REQUESTED, isDetailsRequested);
        outState.putBoolean(KEY_IS_SIGN_OUT_ENABLED, isSignOutEnabled);
        outState.putSerializable(KEY_DETAILS, optionalDetails);
        outState.putSerializable(KEY_TOKEN, optionalToken);
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
    public void onBankCardSelected(BankCard bankCard) {
        Fragment bankCardDetailsFragment = prepareBankCardDetailsFragment(bankCard);
        replaceCurrentFragmentOn(bankCardDetailsFragment, TAG_BANK_CARD_DETAILS_FRAGMENT);
    }

    private Fragment prepareBankCardDetailsFragment(BankCard bankCard) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_BANK_CARD, (Serializable) bankCard);
        Fragment bankCardDetailsFragment = new BankCardDetailsFragment();
        bankCardDetailsFragment.setArguments(args);
        return bankCardDetailsFragment;
    }

    private void replaceCurrentFragmentOn(Fragment newFragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.containerOfFragment, newFragment, tag);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    public void requestDetails() {
        if (isDetailsRequested) {
            return;
        }
        isDetailsRequested = true;
        if (optionalDetails.isPresent()) {
            onReceiveDetails(optionalDetails.get());
        } else {
            requestToken();
        }
    }

    private void requestToken() {
        if (isInvalidToken(optionalToken)) {
            startTaskReceivingTokenFromDB();
        } else {
            onReceiveTokenWithoutSavingToDatabase(optionalToken.get());
        }
    }

    private static boolean isInvalidToken(Optional<AuthenticationToken> optionalToken) {
        return !isValidToken(optionalToken);
    }

    private static boolean isValidToken(Optional<AuthenticationToken> optionalToken) {
        return optionalToken.isPresent() && optionalToken.get().isValid();
    }

    private void startTaskReceivingTokenFromDB() {
        startTask(TASK_REQUEST_CODE_RECEIVE_TOKEN_FROM_DB, new TaskNotBeInterrupted() {
            @Override
            protected Serializable doInBackground(Void... voids) {
                Snorlax.sleep();

                Context context = PersonalAccountActivity.this;
                AuthenticationTokenSaverLoader saverLoader = new AuthenticationTokenSaverLoader(context);
                return saverLoader.load();
            }
        });
    }

    @Override
    public void startTask(int requestCode, TaskNotBeInterrupted task, Integer titleId) {
        super.startTask(requestCode, task, titleId);

        setEnabledSignOutAction(false);
    }

    @Override
    public void onTaskResult(int requestCode, int resultCode, Serializable result) {
        setEnabledSignOutAction(true);

        if (requestCode == TASK_REQUEST_CODE_RECEIVE_TOKEN_FROM_DB) {
            onReceiveTokenFromDB(resultCode, result);
        } else if (requestCode == TASK_REQUEST_CODE_RECEIVE_DETAILS) {
            onReceiveDetails(resultCode, result);
        } else if (requestCode == TASK_REQUEST_CODE_SIGN_OUT) {
            onSignOutTaskCompleted(resultCode, result);
        } else if (requestCode == TASK_REQUEST_CODE_SAVE_TOKEN_TO_DB) {
            onSavingTokenToDbCompleted(resultCode, result);
        }
    }

    private void onReceiveTokenFromDB(int resultCode, Serializable result) {
        if (resultCode != RESULT_OK) {
            onTaskCancelled();
        } else {
            Optional<AuthenticationToken> optionalToken = (Optional<AuthenticationToken>) result;
            if (isInvalidToken(optionalToken)) {
                startSignInActivity();
            } else {
                onReceiveTokenWithoutSavingToDatabase(optionalToken.get());
            }
        }
    }

    private void onTaskCancelled() {
        finish();
    }

    private void startSignInActivity() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SIGNIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SIGNIN) {
            onReturningFromSignInActivity(resultCode, data);
        }
    }

    private void onReturningFromSignInActivity(int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            onUserLeaveSignInActivity();
        } else {
            onUserSignIn(data);
        }
    }

    private void onUserLeaveSignInActivity() {
        finish();
    }

    private void onUserSignIn(Intent data) {
        AuthenticationToken token = (AuthenticationToken) data.getSerializableExtra(SignInFragment.KEY_TOKEN);
        onReceiveToken(token);
    }

    private void onReceiveToken(AuthenticationToken token) {
        startTaskSavingTokenToDb(token);
    }

    private void startTaskSavingTokenToDb(final AuthenticationToken token) {
        startTask(TASK_REQUEST_CODE_SAVE_TOKEN_TO_DB, new TaskNotBeInterrupted() {
            @Override
            protected Serializable doInBackground(Void... voids) {
                Snorlax.sleep();

                saveTokenToDb(token);
                return token;
            }
        });
    }

    private void saveTokenToDb(AuthenticationToken token) {
        AuthenticationTokenSaverLoader saverLoader = new AuthenticationTokenSaverLoader(this);
        saverLoader.save(token);
    }

    private void onSavingTokenToDbCompleted(int resultCode, Serializable result) {
        if (resultCode != RESULT_OK) {
            onTaskCancelled();
        } else {
            AuthenticationToken token = (AuthenticationToken) result;
            onReceiveTokenWithoutSavingToDatabase(token);
        }
    }

    private void onReceiveTokenWithoutSavingToDatabase(AuthenticationToken token) {
        optionalToken = Optional.of(token);
        requestDetails(token);
    }

    private void requestDetails(AuthenticationToken token) {
        if (isInvalidToken(Optional.fromNullable(token))) {
            throw new IllegalStateException("requestDetails(token): given argument is invalid: " + token);
        } else {
            startTaskReceivingDetails(token);
        }
    }

    private void startTaskReceivingDetails(final AuthenticationToken token) {
        final PersonalDetailsRequester requester =
                new PersonalDetailsRequesterFromInetAndDatabase(this);
        startTask(TASK_REQUEST_CODE_RECEIVE_DETAILS, new TaskNotBeInterrupted() {
            @Override
            protected Serializable doInBackground(Void... voids) {
                Snorlax.sleep();

                return requester.requestDetails(token);
            }
        });
    }

    private void onReceiveDetails(int resultCode, Serializable result) {
        if (resultCode != RESULT_OK) {
            onTaskCancelled();
            return;
        }
        optionalDetails = (Optional<AccountOwner>) result;
        if (optionalDetails.isPresent()) {
            onReceiveDetails(optionalDetails.get());
        } else {
            onRequestedDetailsCannotBeReceived();
        }
    }

    private void onRequestedDetailsCannotBeReceived() {
        // TODO
        throw new IllegalStateException("onReceiveDetails(RESULT_OK, result): received details is absent");
    }

    private void onReceiveDetails(AccountOwner accountOwner) {
        DetailsReceiver detailsReceiver = (DetailsReceiver) getFragmentDisplayedAtTheMoment();
        detailsReceiver.onReceiveDetails(accountOwner);
        isDetailsRequested = false;
    }

    private Fragment getFragmentDisplayedAtTheMoment() {
        final String[] tags = { TAG_BANK_CARD_DETAILS_FRAGMENT, TAG_PERSONAL_DETAILS_FRAGMENT };
        FragmentManager fragmentManager = getSupportFragmentManager();
        for (String tag : tags) {
            Fragment fragment = fragmentManager.findFragmentByTag(tag);
            if (fragment != null) {
                return fragment;
            }
        }
        throw new IllegalStateException("Currently there is no displayed fragment");
    }

    @Override
    protected MenuHelper getMenuHelper() {
        MenuItemHelper.Action signOutAction = new MenuItemHelper.Action() {
            @Override
            public void run(Activity activity) {
                onSignOutAction();
            }
        };
        Predicate signOutActionIsEnable = new Predicate() {
            @Override
            public boolean isTrue() {
                return isSignOutEnabled;
            }
        };
        final MenuItemHelper signOutItemHelper = MenuItemHelpers.SIGNOUT
                .withAction(signOutAction).withPredicateEnable(signOutActionIsEnable);
        return new MenuHelperItemsProvider(this) {
            @Override
            protected MenuItemHelper[] getMenuItemHelpers() {
                return new MenuItemHelper[] {
                        signOutItemHelper
                };
            }
        };
    }

    private void onSignOutAction() {
        startSignOutTask();
    }

    private void setEnabledSignOutAction(boolean enabled) {
        isSignOutEnabled = enabled;

        updateSignOutActionState();
    }

    private void updateSignOutActionState() {
        Menu menu = getMenu();
        if (menu != null) {
            MenuItem signOutAction = menu.findItem(MenuItemIds.SIGNOUT);
            signOutAction.setEnabled(!isRunningTask());
        }
    }

    private void startSignOutTask() {
        if (isInvalidToken(optionalToken)) {
            onTryingSignOutWhenCurrentTokenAlreadyIsInvalid();
            return;
        }

        TaskNotBeInterrupted signOutTask = new TaskNotBeInterrupted() {
            @Override
            protected Serializable doInBackground(Void... voids) {
                Snorlax.sleep();

                signOut();
                return null;
            }
        };

        startTask(TASK_REQUEST_CODE_SIGN_OUT, signOutTask, R.string.sign_out_in_progress);
    }

    private void signOut() {
        Optional<AuthenticationToken> tokenToInvalidate = optionalToken;
        optionalToken = Optional.absent();
        AuthenticationTokenInvalidator tokenInvalidator = new AuthenticationTokenInvalidator(this);
        tokenInvalidator.invalidate(tokenToInvalidate);
    }

    private void onTryingSignOutWhenCurrentTokenAlreadyIsInvalid() {
        throw new IllegalStateException("Cannot perform Sign Out because current token already is invalid");
    }

    private void onSignOutTaskCompleted(int resultCode, Serializable result) {
        if (resultCode != RESULT_OK) {
            onSignOutCancelled();
        } else {
            onSignOutCompletedSuccessfully();
        }
    }

    private void onSignOutCancelled() {
        if (signOutWasPerformed()) {
            onSignOutCompletedSuccessfully();
        }
    }

    private boolean signOutWasPerformed() {
        return isInvalidToken(optionalToken);
    }

    private void onSignOutCompletedSuccessfully() {
        invalidateSession();
        removeBankCardDetailsFragmentIfItIsDisplaying();
        recreatePersonalDetailsFragment();
    }

    private void invalidateSession() {
        optionalDetails = Optional.absent();
        optionalToken = Optional.absent();
    }

    private void removeBankCardDetailsFragmentIfItIsDisplaying() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(TAG_BANK_CARD_DETAILS_FRAGMENT);
        if (fragment != null) {
            fragmentManager.popBackStackImmediate();
        }
    }

    private void recreatePersonalDetailsFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new PersonalDetailsFragment();
        transaction.replace(R.id.containerOfFragment, fragment, TAG_PERSONAL_DETAILS_FRAGMENT);
        transaction.commitAllowingStateLoss();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        updateSignOutActionState();
        return result;
    }

}
