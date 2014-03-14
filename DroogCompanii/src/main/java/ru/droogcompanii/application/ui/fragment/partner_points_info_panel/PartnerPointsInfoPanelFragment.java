package ru.droogcompanii.application.ui.fragment.partner_points_info_panel;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.activity.partner_details.PartnerDetailsActivity;
import ru.droogcompanii.application.ui.util.FavoriteViewUtils;
import ru.droogcompanii.application.ui.util.Router;
import ru.droogcompanii.application.ui.util.caller.CallerHelper;
import ru.droogcompanii.application.util.CalendarUtils;

/**
 * Created by ls on 22.01.14.
 */
public class PartnerPointsInfoPanelFragment extends android.support.v4.app.Fragment {

    private static final int NO_INDEX = -1;

    private static final String KEY_PARTNER_POINTS = "KEY_PARTNER_POINTS";
    private static final String KEY_INDEX_OF_CURRENT_PARTNER_POINT = "KEY_INDEX_OF_CURRENT_PARTNER_POINT";
    private static final String KEY_VISIBLE = "KEY_VISIBLE";

    private boolean visible;
    private int indexOfCurrentPartnerPoint;
    private List<PartnerPoint> partnerPoints;
    private FavoriteViewUtils favoriteViewUtils;

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

        if (savedInstanceState == null) {
            initStateByDefault();
        } else {
            restoreStateFrom(savedInstanceState);
        }

        initCatchingTouchEvents();
    }

    private void initStateByDefault() {
        partnerPoints = new ArrayList<PartnerPoint>();
        indexOfCurrentPartnerPoint = NO_INDEX;
        hide();
    }

    public void hide() {
        visible = false;
        getView().setVisibility(View.INVISIBLE);
    }

    private void restoreStateFrom(Bundle savedInstanceState) {
        partnerPoints = (List<PartnerPoint>) savedInstanceState.getSerializable(KEY_PARTNER_POINTS);
        indexOfCurrentPartnerPoint = savedInstanceState.getInt(KEY_INDEX_OF_CURRENT_PARTNER_POINT);
        restoreVisibilityFrom(savedInstanceState);
    }

    private void restoreVisibilityFrom(Bundle savedInstanceState) {
        boolean isNeedToShow = savedInstanceState.getBoolean(KEY_VISIBLE);
        if (isNeedToShow) {
            show();
        } else {
            hide();
        }
    }

    public void show() {
        visible = true;
        getView().setVisibility(View.VISIBLE);
        updateUI();
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
        int indexOfCurrent = indexOfCurrentPartnerPoint + 1;
        int totalNumber = partnerPoints.size();
        return indexOfCurrent + "/" + totalNumber;
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
        return partnerPoints.get(indexOfCurrentPartnerPoint);
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

    public void setPartnerPoints(Collection<PartnerPoint> partnerPointsToSet) {
        if (partnerPointsToSet == null) {
            throw new IllegalArgumentException("partnerPointsToSet should be not null");
        }
        if (partnerPoints.equals(partnerPointsToSet)) {
            return;
        }
        List<PartnerPoint> newPartnerPoints = new ArrayList<PartnerPoint>(partnerPointsToSet);
        setPartnerPoints(partnerPoints, newPartnerPoints);
        show();
    }

    private void setPartnerPoints(List<PartnerPoint> oldPartnerPoints, List<PartnerPoint> newPartnerPoints) {
        indexOfCurrentPartnerPoint = defineIndex(oldPartnerPoints, newPartnerPoints);
        partnerPoints = newPartnerPoints;
    }

    private int defineIndex(List<PartnerPoint> oldPartnerPoints, List<PartnerPoint> newPartnerPoints) {
        if ((indexOfCurrentPartnerPoint == NO_INDEX) || !visible) {
            return 0;
        }
        PartnerPoint partnerPointDisplayedNow = oldPartnerPoints.get(indexOfCurrentPartnerPoint);
        int index = newPartnerPoints.indexOf(partnerPointDisplayedNow);
        if (index == -1) {
            return 0;
        }
        return index;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (visible) {
            updateIsFavorite();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveStateInto(outState);
    }

    private void saveStateInto(Bundle outState) {
        outState.putSerializable(KEY_PARTNER_POINTS, (Serializable) partnerPoints);
        outState.putInt(KEY_INDEX_OF_CURRENT_PARTNER_POINT, indexOfCurrentPartnerPoint);
        outState.putBoolean(KEY_VISIBLE, visible);
    }
}
