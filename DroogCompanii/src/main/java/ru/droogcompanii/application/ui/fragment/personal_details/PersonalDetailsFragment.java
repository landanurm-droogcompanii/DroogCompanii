package ru.droogcompanii.application.ui.fragment.personal_details;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.common.base.Optional;

import java.io.Serializable;

import ru.droogcompanii.application.data.personal_details.PersonalDetails;
import ru.droogcompanii.application.ui.activity.able_to_start_task.AbleToStartTask;
import ru.droogcompanii.application.ui.activity.personal_account.PersonalAccountActivity;
import ru.droogcompanii.application.ui.activity.signin.AuthenticationToken;
import ru.droogcompanii.application.ui.activity.signin.AuthenticationTokenSaverLoader;
import ru.droogcompanii.application.ui.helpers.task.TaskNotBeInterrupted;
import ru.droogcompanii.application.util.LogUtils;

/**
 * Created by ls on 21.02.14.
 */
public class PersonalDetailsFragment extends Fragment implements PersonalAccountActivity.Callbacks {

    private static final String KEY_DETAILS = "Details";
    private static final String KEY_IS_TOKEN_REQUESTED = "IS_TOKEN_REQUESTED";
    private static final String KEY_IS_DETAILS_REQUESTED = "IS_DETAILS_REQUESTED";;

    private AbleToStartTask ableToStartTask;
    private PersonalDetailsViewHelper detailsViewHelper;
    private Optional<PersonalDetails> optionalDetails;

    private boolean isDetailsRequested;
    private boolean isTokenRequested;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ableToStartTask = (AbleToStartTask) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        OnBankCardSelectedListener listener = (OnBankCardSelectedListener) getActivity();
        detailsViewHelper = new PersonalDetailsViewHelper(inflater, listener);
        return detailsViewHelper.getView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initState(savedInstanceState);

        setDefaultTitle();

        updateDetails();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setDefaultTitle() {
        PersonalAccountActivity activity = (PersonalAccountActivity) getActivity();
        activity.setDefaultTitle();
    }

    private void initState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            isDetailsRequested = false;
            isTokenRequested = false;
            optionalDetails = Optional.absent();
        } else {
            isDetailsRequested = savedInstanceState.getBoolean(KEY_IS_DETAILS_REQUESTED);
            isTokenRequested = savedInstanceState.getBoolean(KEY_IS_TOKEN_REQUESTED);
            optionalDetails = (Optional<PersonalDetails>) savedInstanceState.getSerializable(KEY_DETAILS);
        }
    }

    private void updateDetails() {
        if (optionalDetails.isPresent()) {
            detailsViewHelper.displayDetails(optionalDetails.get());
        } else if (!isDetailsRequested) {
            requestDetails();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_DETAILS, optionalDetails);
        outState.putBoolean(KEY_IS_DETAILS_REQUESTED, isDetailsRequested);
        outState.putBoolean(KEY_IS_TOKEN_REQUESTED, isTokenRequested);
    }

    private void requestDetails() {
        Optional<AuthenticationToken> optionalToken = getToken();
        boolean isThereValidToken = optionalToken.isPresent() && optionalToken.get().isValid();
        if (isThereValidToken) {
            requestDetails(optionalToken.get());
        } else if (!isTokenRequested) {
            requestToken();
        }
    }

    private Optional<AuthenticationToken> getToken() {
        AuthenticationTokenSaverLoader saverLoader = new AuthenticationTokenSaverLoader(getActivity());
        return saverLoader.load();
    }

    private void requestDetails(AuthenticationToken token) {
        isDetailsRequested = true;
        Context context = getActivity();
        PersonalDetailsRequester requester = new PersonalDetailsRequesterFromInetAndDatabase(context, token);
        TaskNotBeInterrupted requestDetailsTask = new PersonalDetailsRequestTask(requester);
        ableToStartTask.startTask(requestDetailsTask);
    }

    private void requestToken() {
        isTokenRequested = true;
        PersonalAccountActivity activity = (PersonalAccountActivity) getActivity();
        activity.requestToken();
    }

    @Override
    public void onTokenReceived(AuthenticationToken token) {
        AuthenticationTokenSaverLoader saverLoader = new AuthenticationTokenSaverLoader(getActivity());
        saverLoader.save(token);
        isTokenRequested = false;
        requestDetails();
    }

    @Override
    public void onTaskResult(int resultCode, Serializable result) {
        onRequestedDetailsReturning(resultCode, (Optional<PersonalDetails>) result);
    }

    private void onRequestedDetailsReturning(int resultCode, Optional<PersonalDetails> optionalResult) {
        if (resultCode != Activity.RESULT_OK) {
            onDetailsAreNotReceived(resultCode);
        } else if (optionalResult.isPresent()) {
            onReceivingDetails(optionalResult);
        } else {
            onReceivingDetailsCannotBeRepresented();
        }
        isDetailsRequested = false;
    }

    private void onDetailsAreNotReceived(int resultCode) {
        if (resultCode == Activity.RESULT_CANCELED) {
            getActivity().finish();
        } else {
            Toast.makeText(getActivity(), "Details are not received", Toast.LENGTH_SHORT).show();
        }
    }

    private void onReceivingDetails(Optional<PersonalDetails> result) {
        optionalDetails = result;
        updateDetails();
    }

    private void onReceivingDetailsCannotBeRepresented() {
        String message = "Receiving details cannot be represented";
        LogUtils.debug(message);
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void onSignOut() {
        Optional<AuthenticationToken> optionalToken = getToken();
        if (!isValidToken(optionalToken)) {
            return;
        }
        SignOutTask signOutTask = new SignOutTask(getActivity(), optionalToken, new Runnable() {
            @Override
            public void run() {
                onSignOutCompleted();
            }
        });
        signOutTask.execute();
    }

    private static boolean isValidToken(Optional<AuthenticationToken> optionalToken) {
        return (optionalToken.isPresent() && optionalToken.get().isValid());
    }

    private void onSignOutCompleted() {
        invalidateDetails();
        requestDetails();
    }

    private void invalidateDetails() {
        optionalDetails = Optional.absent();
    }
}
