package ru.droogcompanii.application.util;

import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by ls on 23.12.13.
 */
public class ObserverOfViewWillBePlacedOnGlobalLayout {

    public static void runAfterViewWillBePlacedOnLayout(final View view, final Runnable runnable) {
        if (!view.getViewTreeObserver().isAlive()) {
            return;
        }
        view.getViewTreeObserver().addOnGlobalLayoutListener(
            new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    removeOnGlobalLayoutListener(view, this);
                    runnable.run();
                }
        });
    }

    @SuppressWarnings("deprecation")
    private static void removeOnGlobalLayoutListener(View view, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT < 16) {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }
}
