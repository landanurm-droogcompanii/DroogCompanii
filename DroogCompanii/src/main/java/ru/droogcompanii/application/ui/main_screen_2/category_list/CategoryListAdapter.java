package ru.droogcompanii.application.ui.main_screen_2.category_list;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import ru.droogcompanii.application.R;

/**
 * Created by ls on 24.03.14.
 */
class CategoryListAdapter extends ArrayAdapter<ListItemHelper> {

    public static final int ROW_LAYOUT_ID = R.layout.view_list_item_category;

    private final CategoryListFragment fragment;

    public CategoryListAdapter(CategoryListFragment fragment, List<ListItemHelper> items) {
        super(fragment.getActivity(), ROW_LAYOUT_ID, items);
        this.fragment = fragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemHelper itemHelper = getItem(position);
        View itemView = itemHelper.makeView(getContext(), convertView);
        Integer tag = position;
        itemView.setTag(tag);
        initSelectionState(itemView, position);
        return itemView;
    }

    private void initSelectionState(View itemView, int position) {
        if (position == fragment.getCurrentSelection()) {
            fragment.getSelector().select(itemView);
        } else {
            fragment.getSelector().unselect(itemView);
        }
    }

    public static int getPositionOfItem(View itemView) {
        return (Integer) itemView.getTag();
    }

    public static boolean areAtTheSamePosition(View itemView1, View itemView2) {
        return getPositionOfItem(itemView1) == getPositionOfItem(itemView2);
    }
}
