package ru.droogcompanii.application.ui.fragment.search_result_list.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.fragment.search_result_list.SearchResultListFragment;
import ru.droogcompanii.application.util.Objects;

/**
 * Created by ls on 11.03.14.
 */
public class SearchResultListAdapter extends ArrayAdapter<SearchResultListItem> {

    public SearchResultListAdapter(Context context, SearchResultListFragment.Input input) {
        super(context, R.layout.empty_frame_layout, InputToItemsConvertor.convert(input));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchResultListItem item = getItem(position);
        View itemView = (convertView != null) ? convertView : inflateEmptyFrameLayout();
        initItemView(item, (FrameLayout) itemView);
        return itemView;
    }

    private void initItemView(SearchResultListItem item, FrameLayout containerOfItemView) {
        Object newTag = item.getClass();
        Object previousTag = containerOfItemView.getTag();
        View itemView;
        if (!Objects.equals(newTag, previousTag)) {
            containerOfItemView.setTag(newTag);
            containerOfItemView.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            itemView = inflater.inflate(item.getLayoutId(), null);
            containerOfItemView.addView(itemView);
        } else {
            itemView = containerOfItemView.getChildAt(0);
        }
        item.init(itemView);
    }

    private View inflateEmptyFrameLayout() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        return layoutInflater.inflate(R.layout.empty_frame_layout, null);
    }
}
