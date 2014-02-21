package ru.droogcompanii.application.ui.fragment.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.activity.login.AuthenticationResult;
import ru.droogcompanii.application.ui.activity.login.SignInTask;
import ru.droogcompanii.application.ui.activity.able_to_start_task.AbleToStartTask;
import ru.droogcompanii.application.ui.activity.able_to_start_task.TaskResultReceiver;
import ru.droogcompanii.application.ui.helpers.task.TaskNotBeInterrupted;

/**
 * Created by ls on 21.02.14.
 */
public class LoginFragment extends Fragment implements TaskResultReceiver {

    public static final String KEY_TOKEN = "token";

    private AbleToStartTask ableToStartTask;

    private String login;
    private String password;

    private EditText loginInput;
    private EditText passwordInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, null);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ableToStartTask = (AbleToStartTask) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loginInput = (EditText) findViewById(R.id.login);
        passwordInput = (EditText) findViewById(R.id.password);
        passwordInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private View findViewById(int viewId) {
        return getView().findViewById(viewId);
    }

    private void attemptLogin() {
        loginInput.setError(null);
        passwordInput.setError(null);

        login = loginInput.getText().toString();
        password = passwordInput.getText().toString();

        if (TextUtils.isEmpty(login)) {
            onFieldRequired(loginInput);
        } else if (TextUtils.isEmpty(password)) {
            onFieldRequired(passwordInput);
        } else {
            signIn();
        }
    }

    private void signIn() {
        startSignInTask();
    }

    private void startSignInTask() {
        TaskNotBeInterrupted signInTask = new SignInTask(getActivity(), login, password);
        ableToStartTask.startTask(signInTask, R.string.login_progress_signing_in);
    }

    private void onFieldRequired(EditText fieldInput) {
        fieldInput.setError(getString(R.string.error_field_required));
        fieldInput.requestFocus();
    }

    @Override
    public void onTaskResult(int resultCode, Serializable result) {
        if (resultCode == Activity.RESULT_CANCELED) {
            onAuthenticationCancelled();
        } else if (resultCode == Activity.RESULT_OK) {
            onAuthenticationCompleted((AuthenticationResult) result);
        }
    }

    private void onAuthenticationCancelled() {
        // do nothing
    }

    private void onAuthenticationCompleted(AuthenticationResult result) {
        if (result.isSuccessful()) {
            onAuthenticationCompletedSuccessfully(result);
        } else {
            onAuthenticationFailed(result);
        }
    }

    private void onAuthenticationFailed(AuthenticationResult result) {
        Toast.makeText(getActivity(), "Authentication Failed", Toast.LENGTH_LONG).show();
    }

    private void onAuthenticationCompletedSuccessfully(AuthenticationResult result) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_TOKEN, result.getToken());
        getActivity().setResult(Activity.RESULT_OK, resultIntent);
        getActivity().finish();
    }
}
