package ru.droogcompanii.application.util.ui.able_to_start_task;

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

    private static final String KEY_REQUEST_CODE = "KEY_REQUEST_CODE";


    private boolean shouldNotReturnResult = false;
    private boolean isNeedToReturnResult = false;
    private boolean isResultReturned = false;
    private TaskNotBeInterruptedDuringConfigurationChange task;

    private int resultCode = Activity.RESULT_CANCELED;
    private Serializable result = null;
    private Integer titleId;
    private int requestCode;


    public static TaskFragment newInstance(int requestCode) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_REQUEST_CODE, requestCode);
        fragment.setArguments(args);
        return fragment;
    }

    public void setTask(TaskNotBeInterruptedDuringConfigurationChange task) {
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

        requestCode = getArguments().getInt(KEY_REQUEST_CODE);
        if (savedInstanceState == null && task != null) {
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
        cancelTaskIfNeed();
        tryReturnResult();
    }

    private void cancelTaskIfNeed() {
        if (task != null) {
            if (!task.isCancelled()) {
                task.cancel(false);
            }
            task = null;
        }
    }

    private void tryReturnResult() {
        cancelTaskIfNeed();
        if (isResultReturned) {
            return;
        }
        if (getTargetFragment() != null) {
            returnResult();
        }
    }

    private void returnResult() {
        if (shouldNotReturnResult) {
            return;
        }
        isResultReturned = true;
        FragmentAbleToStartTask ableToStartTask = (FragmentAbleToStartTask) getTargetFragment();
        if (ableToStartTask.isAbleToReceiveResult()) {
            ableToStartTask.onTaskFinished(requestCode, resultCode, result);
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

    public void getResultIfItReturnedWhenResultReceiverDisabledToReceiveIt() {
        if (isNeedToReturnResult) {
            returnResult();
        }
    }

    public void detachTask() {
        shouldNotReturnResult = true;
        cancelTaskIfNeed();
    }
}