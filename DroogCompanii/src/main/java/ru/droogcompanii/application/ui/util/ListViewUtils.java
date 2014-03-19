package ru.droogcompanii.application.ui.util;

import android.view.View;
import android.widget.ListView;

import com.google.common.base.Optional;

/**
 * Created by ls on 19.03.14.
 */
public class ListViewUtils {

    public static Optional<View> getItemView(ListView listView, int position) {
        int firstPosition = listView.getFirstVisiblePosition() - listView.getHeaderViewsCount();
        int positionRelativeToTheFirstPosition = position - firstPosition;
        if (positionRelativeToTheFirstPosition < 0 ||
            positionRelativeToTheFirstPosition >= listView.getChildCount()) {

            return Optional.absent();
        }
        View itemView = listView.getChildAt(positionRelativeToTheFirstPosition);
        return Optional.of(itemView);
    }
}
