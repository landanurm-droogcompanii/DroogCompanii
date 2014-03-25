package ru.droogcompanii.application.ui.main_screen_2.category_list;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by ls on 24.03.14.
 */
class CategoryListAdapter extends ArrayAdapter<ListItemHelper> {

    private final CategoryListFragment fragment;

    public CategoryListAdapter(CategoryListFragment fragment, List<ListItemHelper> items) {
        super(fragment.getActivity(), ListItemHelperBuilder.ROW_LAYOUT_ID, items);
        this.fragment = fragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemHelper itemHelper = getItem(position);
        View itemView = itemHelper.makeView(getContext(), convertView);
        Integer tag = position;
        itemView.setTag(tag);
        updateSelection(itemView, position);
        return itemView;
    }

    private void updateSelection(View itemView, int position) {
        if (position == fragment.getCurrentSelection()) {
            fragment.getSelector().select(itemView);
        } else {
            fragment.getSelector().unselect(itemView);
        }
    }

    public static boolean areAtTheSamePosition(View itemView1, View itemView2) {
        int position1 = (Integer) itemView1.getTag();
        int position2 = (Integer) itemView2.getTag();
        return position1 == position2;
    }
}
