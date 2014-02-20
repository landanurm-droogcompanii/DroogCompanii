package ru.droogcompanii.application.ui.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;

public class LoginActivity extends FragmentActivity implements TaskFragmentHolder.Callbacks {

    public static final String KEY_LOGIN = "login";
    public static final String KEY_PASSWORD = "password";

    private static final String TAG_TASK_FRAGMENT_HOLDER = "SignInTask";

    public static void startForResult(Activity activity, String login) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtra(EXTRA_LOGIN, login);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    public static final int REQUEST_CODE = 6;
    public static final String EXTRA_LOGIN = "com.example.android.authenticatordemo.extra.LOGIN";

    private String login;
    private String password;

    private EditText loginInput;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        login = getIntent().getStringExtra(EXTRA_LOGIN);
        loginInput = (EditText) findViewById(R.id.login);
        loginInput.setText(login);

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    public void attemptLogin() {

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
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment taskFragment = new SignInTaskFragmentHolder();
        taskFragment.setArguments(prepareArguments(login, password));
        transaction.add(R.id.taskFragmentContainer, taskFragment, TAG_TASK_FRAGMENT_HOLDER);
        transaction.commit();
    }

    private static Bundle prepareArguments(String login, String password) {
        Bundle args = new Bundle();
        args.putString(KEY_LOGIN, login);
        args.putString(KEY_PASSWORD, password);
        return args;
    }

    private void onFieldRequired(EditText fieldInput) {
        fieldInput.setError(getString(R.string.error_field_required));
        fieldInput.requestFocus();
    }

    @Override
    public void onTaskFinished(int resultCode, Serializable result) {
        if (resultCode == RESULT_CANCELED) {
            onAuthenticationCancelled();
        } else if (resultCode == RESULT_OK) {
            onAuthenticationCompleted((AuthenticationResult) result);
        }
    }

    private void onAuthenticationCancelled() {
        // do nothing
    }

    private void onAuthenticationCompleted(AuthenticationResult result) {
        if (result.isSuccessful()) {
            onVerificationCompletedSuccessfully(result);
        } else {
            onVerificationFailed(result);
        }
    }

    private void onVerificationFailed(AuthenticationResult result) {
        Toast.makeText(this, "Verification Failed", Toast.LENGTH_LONG).show();
    }

    private void onVerificationCompletedSuccessfully(AuthenticationResult result) {
        setResult(RESULT_OK);
        finish();
    }
}
