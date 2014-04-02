package ru.droogcompanii.application.util.ui.activity;

import android.content.Intent;

/**
 * Created by ls on 02.04.14.
 */
public class ReuseAlreadyLaunchedActivityFlag {

    public static void set(Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }
}
