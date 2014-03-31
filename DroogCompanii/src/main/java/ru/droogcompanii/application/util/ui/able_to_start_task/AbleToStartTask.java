package ru.droogcompanii.application.util.ui.able_to_start_task;

/**
 * Created by ls on 21.02.14.
 */
public interface AbleToStartTask {
    void startTask(int requestCode, TaskNotBeInterruptedDuringConfigurationChange task);
    void startTask(int requestCode, TaskNotBeInterruptedDuringConfigurationChange task, Integer titleId);
}
