package ru.droogcompanii.application.ui.util.able_to_start_task;

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
import ru.droogcompanii.application.util.Objects;

/**
 * Created by ls on 26.12.13.
 */
public class TaskFragment extends DialogFragment {

    public static final Integer NO_TITLE_ID = null;

    private static final String KEY_TASK_REQUEST_CODE = "KEY_TASK_REQUEST_CODE";

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
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (task != null) {
            task.execute();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        if ((getDialog() != null) && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
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
            ableToStartTask.onResult(FragmentAbleToStartTask.TASK_FRAGMENT_TARGET, resultCode, result);
            isNeedToReturnResult = false;
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
        super.onResume();
        if (task == null) {
            dismiss();
        }
    }

    public void onTaskFinished(Serializable result) {
        setResult(Activity.RESULT_OK, result);
        tryReturnResult();
    }

    private void setResult(int resultCode, Serializable result) {
        this.resultCode = resultCode;
        this.result = result;
    }

    public void getResultIfItReturnedDuringDisactivity() {
        if (isNeedToReturnResult) {
            returnResult();
        }
    }
}
