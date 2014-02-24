package ru.droogcompanii.application.ui.activity.able_to_start_task;

import ru.droogcompanii.application.ui.helpers.task.TaskNotBeInterrupted;

/**
 * Created by ls on 21.02.14.
 */
public interface AbleToStartTask {

    void startTask(TaskNotBeInterrupted task);
    void startTask(TaskNotBeInterrupted task, Integer titleId);

}
