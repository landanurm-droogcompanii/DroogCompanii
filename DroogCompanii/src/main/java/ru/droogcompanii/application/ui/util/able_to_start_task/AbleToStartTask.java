package ru.droogcompanii.application.ui.util.able_to_start_task;

/**
 * Created by ls on 21.02.14.
 */
public interface AbleToStartTask {

    void startTask(int requestCode, TaskNotBeInterrupted task);
    void startTask(int requestCode, TaskNotBeInterrupted task, Integer titleId);

}
