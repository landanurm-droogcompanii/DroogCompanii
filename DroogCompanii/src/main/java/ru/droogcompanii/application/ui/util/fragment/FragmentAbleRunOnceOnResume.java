package ru.droogcompanii.application.ui.util.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by ls on 03.03.14.
 */
public class FragmentAbleRunOnceOnResume extends Fragment {

    private static final Runnable DUMMY_RUNNABLE_ON_RESUME = new Runnable() {
        @Override
        public void run() {
            // do nothing
        }
    };

    private Runnable runnableOnResume;

    public FragmentAbleRunOnceOnResume() {
        runnableOnResume = DUMMY_RUNNABLE_ON_RESUME;
    }

    public final void callOnceOnResume(Runnable runnable) {
        runnableOnResume = runnable;
    }

    @Override
    public void onResume() {
        super.onResume();
        runnableOnResume.run();
        runnableOnResume = DUMMY_RUNNABLE_ON_RESUME;
    }
}
