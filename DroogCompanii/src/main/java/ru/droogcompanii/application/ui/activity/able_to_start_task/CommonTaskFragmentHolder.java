package ru.droogcompanii.application.ui.activity.able_to_start_task;

import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;
import ru.droogcompanii.application.ui.helpers.task.TaskNotBeInterrupted;

/**
 * Created by ls on 21.02.14.
 */
public class CommonTaskFragmentHolder extends TaskFragmentHolder {

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
    protected TaskNotBeInterrupted prepareTask() {
        return task;
    }
}
