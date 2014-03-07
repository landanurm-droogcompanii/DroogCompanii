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

/**
 * Created by ls on 26.12.13.
 */
public class TaskFragment extends DialogFragment {

    public static final Integer NO_TITLE_ID = null;

    private boolean isNeedToReturnResult;
    private boolean isResultReturned;
    private int resultCode;
    private Serializable result;
    private TaskNotBeInterrupted task;
    private Integer titleId;

    public TaskFragment() {
        isNeedToReturnResult = false;
        isResultReturned = false;
        resultCode = Activity.RESULT_CANCELED;
        result = null;
    }

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
        initTitle();
        getDialog().setCanceledOnTouchOutside(false);
    }

    private void initTitle() {
        if (titleId != NO_TITLE_ID) {
            getDialog().setTitle(titleId);
        } else {
            hideTitleBar();
        }
    }

    private void hideTitleBar() {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
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
        tryReturnTaskResult();
    }

    private void tryReturnTaskResult() {
        task = null;
        if (isResultReturned) {
            return;
        }
        if (getTargetFragment() != null) {
            returnTaskResult();
        }
    }

    private void returnTaskResult() {
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
        super.onResume();
        if (task == null) {
            dismiss();
        }
    }

    public void onTaskFinished(Serializable result) {
        setTaskResult(Activity.RESULT_OK, result);
        tryReturnTaskResult();
    }

    private void setTaskResult(int resultCode, Serializable result) {
        this.resultCode = resultCode;
        this.result = result;
    }

    public void getTaskResultIfItReturnedDuringDisactivity() {
        if (isNeedToReturnResult) {
            isNeedToReturnResult = false;
            returnTaskResult();
        }
    }
}
