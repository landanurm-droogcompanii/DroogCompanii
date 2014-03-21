package ru.droogcompanii.application.ui.util;

import android.os.Bundle;

/**
 * Created by ls on 21.03.14.
 */
public abstract class StateManager {

    public void initState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            initStateByDefault();
        } else {
            restoreState(savedInstanceState);
        }
    }

    public abstract void initStateByDefault();
    public abstract void restoreState(Bundle savedInstanceState);
    public abstract void saveState(Bundle outState);
}
