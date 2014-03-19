package ru.droogcompanii.application.ui.activity.main_screen_2;

import android.support.v4.widget.DrawerLayout;
import android.view.View;

import ru.droogcompanii.application.ui.util.ViewResizer;

/**
 * Created by ls on 19.03.14.
 */
public class NavigationDrawerIndentHelper {

    private final DrawerLayout drawerLayout;
    private final View indentView;

    public NavigationDrawerIndentHelper(DrawerLayout drawerLayout, View indentView) {
        this.drawerLayout = drawerLayout;
        this.indentView = indentView;
    }

    public void updateIndent() {
        if (drawerLayout.getVisibility() != View.VISIBLE) {
            ViewResizer.setWidth(indentView, 0);
        } else {
            ViewResizer.setWidth(indentView, drawerLayout.getWidth());
        }
    }
}
