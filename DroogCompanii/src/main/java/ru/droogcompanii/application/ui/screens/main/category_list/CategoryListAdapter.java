package ru.droogcompanii.application.ui.screens.main.category_list;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import ru.droogcompanii.application.R;

/**
 * Created by ls on 24.03.14.
 */
class CategoryListAdapter extends ArrayAdapter<ListItemHelper> {

    public static class PositionUtil {

        public static void setPosition(View itemView, int position) {
            Integer tag = position;
            itemView.setTag(tag);
        }

        public static int getPosition(View itemView) {
            return (Integer) itemView.getTag();
        }
    }


    public static final int ROW_LAYOUT_ID = R.layout.view_list_item_category;

    private final CategoryListFragment fragment;
    private final CategoryListItemSelector selector;

    public CategoryListAdapter(CategoryListFragment fragment, List<ListItemHelper> items) {
        super(fragment.getActivity(), ROW_LAYOUT_ID, items);
        this.fragment = fragment;
        this.selector = fragment.getSelector();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemHelper itemHelper = getItem(position);
        View itemView = itemHelper.makeView(getContext(), convertView);
        PositionUtil.setPosition(itemView, position);
        initSelectionState(itemView, position);
        return itemView;
    }

    private void initSelectionState(View itemView, int position) {
        if (isPositionOfCurrentSelection(position)) {
            selector.select(itemView);
        } else {
            selector.unselect(itemView);
        }
    }

    private boolean isPositionOfCurrentSelection(int position) {
        return position == fragment.getCurrentSelection();
    }
}
