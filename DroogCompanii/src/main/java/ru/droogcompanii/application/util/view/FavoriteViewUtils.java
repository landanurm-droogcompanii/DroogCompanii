package ru.droogcompanii.application.util.view;

import android.content.Context;
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

    public void init(CheckBox checkBox, final int partnerId) {
        boolean isFavorite = favoriteDBUtils.isFavorite(partnerId);
        checkBox.setChecked(isFavorite);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                favoriteDBUtils.setFavorite(partnerId, checked);
            }
        });
    }
}
