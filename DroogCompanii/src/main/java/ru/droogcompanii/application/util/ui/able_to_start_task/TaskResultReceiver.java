package ru.droogcompanii.application.util.ui.able_to_start_task;

import java.io.Serializable;

/**
 * Created by ls on 21.02.14.
 */
public interface TaskResultReceiver {
    void onTaskResult(int requestCode, int resultCode, Serializable result);
}
