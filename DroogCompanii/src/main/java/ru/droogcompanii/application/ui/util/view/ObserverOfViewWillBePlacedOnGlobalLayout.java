package ru.droogcompanii.application.ui.util.view;

import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

import ru.droogcompanii.application.ui.util.constants.ApiVersionUtils;

/**
 * Created by ls on 23.12.13.
 */
public class ObserverOfViewWillBePlacedOnGlobalLayout {

    public static void runAfterViewWillBePlacedOnLayout(final View view, final Runnable runnable) {
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver == null) {
            return;
        }
        if (!viewTreeObserver.isAlive()) {
            return;
        }
        if (isViewAlreadyOnLayout(view)) {
            runnable.run();
            return;
        }
        viewTreeObserver.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        removeOnGlobalLayoutListener(view, this);
                        runnable.run();
                    }
                }
        );
    }

    private static boolean isViewAlreadyOnLayout(View view) {
        final int REQUIRED_MIN_VERSION = 18;
        return (ApiVersionUtils.getCurrentVersion() >= REQUIRED_MIN_VERSION) && view.isInLayout();
    }

    @SuppressWarnings("deprecation")
    private static void removeOnGlobalLayoutListener(View view, ViewTreeObserver.OnGlobalLayoutListener listener) {
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver == null) {
            return;
        }
        if (Build.VERSION.SDK_INT < 16) {
            viewTreeObserver.removeGlobalOnLayoutListener(listener);
        } else {
            viewTreeObserver.removeOnGlobalLayoutListener(listener);
        }
    }
}
