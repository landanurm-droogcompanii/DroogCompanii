package ru.droogcompanii.application.ui.screens.personal_account;

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

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.personal_details.AccountOwner;
import ru.droogcompanii.application.data.personal_details.BankCard;
import ru.droogcompanii.application.ui.screens.personal_account.bank_card_details.BankCardDetailsFragment;
import ru.droogcompanii.application.ui.screens.personal_account.personal_details.OnBankCardSelectedListener;
import ru.droogcompanii.application.ui.screens.personal_account.personal_details.PersonalDetailsFragment;
import ru.droogcompanii.application.ui.screens.personal_account.signin.AuthenticationToken;
import ru.droogcompanii.application.ui.screens.personal_account.signin.AuthenticationTokenSaverLoader;
import ru.droogcompanii.application.ui.screens.personal_account.signin.SignInActivity;
import ru.droogcompanii.application.ui.screens.personal_account.signin.SignInFragment;
import ru.droogcompanii.application.util.Predicate;
import ru.droogcompanii.application.util.StateManager;
import ru.droogcompanii.application.util.ui.able_to_start_task.TaskNotBeInterruptedDuringConfigurationChange;
import ru.droogcompanii.application.util.ui.activity.ActionBarActivityWithUpButton;
import ru.droogcompanii.application.util.ui.activity.ReuseAlreadyLaunchedActivityFlag;
import ru.droogcompanii.application.util.ui.activity.menu_helper.MenuHelper;
import ru.droogcompanii.application.util.ui.activity.menu_helper.MenuHelperItemsProvider;
import ru.droogcompanii.application.util.ui.activity.menu_helper.menu_item_helper.MenuItemHelper;
import ru.droogcompanii.application.util.ui.activity.menu_helper.menu_item_helper.MenuItemHelpers;
import ru.droogcompanii.application.util.ui.activity.menu_helper.menu_item_helper.MenuItemIds;

/**
 * Created by ls on 25.02.14.
 */
public class PersonalAccountActivity extends ActionBarActivityWithUpButton implements OnBankCardSelectedListener {

    private static class RequestCode {
        public static final int SIGNIN = 141;
    }

    private static class TaskRequestCode {
        public static final int RECEIVE_TOKEN_FROM_DB = 100;
        public static final int RECEIVE_DETAILS = RECEIVE_TOKEN_FROM_DB + 1;
        public static final int SIGN_OUT = RECEIVE_DETAILS + 1;
        public static final int SAVE_TOKEN_TO_DB = SIGN_OUT + 1;
    }

    private static class Key {
        public static final String DETAILS = "DETAILS";
        public static final String TOKEN = "TOKEN";
        public static final String IS_DETAILS_REQUESTED = "IS_DETAILS_REQUESTED";
        public static final String IS_SIGN_OUT_ENABLED = "IS_SIGN_OUT_ENABLED";
    }

    private static class Tag {
        public static final String PERSONAL_DETAILS_FRAGMENT = "PERSONAL_DETAILS";
        public static final String BANK_CARD_DETAILS_FRAGMENT = "BANK_CARD_DETAILS";
    }

    private boolean isDetailsRequested;
    private boolean isSignOutEnabled;
    private Optional<AccountOwner> optionalDetails;
    private Optional<AuthenticationToken> optionalToken;


    private final StateManager STATE_MANAGER = new StateManager() {
        @Override
        public void initStateByDefault() {
            isDetailsRequested = false;
            isSignOutEnabled = false;
            optionalDetails = Optional.absent();
            optionalToken = Optional.absent();

            placePersonalDetailsFragmentOnLayout();
        }


        @Override
        public void restoreState(Bundle savedInstanceState) {
            isDetailsRequested = savedInstanceState.getBoolean(Key.IS_DETAILS_REQUESTED);
            isSignOutEnabled = savedInstanceState.getBoolean(Key.IS_SIGN_OUT_ENABLED);
            optionalDetails = (Optional<AccountOwner>) savedInstanceState.getSerializable(Key.DETAILS);
            optionalToken = (Optional<AuthenticationToken>) savedInstanceState.getSerializable(Key.TOKEN);
        }

        @Override
        public void saveState(Bundle outState) {
            outState.putBoolean(Key.IS_DETAILS_REQUESTED, isDetailsRequested);
            outState.putBoolean(Key.IS_SIGN_OUT_ENABLED, isSignOutEnabled);
            outState.putSerializable(Key.DETAILS, optionalDetails);
            outState.putSerializable(Key.TOKEN, optionalToken);
        }
    };


