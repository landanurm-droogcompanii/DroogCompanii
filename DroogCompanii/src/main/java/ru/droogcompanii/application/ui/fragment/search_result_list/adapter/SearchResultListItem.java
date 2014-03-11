package ru.droogcompanii.application.ui.fragment.search_result_list.adapter;

import android.content.Context;
import android.view.View;

/**
 * Created by ls on 11.03.14.
 */
public interface SearchResultListItem {
    int getLayoutId();
    void init(View itemView);
    void onClick(Context context);
}
