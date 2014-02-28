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
import ru.droogcompanii.application.data.personal_details.PersonalDetails;
import ru.droogcompanii.application.data.personal_details.bank_card.BankCard;
import ru.droogcompanii.application.ui.activity.able_to_start_task.ActivityAbleToStartTask;
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
import ru.droogcompanii.application.ui.helpers.task.TaskNotBeInterrupted;
import ru.droogcompanii.application.util.LogUtils;
import ru.droogcompanii.application.util.Predicate;

/**
 * Created by ls on 25.02.14.
 */
public class PersonalAccountActivity extends ActivityAbleToStartTask implements OnBankCardSelectedListener {

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
    private Optional<PersonalDetails> optionalDetails;
    private Optional<AuthenticationToken> optionalToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logCurrentMethodName();

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
        logCurrentMethodName();

        isDetailsRequested = false;
        isSignOutEnabled = false;
        optionalDetails = Optional.absent();
        optionalToken = Optional.absent();
    }

    private static void logCurrentMethodName(Object... additional) {
        final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements.length > 0) {
            String currentMethodName = stackTraceElements[3].getMethodName();
            String className = PersonalAccountActivity.class.getSimpleName();
            LogUtils.debug(className + "." + currentMethodName + "()" + combine(additional));
        }
    }

    private static String combine(Object[] toCombine) {
        if (toCombine.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(": ");
        for (Object each : toCombine) {
            builder.append(each);
        }
        return builder.toString();
    }

    private void restoreState(Bundle savedInstanceState) {
        logCurrentMethodName();

        isDetailsRequested = savedInstanceState.getBoolean(KEY_IS_DETAILS_REQUESTED);
        isSignOutEnabled = savedInstanceState.getBoolean(KEY_IS_SIGN_OUT_ENABLED);
        optionalDetails = (Optional<PersonalDetails>) savedInstanceState.getSerializable(KEY_DETAILS);
        optionalToken = (Optional<AuthenticationToken>) savedInstanceState.getSerializable(KEY_TOKEN);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        logCurrentMethodName();

        super.onSaveInstanceState(outState);
        saveStateInto(outState);
    }

    private void saveStateInto(Bundle outState) {
        logCurrentMethodName();

        outState.putBoolean(KEY_IS_DETAILS_REQUESTED, isDetailsRequested);
        outState.putBoolean(KEY_IS_SIGN_OUT_ENABLED, isSignOutEnabled);
        outState.putSerializable(KEY_DETAILS, optionalDetails);
        outState.putSerializable(KEY_TOKEN, optionalToken);
    }

    private void placePersonalDetailsFragmentOnLayout() {
        logCurrentMethodName();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        PersonalDetailsFragment fragment = new PersonalDetailsFragment();
        transaction.add(R.id.containerOfFragment, fragment, TAG_PERSONAL_DETAILS_FRAGMENT);
        transaction.commit();
    }

    public void setDefaultTitle() {
        logCurrentMethodName();

        setTitle(R.string.titleOfPersonalAccountActivity);
    }

    @Override
    public void onBankCardSelected(BankCard bankCard) {
        logCurrentMethodName();

        Fragment bankCardDetailsFragment = prepareBankCardDetailsFragment(bankCard);
        replaceCurrentFragmentOn(bankCardDetailsFragment, TAG_BANK_CARD_DETAILS_FRAGMENT);
    }

    private Fragment prepareBankCardDetailsFragment(BankCard bankCard) {
        logCurrentMethodName();

        Bundle args = new Bundle();
        args.putSerializable(KEY_BANK_CARD, (Serializable) bankCard);
        Fragment bankCardDetailsFragment = new BankCardDetailsFragment();
        bankCardDetailsFragment.setArguments(args);
        return bankCardDetailsFragment;
    }

    private void replaceCurrentFragmentOn(Fragment newFragment, String tag) {
        logCurrentMethodName();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.containerOfFragment, newFragment, tag);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    public void requestDetails() {
        logCurrentMethodName();

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
        logCurrentMethodName();

        if (isInvalidToken(optionalToken)) {
            startTaskReceivingTokenFromDB();
        } else {
            onReceiveTokenWithoutSavingToDatabase(optionalToken.get());
        }
    }

    private static boolean isInvalidToken(Optional<AuthenticationToken> optionalToken) {
        logCurrentMethodName();

        return !isValidToken(optionalToken);
    }

    private static boolean isValidToken(Optional<AuthenticationToken> optionalToken) {
        logCurrentMethodName();

        return optionalToken.isPresent() && optionalToken.get().isValid();
    }

    private void startTaskReceivingTokenFromDB() {
        logCurrentMethodName();

        startTask(TASK_REQUEST_CODE_RECEIVE_TOKEN_FROM_DB, new TaskNotBeInterrupted() {
            @Override
            protected Serializable doInBackground(Void... voids) {
                Context context = PersonalAccountActivity.this;
                AuthenticationTokenSaverLoader saverLoader = new AuthenticationTokenSaverLoader(context);
                return saverLoader.load();
            }
        });
    }

    @Override
    protected void onReceiveResult(int requestCode, int resultCode, Serializable result) {
        logCurrentMethodName();

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
        logCurrentMethodName();

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
        logCurrentMethodName();

        finish();
    }

    private void startSignInActivity() {
        logCurrentMethodName();

        Intent intent = new Intent(this, SignInActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SIGNIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        logCurrentMethodName();

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SIGNIN) {
            onReturningFromSignInActivity(resultCode, data);
        }
    }

    private void onReturningFromSignInActivity(int resultCode, Intent data) {
        logCurrentMethodName();

        if (resultCode != RESULT_OK) {
            onUserLeaveSignInActivity();
        } else {
            onUserSignIn(data);
        }
    }

    private void onUserLeaveSignInActivity() {
        logCurrentMethodName();

        finish();
    }

    private void onUserSignIn(Intent data) {
        logCurrentMethodName();

        AuthenticationToken token = (AuthenticationToken) data.getSerializableExtra(SignInFragment.KEY_TOKEN);
        onReceiveToken(token);
    }

    private void onReceiveToken(AuthenticationToken token) {
        logCurrentMethodName();

        startTaskSavingTokenToDb(token);
    }

    private void startTaskSavingTokenToDb(final AuthenticationToken token) {
        logCurrentMethodName();

        startTask(TASK_REQUEST_CODE_SAVE_TOKEN_TO_DB, new TaskNotBeInterrupted() {
            @Override
            protected Serializable doInBackground(Void... voids) {
                saveTokenToDb(token);
                return token;
            }
        });
    }

    private void saveTokenToDb(AuthenticationToken token) {
        logCurrentMethodName();

        AuthenticationTokenSaverLoader saverLoader = new AuthenticationTokenSaverLoader(this);
        saverLoader.save(token);
    }

    private void onSavingTokenToDbCompleted(int resultCode, Serializable result) {
        logCurrentMethodName();

        if (resultCode != RESULT_OK) {
            onTaskCancelled();
        } else {
            AuthenticationToken token = (AuthenticationToken) result;
            onReceiveTokenWithoutSavingToDatabase(token);
        }
    }

    private void onReceiveTokenWithoutSavingToDatabase(AuthenticationToken token) {
        logCurrentMethodName();

        optionalToken = Optional.of(token);
        requestDetails(token);
    }

    private void requestDetails(AuthenticationToken token) {
        logCurrentMethodName();

        setEnabledSignOutAction(true);

        if (isInvalidToken(Optional.fromNullable(token))) {
            throw new IllegalStateException("requestDetails(token): given argument is invalid: " + token);
        } else {
            startTaskReceivingDetails(token);
        }
    }

    private void startTaskReceivingDetails(final AuthenticationToken token) {
        logCurrentMethodName();

        final PersonalDetailsRequester requester =
                new PersonalDetailsRequesterFromInetAndDatabase(this);
        startTask(TASK_REQUEST_CODE_RECEIVE_DETAILS, new TaskNotBeInterrupted() {
            @Override
            protected Serializable doInBackground(Void... voids) {
                return requester.requestDetails(token);
            }
        });
    }

    private void onReceiveDetails(int resultCode, Serializable result) {
        logCurrentMethodName();

        if (resultCode != RESULT_OK) {
            onTaskCancelled();
            return;
        }
        optionalDetails = (Optional<PersonalDetails>) result;
        if (optionalDetails.isPresent()) {
            onReceiveDetails(optionalDetails.get());
        } else {
            onRequestedDetailsCannotBeReceived();
        }
    }

    private void onRequestedDetailsCannotBeReceived() {
        logCurrentMethodName();

        // TODO
        throw new IllegalStateException("onReceiveDetails(RESULT_OK, result): received details is absent");
    }

    private void onReceiveDetails(PersonalDetails personalDetails) {
        logCurrentMethodName();

        DetailsReceiver detailsReceiver = (DetailsReceiver) getFragmentDisplayedAtTheMoment();
        detailsReceiver.onReceiveDetails(personalDetails);
        isDetailsRequested = false;
    }

    private Fragment getFragmentDisplayedAtTheMoment() {
        logCurrentMethodName();

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
        logCurrentMethodName();

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
        logCurrentMethodName();

        if (isRunningTask()) {
            LogUtils.debug("Activity has running task");
            return;
        }

        startSignOutTask();
    }

    private void setEnabledSignOutAction(boolean enabled) {
        logCurrentMethodName();

        isSignOutEnabled = enabled;

        updateSignOutActionState();
    }

    private void updateSignOutActionState() {
        Menu menu = getMenu();
        if (menu != null) {
            MenuItem signOutAction = menu.findItem(MenuItemIds.SIGNOUT);
            signOutAction.setEnabled(isSignOutEnabled);
        }
    }

    private void startSignOutTask() {
        logCurrentMethodName();

        if (isInvalidToken(optionalToken)) {
            onTryingSignOutWhenCurrentTokenAlreadyIsInvalid();
            return;
        }

        setEnabledSignOutAction(false);

        TaskNotBeInterrupted signOutTask = new TaskNotBeInterrupted() {
            @Override
            protected Serializable doInBackground(Void... voids) {
                signOut();
                return null;
            }
        };
        startTask(TASK_REQUEST_CODE_SIGN_OUT, signOutTask, R.string.sign_out_in_progress);
    }

    private void signOut() {
        logCurrentMethodName();

        Optional<AuthenticationToken> tokenToInvalidate = optionalToken;
        optionalToken = Optional.absent();
        AuthenticationTokenInvalidator tokenInvalidator = new AuthenticationTokenInvalidator(this);
        tokenInvalidator.invalidate(tokenToInvalidate);
    }

    private void onTryingSignOutWhenCurrentTokenAlreadyIsInvalid() {
        logCurrentMethodName();

        throw new IllegalStateException("Cannot perform Sign Out because current token already is invalid");
    }

    private void onSignOutTaskCompleted(int resultCode, Serializable result) {
        logCurrentMethodName();

        if (resultCode != RESULT_OK) {
            onSignOutCancelled();
        } else {
            onSignOutCompletedSuccessfully();
        }
    }

    private void onSignOutCancelled() {
        logCurrentMethodName();

        if (signOutWasPerformed()) {
            onSignOutCompletedSuccessfully();
        } else {
            setEnabledSignOutAction(true);
        }
    }

    private boolean signOutWasPerformed() {
        logCurrentMethodName();

        return isInvalidToken(optionalToken);
    }

    private void onSignOutCompletedSuccessfully() {
        logCurrentMethodName();

        invalidateSession();
        removeBankCardDetailsFragmentIfItIsDisplaying();
        recreatePersonalDetailsFragment();

        setEnabledSignOutAction(false);
    }

    private void invalidateSession() {
        optionalDetails = Optional.absent();
        optionalToken = Optional.absent();
    }

    private void removeBankCardDetailsFragmentIfItIsDisplaying() {
        logCurrentMethodName();

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(TAG_BANK_CARD_DETAILS_FRAGMENT);
        if (fragment != null) {
            fragmentManager.popBackStackImmediate();
        }
    }

    private void recreatePersonalDetailsFragment() {
        logCurrentMethodName();

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
