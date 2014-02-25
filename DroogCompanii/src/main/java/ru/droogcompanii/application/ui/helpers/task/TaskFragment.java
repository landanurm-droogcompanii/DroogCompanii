package ru.droogcompanii.application.ui.helpers.task;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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

    private boolean resultReturned;
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
        if (Objects.equals(titleId, NO_TITLE_ID)) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        } else {
            getDialog().setTitle(titleId);
        }
        getDialog().setCanceledOnTouchOutside(false);
        return inflater.inflate(R.layout.fragment_task, container);
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
        returnResult();
    }

    private void returnResult() {
        if (resultReturned) {
            return;
        }
        Fragment targetFragment = getTargetFragment();
        if (targetFragment != null) {
            TaskFragmentHolder taskFragmentHolder = (TaskFragmentHolder) targetFragment;
            taskFragmentHolder.onResult(TaskFragmentHolder.REQUEST_CODE_TASK_FRAGMENT, resultCode, result);
            resultReturned = true;
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
        if (isResumed()) {
            dismiss();
        }
        task = null;
        returnResult();
    }

    private void setResult(int resultCode, Serializable result) {
        this.resultCode = resultCode;
        this.result = result;
    }
}
