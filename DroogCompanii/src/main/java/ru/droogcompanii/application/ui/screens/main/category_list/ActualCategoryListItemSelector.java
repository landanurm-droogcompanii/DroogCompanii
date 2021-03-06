package ru.droogcompanii.application.ui.screens.main.category_list;

import android.view.View;

import com.google.common.base.Optional;

import ru.droogcompanii.application.R;

/**
 * Created by ls on 24.03.14.
 */
class ActualCategoryListItemSelector implements CategoryListItemSelector {
    private static final int SELECTED_BACKGROUND = R.color.backgroundOfCheckedCategoryListItem;
    private static final int UNSELECTED_BACKGROUND = android.R.color.transparent;

    private Optional<View> lastSelectedItem;

    public ActualCategoryListItemSelector() {
        lastSelectedItem = Optional.absent();
    }

    public void select(View itemView) {
        if (lastSelectedItem.isPresent()) {
            lastSelectedItem.get().setBackgroundResource(UNSELECTED_BACKGROUND);
        }
        itemView.setBackgroundResource(SELECTED_BACKGROUND);
        lastSelectedItem = Optional.of(itemView);
    }

    public void unselect(View itemView) {
        itemView.setBackgroundResource(UNSELECTED_BACKGROUND);
        if (lastSelectedItem.isPresent() && areAtTheSamePosition(lastSelectedItem.get(), itemView)) {
            lastSelectedItem.get().setBackgroundResource(UNSELECTED_BACKGROUND);
            lastSelectedItem = Optional.absent();
        }
    }

    private boolean areAtTheSamePosition(View itemView1, View itemView2) {
        return (CategoryListAdapter.PositionUtil.getPosition(itemView1) ==
                CategoryListAdapter.PositionUtil.getPosition(itemView2));
    }
}
