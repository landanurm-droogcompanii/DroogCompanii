package ru.droogcompanii.application.view.fragment.partner_points_info_panel_fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.util.ListUtils;
import ru.droogcompanii.application.view.activity_3.partner_activity.PartnerActivity;

/**
 * Created by ls on 22.01.14.
 */
public class PartnerPointsInfoPanelFragment extends android.support.v4.app.Fragment {
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
        indexOfCurrentPartnerPoint = -1;
        hide();
    }

    private void restoreStateFrom(Bundle savedInstanceState) {
        partnerPoints = (List<PartnerPoint>) savedInstanceState.getSerializable(Keys.partnerPoints);
        indexOfCurrentPartnerPoint = savedInstanceState.getInt(Keys.indexOfCurrentPartnerPoint);
        restoreVisibilityFrom(savedInstanceState);
    }

    private void restoreVisibilityFrom(Bundle savedInstanceState) {
        boolean needToShow = savedInstanceState.getBoolean(Keys.visible);
        if (needToShow) {
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

    public void setPartnerPoints(Collection<PartnerPoint> partnerPoints) {
        if (partnerPoints == null) {
            throw new IllegalArgumentException("partnerPoints should be not null");
        }
        if (this.partnerPoints.equals(partnerPoints)) {
            return;
        }
        List<PartnerPoint> oldPartnerPoints = this.partnerPoints;
        List<PartnerPoint> newPartnerPoints = new ArrayList<PartnerPoint>(partnerPoints);
        this.partnerPoints = newPartnerPoints;
        this.indexOfCurrentPartnerPoint = defineIndex(oldPartnerPoints, newPartnerPoints);
        show();
    }

    private int defineIndex(List<PartnerPoint> oldPartnerPoints, List<PartnerPoint> newPartnerPoints) {
        if ((indexOfCurrentPartnerPoint == -1) || !visible) {
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
        if (indexOutOfBounds()) {
            throw new IllegalStateException(
                    "Index of current partner point is out of bounds: " + indexOfCurrentPartnerPoint
            );
        }
    }

    private boolean indexOutOfBounds() {
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

    private boolean isAbleToGoToPrevious() {
        return (indexOfCurrentPartnerPoint > 0);
    }

    private boolean isAbleToGoToNext() {
        return (indexOfCurrentPartnerPoint < partnerPoints.size() - 1);
    }

    private void setEnabled(int idOfView, boolean enabled) {
        findViewById(idOfView).setEnabled(enabled);
    }

    private View findViewById(int idOfView) {
        return getView().findViewById(idOfView);
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
        setWorkingHoursIndicator(partnerPoint);
        findViewById(R.id.goToPartnerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPartner(partnerPoint);
            }
        });
    }

    private void setText(int idOfTextView, String text) {
        TextView textView = (TextView) findViewById(idOfTextView);
        textView.setText(text);
    }

    private void setPhones(PartnerPoint partnerPoint) {
        ViewGroup containerOfPhones = (ViewGroup) findViewById(R.id.containerOfPhones);
        containerOfPhones.removeAllViews();
        for (String phone : partnerPoint.phones) {
            containerOfPhones.addView(preparePhoneButton(phone));
        }
    }

    private Button preparePhoneButton(final String phone) {
        Button phoneButton = new Button(getActivity());
        phoneButton.setText(phone);
        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNeedToCall(phone);
            }
        });
        return phoneButton;
    }

    private void onNeedToCall(String phone) {
        String formattedPhone = PhoneNumberUtils.formatNumber(phone);
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + formattedPhone));
        startActivity(intent);
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

    private void goToPartner(PartnerPoint partnerPoint) {
        Context context = getActivity();

        PartnersReader partnersReader = new PartnersReader(context);
        Partner partner = partnersReader.getPartnerOf(partnerPoint);

        PartnerPointsReader partnerPointsReader = new PartnerPointsReader(context);
        List<PartnerPoint> partnerPoints = partnerPointsReader.getPartnerPointsOf(partner);

        ListUtils.moveElementAtFirstPosition(partnerPoint, partnerPoints);
        PartnerActivity.start(context, partner, partnerPoints);
    }
}
