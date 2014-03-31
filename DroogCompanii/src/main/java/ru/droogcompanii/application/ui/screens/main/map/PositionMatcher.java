package ru.droogcompanii.application.ui.screens.main.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.common.base.Optional;

import ru.droogcompanii.application.util.Objects;

/**
 * Created by ls on 17.03.14.
 */
class PositionMatcher {
    private boolean isMet;
    private Optional<LatLng> positionToMet;

    public PositionMatcher(Optional<LatLng> positionToMet) {
        this.positionToMet = positionToMet;
        isMet = !positionToMet.isPresent();
    }

    public void makeOut(LatLng position) {
        if (!isMet && Objects.equals(position, positionToMet.get())) {
            isMet = true;
        }
    }

    public boolean isMet() {
        return isMet;
    }
}
