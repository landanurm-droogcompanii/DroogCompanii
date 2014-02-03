package ru.droogcompanii.application.ui.fragment.partner_points_info_panel_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnerPointsReader;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnersReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.activity.partner_activity.PartnerActivity;
import ru.droogcompanii.application.ui.helpers.Caller;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.util.ListUtils;

/**
 * Created by ls on 22.01.14.
 */
public class PartnerPointsInfoPanelFragment extends android.support.v4.app.Fragment {
    private static final int NO_INDEX = -1;

    private boolean visible;
    private int indexOfCurrentPartnerPoint;
    private List<PartnerPoint> partnerPoints;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_partner_points_info_panel, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initCatchingOfTouchEvents();

        if (savedInstanceState == null) {
            initStateByDefault();
        } else {
            restoreStateFrom(savedInstanceState);
        }
    }

    private void initCatchingOfTouchEvents() {
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
        final PartnerPoint partnerPoint = partnerPoints.get(indexOfCurrentPartnerPoint);
        setText(R.id.titleTextView, partnerPoint.title);
        setText(R.id.addressTextView, partnerPoint.address);
        setText(R.id.paymentMethodsTextView, partnerPoint.paymentMethods);
        setPhones(partnerPoint);
        setRouteButton(partnerPoint);
        setWorkingHoursIndicator(partnerPoint);
        setGoToPartnerButton(partnerPoint);
    }

    private void setText(int idOfTextView, String text) {
        TextView textView = (TextView) findViewById(idOfTextView);
        textView.setText(text);
    }

    private void setPhones(PartnerPoint partnerPoint) {
        View phoneButton = findViewById(R.id.phoneButton);
        final List<String> phones = partnerPoint.phones;
        if (phones.isEmpty()) {
            phoneButton.setVisibility(View.INVISIBLE);
        } else {
            phoneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onPhoneButtonClick(phones);
                }
            });
        }
        Toast.makeText(getActivity(), "Phones: " + phones.size(), Toast.LENGTH_LONG).show();
    }

    private void onPhoneButtonClick(List<String> phones) {
        if (phones.size() == 1) {
            String phone = phones.get(0);
            Caller.call(getActivity(), phone);
        } else {
            showMultiPhonesDialogFragment(phones);
        }
    }

    private void showMultiPhonesDialogFragment(List<String> phones) {
        DialogFragment dialogFragment = new MultiPhonesCallerDialogFragment();
        dialogFragment.setArguments(prepareArguments(phones));
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        dialogFragment.show(fragmentManager, "MultiPhonesCallerDialogFragment");
    }

    private static Bundle prepareArguments(List<String> phones) {
        Bundle args = new Bundle();
        args.putSerializable(Keys.phones, (Serializable) phones);
        return args;
    }

    private void setRouteButton(final PartnerPoint partnerPoint) {
        findViewById(R.id.routeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRouteTo(partnerPoint);
            }
        });
    }

    private void showRouteTo(PartnerPoint destination) {
        startActivity(RouteIntentMaker.makeIntentRouteTo(destination));
    }

    private void setWorkingHoursIndicator(PartnerPoint partnerPoint) {
        View indicator = findViewById(R.id.workingHoursIndicator);
        if (isOpened(partnerPoint)) {
            indicator.setBackgroundResource(R.color.opened_indicator_color);
        } else {
            indicator.setBackgroundResource(R.color.closed_indicator_color);
        }
    }

    private static boolean isOpened(PartnerPoint partnerPoint) {
        return isOpenedNow(partnerPoint);
    }

    private static boolean isOpenedNow(PartnerPoint partnerPoint) {
        Calendar now = Calendar.getInstance();
        return partnerPoint.workingHours.includes(now);
    }

    private void setGoToPartnerButton(final PartnerPoint partnerPoint) {
        findViewById(R.id.goToPartnerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPartnerOf(partnerPoint);
            }
        });
    }

    private void goToPartnerOf(PartnerPoint partnerPoint) {
        Context context = getActivity();

        PartnersReader partnersReader = new PartnersReader(context);
        Partner partner = partnersReader.getPartnerOf(partnerPoint);

        PartnerPointsReader partnerPointsReader = new PartnerPointsReader(context);
        List<PartnerPoint> partnerPoints = partnerPointsReader.getPartnerPointsOf(partner);
        ListUtils.moveElementAtFirstPosition(partnerPoint, partnerPoints);

        PartnerActivity.start(context, partner, partnerPoints);
    }
}
