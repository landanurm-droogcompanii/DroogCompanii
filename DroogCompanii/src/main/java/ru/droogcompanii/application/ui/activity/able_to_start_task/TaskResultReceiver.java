package ru.droogcompanii.application.ui.activity.able_to_start_task;

import java.io.Serializable;

/**
 * Created by ls on 21.02.14.
 */
public interface TaskResultReceiver {
    void onTaskResult(int resultCode, Serializable result);
}
