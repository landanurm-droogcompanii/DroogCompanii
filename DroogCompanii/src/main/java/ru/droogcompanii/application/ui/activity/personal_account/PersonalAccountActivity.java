package ru.droogcompanii.application.ui.activity.personal_account;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import ru.droogcompanii.application.ui.activity.login.LoginActivity;
import ru.droogcompanii.application.ui.helpers.ActionBarActivityWithUpButton;

/**
 * Created by ls on 31.01.14.
 */
public class PersonalAccountActivity extends ActionBarActivityWithUpButton {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginActivity.startForResult(this, "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LoginActivity.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                onLoginSuccessfully(data);
            } else {
                onCancelLogin();
            }
        }
    }

    private void onLoginSuccessfully(Intent data) {
        Toast.makeText(this, "Login successfully", Toast.LENGTH_SHORT).show();
    }

    private void onCancelLogin() {
        finish();
    }
}
