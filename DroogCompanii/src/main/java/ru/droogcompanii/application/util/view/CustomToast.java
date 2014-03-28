package ru.droogcompanii.application.util.view;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

/**
 * Created by ls on 27.03.14.
 */
public class CustomToast {

    public static interface ViewInitializer {
        void init(CustomToast customToast, View view);
    }

    private static final ViewInitializer DUMMY_VIEW_INITIALIZER = new ViewInitializer() {
        @Override
        public void init(CustomToast customToast, View view) {
            // do nothing
        }
    };


    private static final int ANIMATION_DURATION = 600;
    private static final int DELAY_HIDE = 5000;

    private AlphaAnimation fadeInAnimation;
    private AlphaAnimation fadeOutAnimation;
    private Handler handler;
    private View contentView;
    private ViewGroup container;

    private final Activity activity;
    private final int containerViewId;
    private final int contentViewId;
    private final ViewInitializer viewInitializer;

    private final HideRunnable RUNNABLE_HIDE = new HideRunnable();


    public CustomToast(Activity activity, int containerViewId, int contentViewId) {
        this(activity, containerViewId, contentViewId, DUMMY_VIEW_INITIALIZER);
    }

    public CustomToast(Activity activity, int containerViewId, int contentViewId, ViewInitializer viewInitializer) {
        this.activity = activity;
        this.containerViewId = containerViewId;
        this.contentViewId = contentViewId;
        this.viewInitializer = viewInitializer;
    }

    protected Activity getActivity() {
        return activity;
    }

    public final void show() {
        show(ANIMATION_DURATION);
    }

    public final void showImmediately() {
        show(0);
    }

    public void show(int duration) {
        init();
        contentView.setVisibility(View.VISIBLE);
        fadeInAnimation.setDuration(duration);
        contentView.startAnimation(fadeInAnimation);
        handler.postDelayed(RUNNABLE_HIDE, DELAY_HIDE);
    }

    public void stopHiding() {
        if (fadeOutAnimation != null) {
            fadeOutAnimation.cancel();
            handler.postDelayed(RUNNABLE_HIDE, DELAY_HIDE);
        }
    }

    public void hide() {
        RUNNABLE_HIDE.run();
    }


    private void init() {
        inflateViews();
        viewInitializer.init(this, contentView);
        fadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
        fadeOutAnimation = new AlphaAnimation(1.0f, 0.0f);
        fadeOutAnimation.setDuration(ANIMATION_DURATION);
        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // skip
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                onShowingFinished();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // skip
            }
        });

        handler = new Handler();
        contentView.setVisibility(View.GONE);
    }

    private void inflateViews() {
        container = (ViewGroup) activity.findViewById(containerViewId);
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        contentView = layoutInflater.inflate(contentViewId, null);
        final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        container.addView(contentView, layoutParams);
    }

    private void onShowingFinished() {
        contentView.setVisibility(View.GONE);
        container.removeView(contentView);
        contentView = null;
        container = null;
        fadeInAnimation = null;
        fadeOutAnimation = null;
    }

    private class HideRunnable implements Runnable {
        @Override
        public void run() {
            if (contentView != null) {
                contentView.startAnimation(fadeOutAnimation);
            }
        }
    };
}