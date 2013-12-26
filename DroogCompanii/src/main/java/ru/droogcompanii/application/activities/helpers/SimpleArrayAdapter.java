package ru.droogcompanii.application.activities.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Leonid on 03.12.13.
 */
public class SimpleArrayAdapter<T> extends ArrayAdapter<T> {

    public static interface ItemToTitleConvertor<T> {
        String getTitle(T item);
    }

    private final ItemToTitleConvertor<T> itemToTitleConvertor;

    public SimpleArrayAdapter(Context context, List<T> items,
                              ItemToTitleConvertor<T> itemToTitleConvertor) {
        super(context, android.R.layout.simple_list_item_1, items);
        this.itemToTitleConvertor = itemToTitleConvertor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = (convertView != null) ? convertView : getSimpleListItemView();
        initItemViewByPosition(itemView, position);
        return itemView;
    }

    private View getSimpleListItemView() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        return layoutInflater.inflate(android.R.layout.simple_list_item_1, null);
    }

    private void initItemViewByPosition(View itemView, int position) {
        T item = getItem(position);
        if (item != null) {
            setTitle(itemView, itemToTitleConvertor.getTitle(item));
        } else {
            setTitle(itemView, "");
        }
    }

    private void setTitle(View itemView, String title) {
        TextView textView = (TextView) itemView.findViewById(android.R.id.text1);
        textView.setText(title);
    }
}