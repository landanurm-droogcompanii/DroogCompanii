package ru.droogcompanii.application.ui.activity.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import ru.droogcompanii.application.R;

public class LoginActivity extends Activity {

    public static void startForResult(Activity activity, String loginKey) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtra(EXTRA_LOGIN_KEY, loginKey);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }


    public static final int REQUEST_CODE = 6;
    public static final String EXTRA_LOGIN_KEY = "com.example.android.authenticatordemo.extra.LOGIN_KEY";

    private UserLoginTask authTask = null;

    private String loginKey;
    private String password;


    private EditText loginKeyView;
    private EditText passwordView;
    private View loginFormView;
    private View loginStatusView;
    private TextView loginStatusMessageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        loginKey = getIntent().getStringExtra(EXTRA_LOGIN_KEY);
        loginKeyView = (EditText) findViewById(R.id.loginKey);
        loginKeyView.setText(loginKey);

        passwordView = (EditText) findViewById(R.id.password);
        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        loginFormView = findViewById(R.id.login_form);
        loginStatusView = findViewById(R.id.login_status);
        loginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

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
        if (authTask != null) {
            return;
        }

        loginKeyView.setError(null);
        passwordView.setError(null);

        loginKey = loginKeyView.getText().toString();
        password = passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        CheckResult checkResult = PasswordChecker.check(password);
        if (!checkResult.isSuccessful) {
            passwordView.setError(getString(checkResult.errorDescriptionTextId));
            focusView = passwordView;
            cancel = true;
        }

        checkResult = LoginChecker.check(loginKey);
        if (!checkResult.isSuccessful) {
            loginKeyView.setError(getString(checkResult.errorDescriptionTextId));
            focusView = loginKeyView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            loginStatusMessageView.setText(R.string.login_progress_signing_in);
            showProgress(true);
            authTask = new UserLoginTask();
            authTask.execute((Void) null);
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            loginStatusView.setVisibility(View.VISIBLE);
            loginStatusView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            loginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
                        }
                    });

            loginFormView.setVisibility(View.VISIBLE);
            loginFormView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });
        } else {
            loginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    public class UserLoginTask extends AsyncTask<Void, Void, VerificationResult> {
        @Override
        protected VerificationResult doInBackground(Void... params) {
            PasswordVerifier passwordVerifier = new PasswordVerifier();
            return passwordVerifier.verify(loginKey, password);
        }

        @Override
        protected void onPostExecute(VerificationResult result) {
            authTask = null;
            showProgress(false);

            if (result.isSuccessful) {
                onVerificationIsSuccessful();
            } else {
                passwordView.setError(getString(result.errorDescriptionTextId));
                passwordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            authTask = null;
            showProgress(false);
        }
    }

    private void onVerificationIsSuccessful() {
        setResult(RESULT_OK);
        finish();
    }
}
