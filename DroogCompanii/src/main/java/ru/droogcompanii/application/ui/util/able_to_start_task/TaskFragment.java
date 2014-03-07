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

/**
 * Created by ls on 26.12.13.
 */
public class TaskFragment extends DialogFragment {

    public static final Integer NO_TITLE_ID = null;
    private static final String KEY_REQUEST_CODE = "KEY_REQUEST_CODE";

    private boolean isResultReturnedDuringDisactivity;
    private boolean isResultReturned;
    private int resultCode;
    private int requestCode;
    private Serializable result;
    private TaskNotBeInterrupted task;
    private Integer titleId;

    public static TaskFragment newInstance(int requestCode) {
        Bundle args = new Bundle();
        args.putInt(KEY_REQUEST_CODE, requestCode);
        TaskFragment fragment = new TaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public TaskFragment() {
        isResultReturnedDuringDisactivity = false;
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
        if (savedInstanceState == null) {
            extractRequestCodeFromArguments();
        }
        setRetainInstance(true);
        if (task != null) {
            task.execute();
        }
    }

    private void extractRequestCodeFromArguments() {
        requestCode = getArguments().getInt(KEY_REQUEST_CODE);
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
            ableToStartTask.onTaskResult(requestCode, resultCode, result);
            finishFragment();
        } else {
            isResultReturnedDuringDisactivity = true;
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
        if (isResultReturnedDuringDisactivity) {
            isResultReturnedDuringDisactivity = false;
            returnTaskResult();
        }
    }
}
