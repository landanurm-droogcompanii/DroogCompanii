package ru.droogcompanii.application.ui.activity.main_screen_2.map.clicked_position_helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import ru.droogcompanii.application.R;

/**
 * Created by ls on 25.03.14.
 */
class EmptyMapWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Context context = null;

    public EmptyMapWindowAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return layoutInflater.inflate(R.layout.empty_frame_layout, null);
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
