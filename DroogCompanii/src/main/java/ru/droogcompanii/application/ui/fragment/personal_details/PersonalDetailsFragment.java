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
import ru.droogcompanii.application.ui.activity.login.AuthenticationToken;
import ru.droogcompanii.application.ui.activity.login.AuthenticationTokenSaverLoader;
import ru.droogcompanii.application.ui.activity.personal_account.PersonalAccountActivity;
import ru.droogcompanii.application.ui.helpers.task.TaskNotBeInterrupted;
import ru.droogcompanii.application.util.LogUtils;

/**
 * Created by ls on 21.02.14.
 */
public class PersonalDetailsFragment extends Fragment implements PersonalAccountActivity.Callbacks {

    private static final String KEY_DETAILS = "Details";
    private static final String KEY_IS_TOKEN_REQUESTED = "IS_TOKEN_REQUESTED";

    private AbleToStartTask ableToStartTask;
    private PersonalDetails personalDetails;
    private PersonalDetailsViewHelper detailsViewHelper;

    private boolean isTokenRequested;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ableToStartTask = (AbleToStartTask) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        detailsViewHelper = new PersonalDetailsViewHelper(inflater);
        return detailsViewHelper.getView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initState(savedInstanceState);

        if (savedInstanceState == null) {
            requestDetails();
        } else {
            personalDetails = (PersonalDetails) savedInstanceState.getSerializable(KEY_DETAILS);
            tryDisplayDetails();
        }
    }

    private void initState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            isTokenRequested = false;
        } else {
            isTokenRequested = savedInstanceState.getBoolean(KEY_IS_TOKEN_REQUESTED);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_DETAILS, (Serializable) personalDetails);
    }

    private void tryDisplayDetails() {
        if (personalDetails != null) {
            detailsViewHelper.displayDetails(personalDetails);
        }
    }

    private void requestDetails() {
        Optional<AuthenticationToken> optionalToken = getToken();
        if (optionalToken.isPresent()) {
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
        Context context = getActivity();
        PersonalDetailsRequester requester = new PersonalDetailsRequesterFromInetAndDatabase(context, token);
        TaskNotBeInterrupted requestDetailsTask = new PersonalDetailsRequestTask(requester);
        ableToStartTask.startTask(requestDetailsTask, null);
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
        if (resultCode != Activity.RESULT_OK) {
            onDetailsAreNotReceived(resultCode);
        }
        Optional<PersonalDetails> optionalResult = (Optional<PersonalDetails>) result;
        if (optionalResult.isPresent()) {
            onReceivingDetails(optionalResult.get());
        } else {
            onReceivingDetailsCannotBeRepresented();
        }
    }

    private void onDetailsAreNotReceived(int resultCode) {
        if (resultCode == Activity.RESULT_CANCELED) {
            getActivity().finish();
        } else {
            Toast.makeText(getActivity(), "Details are not received", Toast.LENGTH_SHORT).show();
        }
    }

    private void onReceivingDetails(PersonalDetails result) {
        this.personalDetails = result;
        tryDisplayDetails();
    }

    private void onReceivingDetailsCannotBeRepresented() {
        String message = "Receiving details cannot be represented";
        LogUtils.debug(message);
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
