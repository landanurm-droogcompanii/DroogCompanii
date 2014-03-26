package ru.droogcompanii.application.ui.activity.filter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.fragment.filter.FilterFragment;
import ru.droogcompanii.application.ui.fragment.filter.FilterUtils;
import ru.droogcompanii.application.ui.fragment.filter.filters.Filters;
import ru.droogcompanii.application.util.activity.ActionBarActivityWithUpButton;
import ru.droogcompanii.application.util.view.YesNoDialogMaker;
import ru.droogcompanii.application.util.Objects;

/**
 * Created by ls on 15.01.14.
 */

public class FilterActivity extends ActionBarActivityWithUpButton
                            implements FilterFragment.Callbacks {

    public static final int REQUEST_CODE = 14235;

    private static final String KEY_ARGS = "KEY_ARGS";

    private static enum State {
        ON_BACK_PRESSED,
        ON_UP,
        NORMAL
    }

    private static final String KEY_STATE = "State";

    private Bundle args;
    private FilterFragment filterFragment;
    private State state;

    private final Runnable actionExitWithoutCommittingChanges = new Runnable() {
        @Override
        public void run() {
            setResult(RESULT_CANCELED);
            if (state == State.ON_UP) {
                FilterActivity.super.onNeedToNavigateUp();
            } else if (state == State.ON_BACK_PRESSED) {
                FilterActivity.super.onBackPressed();;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        if (savedInstanceState == null) {
            initByDefault();
        } else {
            restore(savedInstanceState);
        }

        filterFragment = findFilterFragment();
    }

    private void initByDefault() {
        args = getPassedBundle();
        state = State.NORMAL;
    }

    private Bundle getPassedBundle() {
        return getIntent().getExtras();
    }

    private void restore(Bundle savedInstanceState) {
        args = savedInstanceState.getBundle(KEY_ARGS);
        state = (State) savedInstanceState.getSerializable(KEY_STATE);
        restoreCautionIfNeed();
    }

    private void restoreCautionIfNeed() {
        if (state != State.NORMAL) {
            cautionAboutExitWithoutCommittingChanges();
        }
    }

    @Override
    public Bundle getArguments() {
        return args;
    }

    private FilterFragment findFilterFragment() {
        return (FilterFragment) getSupportFragmentManager().findFragmentById(R.id.filterFragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_done) {
            onDone();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(KEY_ARGS, args);
        outState.putSerializable(KEY_STATE, state);
    }

    private void onDone() {
        Filters displayedFilters = filterFragment.getDisplayedFilters();
        if (areDifferent(displayedFilters, getFiltersDuringFirstLaunch())) {
            FilterUtils.shareFilters(this, displayedFilters);
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    private static boolean areDifferent(Filters one, Filters two) {
        return !Objects.equals(one, two);
    }

    private Filters getFiltersDuringFirstLaunch() {
        return FilterUtils.getCurrentFilters(this, filterFragment.getPartnerCategory());
    }

    @Override
    public void onBackPressed() {
        state = State.ON_BACK_PRESSED;
        cautionAboutExitWithoutCommittingChangesIfNeed();
    }

    @Override
    protected void onNeedToNavigateUp() {
        state = State.ON_UP;
        cautionAboutExitWithoutCommittingChangesIfNeed();
    }

    private void cautionAboutExitWithoutCommittingChangesIfNeed() {
        if (wereNotChanges()) {
            actionExitWithoutCommittingChanges.run();
        } else {
            cautionAboutExitWithoutCommittingChanges();
        }
    }

    private boolean wereNotChanges() {
        Filters currentFilters = FilterUtils.getCurrentFilters(this, filterFragment.getPartnerCategory());
        Filters displayedFilters = filterFragment.getDisplayedFilters();
        return Objects.equals(currentFilters, displayedFilters);
    }

    private void cautionAboutExitWithoutCommittingChanges() {
        YesNoDialogMaker dialogMaker = new YesNoDialogMaker(this, android.R.string.ok, android.R.string.cancel);
        DialogInterface.OnClickListener onExitWithoutCommittingChangesListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                actionExitWithoutCommittingChanges.run();
            }
        };
        DialogInterface.OnClickListener onCancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onCancelReturningToPreviousActivity();
            }
        };
        AlertDialog dialog = dialogMaker.make(
                R.string.cautionExitWithoutCommittingChangesInCurrentFilters,
                onExitWithoutCommittingChangesListener, onCancelListener);
        dialog.setCancelable(true);
    }

    private void onCancelReturningToPreviousActivity() {
        state = State.NORMAL;
    }
}
