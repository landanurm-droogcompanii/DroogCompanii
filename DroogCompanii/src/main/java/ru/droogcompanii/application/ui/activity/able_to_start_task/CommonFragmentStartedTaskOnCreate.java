package ru.droogcompanii.application.ui.activity.able_to_start_task;

/**
 * Created by ls on 21.02.14.
 */
public class CommonFragmentStartedTaskOnCreate extends FragmentStartedTaskOnCreate {

    private Integer titleId;
    private TaskNotBeInterrupted task;

    public void set(TaskNotBeInterrupted task, Integer titleId) {
        this.task = task;
        this.titleId = titleId;
    }

    @Override
    protected Integer getTaskDialogTitleId() {
        return titleId;
    }

    @Override
    protected TaskNotBeInterrupted getTask() {
        return task;
    }
}
