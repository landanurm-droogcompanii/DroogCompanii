package ru.droogcompanii.application.ui.activity.main_screen_2;

import com.google.android.gms.maps.model.LatLng;
import com.google.common.base.Optional;

import ru.droogcompanii.application.util.Objects;

/**
 * Created by ls on 17.03.14.
 */
class ClickedPositionHelperUpdater {
    private final ClickedPositionHelper clickedPositionHelper;
    private boolean isNeedToRemoveClickedPosition;
    private Optional<LatLng> clickedPosition;

    public ClickedPositionHelperUpdater(ClickedPositionHelper clickedPositionHelper) {
        this.clickedPositionHelper = clickedPositionHelper;
        initStartState();
    }

    private void initStartState() {
        clickedPosition = clickedPositionHelper.getOptionalClickedPosition();
        isNeedToRemoveClickedPosition = clickedPosition.isPresent();
    }

    public void add(LatLng position) {
        if (!isNeedToRemoveClickedPosition) {
            return;
        }
        if (Objects.equals(Optional.of(position), clickedPosition)) {
            isNeedToRemoveClickedPosition = false;
        }
    }

    public boolean isNeedToRemoveClickedPosition() {
        return isNeedToRemoveClickedPosition;
    }

    public void reset() {
        initStartState();
    }
}
