package ru.droogcompanii.application.activity.helpers.task;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.droogcompanii.application.R;

/**
 * Created by ls on 26.12.13.
 */
public abstract class TaskFragment extends DialogFragment {
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
        if (getTargetFragment() != null) {
            getTargetFragment().onActivityResult(TaskActivityMainFragment.TASK_FRAGMENT, Activity.RESULT_CANCELED, null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (task == null) {
            dismiss();
        }
    }

    public void onTaskFinished() {
        if (isResumed()) {
            dismiss();
        }
        task = null;
        if (getTargetFragment() != null) {
            getTargetFragment().onActivityResult(TaskActivityMainFragment.TASK_FRAGMENT, Activity.RESULT_OK, null);
        }
    }
}
