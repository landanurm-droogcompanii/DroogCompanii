package ru.droogcompanii.application.ui.main_screen_2.details;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.common.base.Optional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerPointsReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.activity.partner_details.PartnerDetailsActivity;
import ru.droogcompanii.application.ui.fragment.partner_points_info_panel.WorkingHoursIndicatorUpdater;
import ru.droogcompanii.application.ui.util.view.FavoriteViewUtils;
import ru.droogcompanii.application.ui.util.workers.Router;
import ru.droogcompanii.application.ui.util.StateManager;
import ru.droogcompanii.application.ui.util.workers.caller.CallerHelper;
import ru.droogcompanii.application.util.CalendarUtils;
import ru.droogcompanii.application.util.Objects;

/**
 * Created by ls on 19.03.14.
 */
public class PartnerPointDetailsFragment extends android.support.v4.app.Fragment {

    private static final int NO_INDEX = -1;

    private static class Key {
        public static final String PARTNER_POINTS = "KEY_PARTNER_POINTS";
        public static final String INDEX_OF_CURRENT_PARTNER_POINT = "KEY_INDEX_OF_CURRENT_PARTNER_POINT";
        public static final String VISIBLE = "KEY_VISIBLE";
        public static final String IDS = "KEY_IDS";
    }

    private boolean isVisible;
    private int indexOfCurrentPartnerPoint;
    private List<Integer> ids;
    private List<Optional<PartnerPoint>> partnerPoints;
    private FavoriteViewUtils favoriteViewUtils;

    private final StateManager STATE_MANAGER = new StateManager() {
        @Override
        public void initStateByDefault() {
            ids = new ArrayList<Integer>();
            partnerPoints = new ArrayList<Optional<PartnerPoint>>();
            indexOfCurrentPartnerPoint = NO_INDEX;
            hide();
        }

        @Override
        public void restoreState(Bundle savedInstanceState) {
            ids = (List<Integer>) savedInstanceState.getSerializable(Key.IDS);
            partnerPoints = (List<Optional<PartnerPoint>>) savedInstanceState.getSerializable(Key.PARTNER_POINTS);
            indexOfCurrentPartnerPoint = savedInstanceState.getInt(Key.INDEX_OF_CURRENT_PARTNER_POINT);
            restoreVisibilityFrom(savedInstanceState);
        }

        @Override
        public void saveState(Bundle outState) {
            outState.putSerializable(Key.IDS, (Serializable) ids);
            outState.putSerializable(Key.PARTNER_POINTS, (Serializable) partnerPoints);
            outState.putInt(Key.INDEX_OF_CURRENT_PARTNER_POINT, indexOfCurrentPartnerPoint);
            outState.putBoolean(Key.VISIBLE, isVisible);
        }
    };

    private void restoreVisibilityFrom(Bundle savedInstanceState) {
        boolean isNeedToShow = savedInstanceState.getBoolean(Key.VISIBLE);
        if (isNeedToShow) {
            show();
        } else {
            hide();
        }
    }

    public void show() {
        isVisible = true;
        getView().setVisibility(View.VISIBLE);
        updateUI();
    }

