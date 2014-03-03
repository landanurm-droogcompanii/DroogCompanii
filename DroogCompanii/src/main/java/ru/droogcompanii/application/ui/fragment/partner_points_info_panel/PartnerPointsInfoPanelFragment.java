package ru.droogcompanii.application.ui.fragment.partner_points_info_panel;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerPointsReader;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnersReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.activity.partner_details.PartnerDetailsActivity;
import ru.droogcompanii.application.ui.util.IsFavoriteViewUtils;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.util.ListUtils;
import ru.droogcompanii.application.ui.util.Router;
import ru.droogcompanii.application.ui.util.caller.CallerHelper;

/**
 * Created by ls on 22.01.14.
 */
public class PartnerPointsInfoPanelFragment extends android.support.v4.app.Fragment {
    private static final int NO_INDEX = -1;

    private boolean visible;
    private int indexOfCurrentPartnerPoint;
    private List<PartnerPoint> partnerPoints;
    private IsFavoriteViewUtils isFavoriteViewUtils;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        isFavoriteViewUtils = new IsFavoriteViewUtils(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_partner_points_info_panel, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initCatchingTouchEvents();

        if (savedInstanceState == null) {
            initStateByDefault();
        } else {
            restoreStateFrom(savedInstanceState);
        }
    }

    private void initCatchingTouchEvents() {
        getView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

    private void initStateByDefault() {
        partnerPoints = new ArrayList<PartnerPoint>();
        indexOfCurrentPartnerPoint = NO_INDEX;
        hide();
    }

    private void restoreStateFrom(Bundle savedInstanceState) {
        partnerPoints = (List<PartnerPoint>) savedInstanceState.getSerializable(Keys.partnerPoints);
        indexOfCurrentPartnerPoint = savedInstanceState.getInt(Keys.indexOfCurrentPartnerPoint);
        restoreVisibilityFrom(savedInstanceState);
    }

    private void restoreVisibilityFrom(Bundle savedInstanceState) {
        boolean isNeedToShow = savedInstanceState.getBoolean(Keys.visible);
        if (isNeedToShow) {
            show();
        } else {
            hide();
        }
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
        outState.putSerializable(Keys.partnerPoints, (Serializable) partnerPoints);
        outState.putInt(Keys.indexOfCurrentPartnerPoint, indexOfCurrentPartnerPoint);
        outState.putBoolean(Keys.visible, visible);
    }

    public void show() {
        visible = true;
        getView().setVisibility(View.VISIBLE);
        updateUI();
    }

    public void hide() {
        visible = false;
        getView().setVisibility(View.INVISIBLE);
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
        Calendar now = Calendar.getInstance();
        return partnerPoint.getWorkingHours().includes(now);
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
        PartnerDetailsActivity.start(getActivity(),
                new PartnerAndPartnerPointsProviderImpl(partnerPoint));
    }

    private static class PartnerAndPartnerPointsProviderImpl
            implements PartnerDetailsActivity.PartnerAndPartnerPointsProvider, Serializable {

        private final PartnerPoint partnerPoint;

        PartnerAndPartnerPointsProviderImpl(PartnerPoint partnerPoint) {
            this.partnerPoint = partnerPoint;
        }

        @Override
        public Partner getPartner(Context context) {
            PartnersReader partnersReader = new PartnersReader(context);
            return partnersReader.getPartnerOf(partnerPoint);
        }

        @Override
        public List<PartnerPoint> getPartnerPoints(Context context) {
            PartnerPointsReader partnerPointsReader = new PartnerPointsReader(context);
            Partner partner = getPartner(context);
            List<PartnerPoint> partnerPoints = partnerPointsReader.getPartnerPointsOf(partner);
            ListUtils.moveElementAtFirstPosition(partnerPoint, partnerPoints);
            return partnerPoints;
        }
    }

    private void updateIsFavorite() {
        CheckBox checkBox = (CheckBox) findViewById(R.id.isFavorite);
        isFavoriteViewUtils.init(checkBox, getCurrentPartnerPoint().getPartnerId());
    }
}
