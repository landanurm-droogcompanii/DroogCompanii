package ru.droogcompanii.application.ui.fragment.personal_details;

import android.content.Context;
import android.os.AsyncTask;

import com.google.common.base.Optional;

import ru.droogcompanii.application.ui.activity.signin.AuthenticationToken;
import ru.droogcompanii.application.util.WeakReferenceWrapper;

/**
 * Created by ls on 24.02.14.
 */
public class SignOutTask extends AsyncTask<Void, Void, Void> {
    private final WeakReferenceWrapper<Context> contextWrapper;
    private final Optional<AuthenticationToken> optionalToken;
    private final Runnable runnableOnSignOutCompleted;

    public SignOutTask(Context context, Optional<AuthenticationToken> optionalToken,
                       Runnable runnableOnSignOutCompleted) {
        this.contextWrapper = new WeakReferenceWrapper<Context>(context);
        this.optionalToken = optionalToken;
        this.runnableOnSignOutCompleted = runnableOnSignOutCompleted;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        contextWrapper.handleIfExist(new WeakReferenceWrapper.Handler<Context>() {
            @Override
            public void handle(Context context) {
                SignOutHelper signOutHelper = new SignOutHelper(context, optionalToken);
                signOutHelper.signOut();
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        contextWrapper.handleIfExist(new WeakReferenceWrapper.Handler<Context>() {
            @Override
            public void handle(Context ref) {
                runnableOnSignOutCompleted.run();
            }
        });
    }
}