    public void hide() {
        isVisible = false;
        getView().setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        favoriteViewUtils = new FavoriteViewUtils(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_partner_points_info_panel, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        STATE_MANAGER.initState(savedInstanceState);

        initCatchingTouchEvents();
    }

    private void updateUI() {
        checkIndexOfCurrentPartnerPoint();
        updateNavigationPanel();
        updatePartnerPointInfo();
    }

    private void checkIndexOfCurrentPartnerPoint() {
        if (isIndexOutOfBounds()) {
            throw new IllegalStateException(
                    "Index of current partner point is out of bounds: " + indexOfCurrentPartnerPoint
            );
        }
    }

    private boolean isIndexOutOfBounds() {
        return (indexOfCurrentPartnerPoint < 0) || (indexOfCurrentPartnerPoint >= partnerPoints.size());
    }

    private void updateNavigationPanel() {
        updateNavigationArrows();
        updateNavigationText();
    }

    private void updateNavigationArrows() {
        setEnabled(R.id.previousButton, isAbleToGoToPrevious());
        setEnabled(R.id.nextButton, isAbleToGoToNext());
        findViewById(R.id.previousButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPreviousPartnerPoint();
            }
        });
        findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNextPartnerPoint();
            }
        });
    }

    private void setEnabled(int idOfView, boolean enabled) {
        findViewById(idOfView).setEnabled(enabled);
    }

    private View findViewById(int idOfView) {
        return getView().findViewById(idOfView);
    }

    private boolean isAbleToGoToPrevious() {
        return (indexOfCurrentPartnerPoint > 0);
    }

    private boolean isAbleToGoToNext() {
        return (indexOfCurrentPartnerPoint < partnerPoints.size() - 1);
    }

    private void goToPreviousPartnerPoint() {
        --indexOfCurrentPartnerPoint;
        updateUI();
    }

    private void goToNextPartnerPoint() {
        ++indexOfCurrentPartnerPoint;
        updateUI();
    }

    private void updateNavigationText() {
        TextView navigationTextView = (TextView) findViewById(R.id.navigationTextView);
        navigationTextView.setText(prepareNavigationText());
    }

    private String prepareNavigationText() {
        int currentIndexBaseOnOne = indexOfCurrentPartnerPoint + 1;
        int totalNumber = partnerPoints.size();
        return currentIndexBaseOnOne + "/" + totalNumber;
    }

    private void updatePartnerPointInfo() {
        updateTextInfo();
        updatePhones();
        updateRouteButton();
        updateWorkingHoursIndicator();
        updateGoToPartnerButton();
        updateIsFavorite();
    }

    private void updateTextInfo() {
        PartnerPoint currentPartnerPoint = getCurrentPartnerPoint();
        setText(R.id.titleTextView, currentPartnerPoint.getTitle());
        setText(R.id.addressTextView, currentPartnerPoint.getAddress());
        setText(R.id.paymentMethodsTextView, currentPartnerPoint.getPaymentMethods());
    }

    private PartnerPoint getCurrentPartnerPoint() {
        Optional<PartnerPoint> currentPartnerPoint = partnerPoints.get(indexOfCurrentPartnerPoint);
        if (!currentPartnerPoint.isPresent()) {
            currentPartnerPoint = Optional.of(
                    loadPartnerPointByIndex(indexOfCurrentPartnerPoint)
            );
            partnerPoints.set(indexOfCurrentPartnerPoint, currentPartnerPoint);
        }
        return currentPartnerPoint.get();
    }

    private PartnerPoint loadPartnerPointByIndex(int index) {
        int idOfPartnerPoint = ids.get(index);
        PartnerPointsReader reader = new PartnerPointsReader(getActivity());
        return reader.getPartnerPointById(idOfPartnerPoint);
    }

    private void setText(int idOfTextView, String text) {
        TextView textView = (TextView) findViewById(idOfTextView);
        textView.setText(text);
    }

    private void updatePhones() {
        View phoneButton = findViewById(R.id.phoneButton);
        CallerHelper callerHelper = new CallerHelper(getActivity());
        callerHelper.initPhoneButton(phoneButton, getCurrentPartnerPoint());
    }

    private void updateRouteButton() {
        final Router router = new Router(getActivity());
        findViewById(R.id.routeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                router.routeTo(getCurrentPartnerPoint());
            }
        });
    }

    private void updateWorkingHoursIndicator() {
        View indicator = findViewById(R.id.workingHoursIndicator);
        WorkingHoursIndicatorUpdater.update(indicator, isOpened(getCurrentPartnerPoint()));
    }

    private static boolean isOpened(PartnerPoint partnerPoint) {
        return isOpenedNow(partnerPoint);
    }

    private static boolean isOpenedNow(PartnerPoint partnerPoint) {
        return partnerPoint.getWorkingHours().includes(CalendarUtils.now());
    }

    private void updateGoToPartnerButton() {
        findViewById(R.id.goToPartnerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPartnerOf(getCurrentPartnerPoint());
            }
        });
    }

    private void goToPartnerOf(PartnerPoint partnerPoint) {
        PartnerDetailsActivity.start(getActivity(), partnerPoint);
    }

    private void updateIsFavorite() {
        CheckBox checkBox = (CheckBox) findViewById(R.id.isFavorite);
        favoriteViewUtils.init(checkBox, getCurrentPartnerPoint().getPartnerId());
    }

    private void initCatchingTouchEvents() {
        getView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

    public void setPartnerPoints(List<PartnerPoint> partnerPointsToSet) {
        if (partnerPointsToSet == null) {
            throw new IllegalArgumentException("partnerPointsToSet should be not null");
        }
        if (isAlreadySetting(partnerPointsToSet)) {
            return;
        }
        update(prepareIds(partnerPointsToSet), prepareAbsentPartnerPoints(partnerPointsToSet));
        show();
    }

    private boolean isAlreadySetting(List<PartnerPoint> partnerPointsToSet) {
        if (partnerPointsToSet.size() != partnerPoints.size()) {
            return false;
        }
        for (int i = 0; i < partnerPointsToSet.size(); ++i) {
            Optional<PartnerPoint> eachSetting = partnerPoints.get(i);
            Optional<PartnerPoint> eachToSet = Optional.of(partnerPointsToSet.get(i));
            if (!Objects.equals(eachSetting, eachToSet)) {
                return false;
            }
        }
        return true;
    }

    private void update(List<Integer> newIds, List<Optional<PartnerPoint>> newPartnerPoints) {
        indexOfCurrentPartnerPoint = defineIndex(ids, newIds);
        ids = newIds;
        partnerPoints = newPartnerPoints;
    }

    private List<Integer> prepareIds(List<PartnerPoint> partnerPointsToSet) {
        List<Integer> result = new ArrayList<Integer>(partnerPointsToSet.size());
        for (PartnerPoint each : partnerPointsToSet) {
            result.add(each.getId());
        }
        return result;
    }

    private List<Optional<PartnerPoint>> prepareAbsentPartnerPoints(List<PartnerPoint> partnerPointsToSet) {
        int size = partnerPointsToSet.size();
        List<Optional<PartnerPoint>> result = new ArrayList<Optional<PartnerPoint>>(size);
        for (int i = 0; i < size; ++i) {
            result.add(Optional.<PartnerPoint>absent());
        }
        return result;
    }

    private int defineIndex(List<Integer> oldIds, List<Integer> newIds) {
        if ((indexOfCurrentPartnerPoint == NO_INDEX) || !isVisible) {
            return 0;
        }
        Integer idOfDisplayedPartnerPoint = oldIds.get(indexOfCurrentPartnerPoint);
        int indexOfDisplayedPartnerPointInNewList = newIds.indexOf(idOfDisplayedPartnerPoint);
        if (indexOfDisplayedPartnerPointInNewList == -1) {
            return 0;
        }
        return indexOfDisplayedPartnerPointInNewList;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisible) {
            updateIsFavorite();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        STATE_MANAGER.saveState(outState);
    }

}