    public static void start(Context context) {
        Intent intent = new Intent(context, PersonalAccountActivity.class);
        ReuseAlreadyLaunchedActivityFlag.set(intent);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);
        STATE_MANAGER.initState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        STATE_MANAGER.saveState(outState);
    }

    private void placePersonalDetailsFragmentOnLayout() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        PersonalDetailsFragment fragment = new PersonalDetailsFragment();
        transaction.add(R.id.containerOfFragment, fragment, Tag.PERSONAL_DETAILS_FRAGMENT);
        transaction.commit();
    }

    public void setDefaultTitle() {
        setTitle(R.string.titleOfPersonalAccountActivity);
    }

    @Override
    public void onBankCardSelected(BankCard bankCard) {
        Fragment bankCardDetailsFragment = BankCardDetailsFragment.newInstance(bankCard);
        replaceCurrentFragmentOn(bankCardDetailsFragment, Tag.BANK_CARD_DETAILS_FRAGMENT);
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
        startTask(TaskRequestCode.RECEIVE_TOKEN_FROM_DB, new TaskNotBeInterruptedDuringConfigurationChange() {
            @Override
            protected Serializable doInBackground(Void... voids) {
                Context appContext = DroogCompaniiApplication.getContext();
                AuthenticationTokenSaverLoader saverLoader = new AuthenticationTokenSaverLoader(appContext);
                return saverLoader.load();
            }
        });
    }

    @Override
    public void startTask(int requestCode, TaskNotBeInterruptedDuringConfigurationChange task, Integer titleId) {
        super.startTask(requestCode, task, titleId);

        disableSignOut();
    }

    @Override
    public void onTaskResult(int requestCode, int resultCode, Serializable result) {
        enableSignOut();

        if (requestCode == TaskRequestCode.RECEIVE_TOKEN_FROM_DB) {
            onReceiveTokenFromDB(resultCode, result);
        } else if (requestCode == TaskRequestCode.RECEIVE_DETAILS) {
            onReceiveDetails(resultCode, result);
        } else if (requestCode == TaskRequestCode.SIGN_OUT) {
            onSignOutTaskCompleted(resultCode, result);
        } else if (requestCode == TaskRequestCode.SAVE_TOKEN_TO_DB) {
            onSavingTokenToDbCompleted(resultCode, result);
        }
    }

    private void onReceiveTokenFromDB(int resultCode, Serializable result) {
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }
        Optional<AuthenticationToken> optionalToken = (Optional<AuthenticationToken>) result;
        if (isInvalidToken(optionalToken)) {
            startSignInActivity();
        } else {
            onReceiveTokenWithoutSavingToDatabase(optionalToken.get());
        }
    }

    private void startSignInActivity() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivityForResult(intent, RequestCode.SIGNIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCode.SIGNIN) {
            onReturningFromSignInActivity(resultCode, data);
        }
    }

    private void onReturningFromSignInActivity(int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }
        AuthenticationToken token = (AuthenticationToken)
                data.getSerializableExtra(SignInFragment.KEY_TOKEN);
        startTaskSavingTokenToDb(token);
    }

    private void startTaskSavingTokenToDb(final AuthenticationToken token) {
        startTask(TaskRequestCode.SAVE_TOKEN_TO_DB, new TaskNotBeInterruptedDuringConfigurationChange() {
            @Override
            protected Serializable doInBackground(Void... voids) {
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
            finish();
            return;
        }
        AuthenticationToken token = (AuthenticationToken) result;
        onReceiveTokenWithoutSavingToDatabase(token);
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
        startTask(TaskRequestCode.RECEIVE_DETAILS, new TaskNotBeInterruptedDuringConfigurationChange() {
            @Override
            protected Serializable doInBackground(Void... voids) {
                return requester.requestDetails(token);
            }
        });
    }

    private void onReceiveDetails(int resultCode, Serializable result) {
        if (resultCode != RESULT_OK) {
            finish();
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
        final String[] tags = { Tag.BANK_CARD_DETAILS_FRAGMENT, Tag.PERSONAL_DETAILS_FRAGMENT };
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
        return new MenuHelperItemsProvider(this) {
            @Override
            protected MenuItemHelper[] getMenuItemHelpers() {
                return new MenuItemHelper[] {
                        prepareSignOutHelper(),
                        MenuItemHelpers.SETTINGS,
                        MenuItemHelpers.HELP
                };
            }
        };
    }

    private MenuItemHelper prepareSignOutHelper() {
        final MenuItemHelper.Action signOutAction = new MenuItemHelper.Action() {
            @Override
            public void run(Activity activity) {
                onSignOutAction();
            }
        };
        final Predicate signOutActionIsEnable = new Predicate() {
            @Override
            public boolean isTrue() {
                return isSignOutEnabled;
            }
        };
        return MenuItemHelpers.SIGNOUT
                .withAction(signOutAction)
                .withPredicateEnable(signOutActionIsEnable);
    }

    private void onSignOutAction() {
        startSignOutTask();
    }

    private void enableSignOut() {
        setEnabledSignOutAction(true);
    }

    private void disableSignOut() {
        setEnabledSignOutAction(false);
    }

    private void setEnabledSignOutAction(boolean enabled) {
        isSignOutEnabled = enabled;

        updateSignOutActionState();
    }

    private void updateSignOutActionState() {
        Menu menu = getMenu();
        if (menu != null) {
            MenuItem signOutAction = menu.findItem(MenuItemIds.SIGNOUT);
            signOutAction.setEnabled(!isRunningTask() && isSignOutEnabled);
        }
    }

    private void startSignOutTask() {
        if (isInvalidToken(optionalToken)) {
            onTryingSignOutWhenCurrentTokenAlreadyIsInvalid();
            return;
        }

        TaskNotBeInterruptedDuringConfigurationChange signOutTask = new TaskNotBeInterruptedDuringConfigurationChange() {
            @Override
            protected Serializable doInBackground(Void... voids) {
                signOut();
                return null;
            }
        };

        startTask(TaskRequestCode.SIGN_OUT, signOutTask, R.string.sign_out_in_progress);
    }

    private void signOut() {
        Optional<AuthenticationToken> tokenToInvalidate = optionalToken;
        optionalToken = Optional.absent();
        AuthenticationTokenInvalidator tokenInvalidator =
                new AuthenticationTokenInvalidator(DroogCompaniiApplication.getContext());
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
        Fragment fragment = fragmentManager.findFragmentByTag(Tag.BANK_CARD_DETAILS_FRAGMENT);
        if (fragment != null) {
            fragmentManager.popBackStackImmediate();
        }
    }

    private void recreatePersonalDetailsFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new PersonalDetailsFragment();
        transaction.replace(R.id.containerOfFragment, fragment, Tag.PERSONAL_DETAILS_FRAGMENT);
        transaction.commitAllowingStateLoss();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        updateSignOutActionState();
        return result;
    }

}
