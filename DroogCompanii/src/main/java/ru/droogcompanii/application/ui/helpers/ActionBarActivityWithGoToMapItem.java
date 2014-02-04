package ru.droogcompanii.application.ui.helpers;

import android.view.Menu;
import android.view.MenuItem;

import ru.droogcompanii.application.R;

/**
 * Created by ls on 04.02.14.
 */
public abstract class ActionBarActivityWithGoToMapItem extends ActionBarActivityWithUpButton {
    private MenuItem goToMapItem;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_with_go_to_map_item, menu);
        goToMapItem = menu.findItem(R.id.action_go_to_map);
        goToMapItem.setVisible(isGoToMapItemVisible());
        return true;
    }

    protected abstract boolean isGoToMapItemVisible();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_go_to_map:
                onNeedToGoToMap();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract void onNeedToGoToMap();

    protected void updateGoToMapItemVisible() {
        if (goToMapItem != null) {
            goToMapItem.setVisible(isGoToMapItemVisible());
        }
    }

    @Override
    protected boolean isUpButtonEnabled() {
        return true;
    }

}
