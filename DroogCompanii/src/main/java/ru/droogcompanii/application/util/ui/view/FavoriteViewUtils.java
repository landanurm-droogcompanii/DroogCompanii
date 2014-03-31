package ru.droogcompanii.application.util.ui.view;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.FavoriteDBUtils;

/**
 * Created by ls on 19.02.14.
 */
public class FavoriteViewUtils {
    private final FavoriteDBUtils favoriteDBUtils;

    public FavoriteViewUtils(Context context) {
        favoriteDBUtils = new FavoriteDBUtils(context);
    }

    public void init(final CheckBox checkBox, final int partnerId) {
        if (!checkBox.isEnabled()) {
            return;
        }
        blockCheckBoxAndRun(checkBox, new Runnable() {
            @Override
            public void run() {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        return null;
                    }
                }.execute();
                boolean isFavorite = favoriteDBUtils.isFavorite(partnerId);
                checkBox.setChecked(isFavorite);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, final boolean checkedIsFavorite) {
                        blockCheckBoxAndRun(checkBox, new Runnable() {
                            @Override
                            public void run() {
                                favoriteDBUtils.setFavorite(partnerId, checkedIsFavorite);
                            }
                        });
                    }
                });
            }
        });
    }

    private void blockCheckBoxAndRun(CheckBox checkBox, Runnable runnable) {
        checkBox.setEnabled(false);
        runnable.run();
        checkBox.setEnabled(true);
    }
}
