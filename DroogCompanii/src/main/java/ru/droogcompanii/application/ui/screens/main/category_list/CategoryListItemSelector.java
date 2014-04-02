package ru.droogcompanii.application.ui.screens.main.category_list;

import android.view.View;

/**
 * Created by ls on 02.04.14.
 */
interface CategoryListItemSelector {
    void select(View itemView);
    void unselect(View itemView);
}
