package ru.droogcompanii.application.ui.activity.able_to_start_task;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.util.CurrentMethodNameLogger;
import ru.droogcompanii.application.util.Objects;

/**
 * Created by ls on 26.12.13.
 */
public class TaskFragment extends DialogFragment {

    private final CurrentMethodNameLogger LOGGER = new CurrentMethodNameLogger(getClass());

    public static final Integer NO_TITLE_ID = null;

    private boolean isNeedToReturnResult = false;
    private boolean isResultReturned = false;
    private TaskNotBeInterrupted task;

    private int resultCode = Activity.RESULT_CANCELED;
    private Serializable result = null;
    private Integer titleId;

    public void setTask(TaskNotBeInterrupted task) {
        task.setFragment(this);
        this.task = task;
    }

    public void setTitle(Integer titleId) {
        this.titleId = titleId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LOGGER.log();

        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (task != null) {
            task.execute();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LOGGER.log();

        initDialog();
        return inflater.inflate(R.layout.fragment_task, container);
    }

    private void initDialog() {
        if (Objects.equals(titleId, NO_TITLE_ID)) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        } else {
            getDialog().setTitle(titleId);
        }
        getDialog().setCanceledOnTouchOutside(false);
    }

    @Override
    public void onDestroyView() {
        LOGGER.log();

        if ((getDialog() != null) && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        LOGGER.log();

        super.onDismiss(dialog);
        if (task != null) {
            task.cancel(false);
        }
        tryReturnResult();
    }

    private void tryReturnResult() {
        task = null;
        if (isResultReturned) {
            return;
        }
        if (getTargetFragment() != null) {
            returnResult();
        }
    }

    private void returnResult() {
        FragmentAbleToStartTask ableToStartTask = (FragmentAbleToStartTask) getTargetFragment();
        isResultReturned = true;
        if (ableToStartTask.isAbleToReceiveResult()) {
            ableToStartTask.onResult(FragmentAbleToStartTask.REQUEST_CODE_TASK_FRAGMENT, resultCode, result);
            finishFragment();
        } else {
            isNeedToReturnResult = true;
        }
    }

    private void finishFragment() {
        if (isResumed()) {
            dismiss();
        }
    }

    @Override
    public void onResume() {
        LOGGER.log();

        super.onResume();
        if (task == null) {
            dismiss();
        }
    }

    public void onTaskFinished(Serializable result) {
        LOGGER.log();

        setResult(Activity.RESULT_OK, result);
        tryReturnResult();
    }

    private void setResult(int resultCode, Serializable result) {
        this.resultCode = resultCode;
        this.result = result;
    }

    public void checkWhetherIsResultReturnedDuringDisactivity() {
        if (isNeedToReturnResult) {
            isNeedToReturnResult = false;
            returnResult();
        }
    }
}
