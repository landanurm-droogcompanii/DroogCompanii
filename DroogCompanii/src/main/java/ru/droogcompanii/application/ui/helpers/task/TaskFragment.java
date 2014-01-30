package ru.droogcompanii.application.ui.helpers.task;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

import ru.droogcompanii.application.R;

/**
 * Created by ls on 26.12.13.
 */
public abstract class TaskFragment extends DialogFragment {
    private boolean resultPassed;
    private Task task;

    public void setTask(Task task) {
        task.setFragment(this);
        this.task = task;
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
        View view = inflater.inflate(R.layout.fragment_task, container);
        getDialog().setTitle(getDialogTitle());
        getDialog().setCanceledOnTouchOutside(false);
        return view;
    }

    protected abstract String getDialogTitle();

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
        onResult(TaskFragmentHolder.REQUEST_CODE_TASK_FRAGMENT, Activity.RESULT_CANCELED, null);
    }

    private void onResult(int requestCode, int resultCode, Serializable result) {
        if (resultPassed) {
            return;
        }
        if (getTaskFragmentHolder() != null) {
            resultPassed = true;
            getTaskFragmentHolder().onResult(requestCode, resultCode, result);
        }
    }

    private TaskFragmentHolder getTaskFragmentHolder() {
        return (TaskFragmentHolder) getTargetFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (task == null) {
            dismiss();
        }
    }

    public void onTaskFinished(Serializable result) {
        task = null;
        onResult(TaskFragmentHolder.REQUEST_CODE_TASK_FRAGMENT, Activity.RESULT_OK, result);
        if (isResumed()) {
            dismiss();
        }
    }
}
