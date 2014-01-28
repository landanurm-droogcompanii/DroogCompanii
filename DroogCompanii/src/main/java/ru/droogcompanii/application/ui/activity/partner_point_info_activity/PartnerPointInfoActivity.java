package ru.droogcompanii.application.ui.activity.partner_point_info_activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Calendar;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.working_hours.DateTimeConstants;
import ru.droogcompanii.application.data.working_hours.WorkingHours;
import ru.droogcompanii.application.data.working_hours.day_of_week_to_string_convertor.DayOfWeekToStringConvertor;
import ru.droogcompanii.application.data.working_hours.day_of_week_to_string_convertor.DayOfWeekToStringConvertorProvider;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.ui.helpers.ActionBarActivityWithBackButton;

/**
 * Created by ls on 25.12.13.
 */
public class PartnerPointInfoActivity extends ActionBarActivityWithBackButton {
    
    private static final int[] daysOfWeek = DateTimeConstants.getDaysOfWeek();

    private Partner partner;
    private PartnerPoint partnerPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_point_info);

        if (savedInstanceState == null) {
            prepareData();
        } else {
            restoreDataFrom(savedInstanceState);
        }

        showData();
    }

    private void prepareData() {
        Intent intent = getIntent();
        partner = (Partner) intent.getSerializableExtra(Keys.partner);
        partnerPoint = (PartnerPoint) intent.getSerializableExtra(Keys.partnerPoint);
    }

    private void restoreDataFrom(Bundle savedInstanceState) {
        partner = (Partner) savedInstanceState.getSerializable(Keys.partner);
        partnerPoint = (PartnerPoint) savedInstanceState.getSerializable(Keys.partnerPoint);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.partner, partner);
        outState.putSerializable(Keys.partnerPoint, partnerPoint);
    }

    private void showData() {
        setTitle(partner.fullTitle);
        textViewById(R.id.title).setText(partnerPoint.title);
        textViewById(R.id.address).setText(partnerPoint.address);
        textViewById(R.id.paymentMethods).setText(partnerPoint.paymentMethods);
        showPhones();
        initWorkingHours();
    }

    private TextView textViewById(int id) {
        return (TextView) findViewById(id);
    }

    private void showPhones() {
        if (noPhones()) {
            return;
        }
        LinearLayout containerOfPhones = (LinearLayout) findViewById(R.id.phones);
        for (String phone : partnerPoint.phones) {
            containerOfPhones.addView(preparePhoneView(phone));
        }
    }

    private boolean noPhones() {
        return partnerPoint.phones.isEmpty();
    }

    private View preparePhoneView(final String phone) {
        Button phoneButton = new Button(this);
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

    private void initWorkingHours() {
        final int daysPerWeek = 7;
        for (int indexOfDayOfWeek = 0; indexOfDayOfWeek < daysPerWeek; ++indexOfDayOfWeek) {
            showWorkingHoursRow(indexOfDayOfWeek);
        }
        selectWorkingHoursRow(getIndexOfCurrentDayRow());
    }

    private void showWorkingHoursRow(int indexOfRow) {
        int dayOfWeek = daysOfWeek[indexOfRow];
        initDayTextView(indexOfRow, dayOfWeek);
        initWorkingHoursTextView(indexOfRow, dayOfWeek);
        unselectWorkingHoursRow(indexOfRow);
    }

    private void initDayTextView(int indexOfRow, int dayOfWeek) {
        DayOfWeekToStringConvertor convertor = DayOfWeekToStringConvertorProvider.getCurrentConvertor();
        String nameOfWeekDay = convertor.dayOfWeekToString(dayOfWeek);
        int idOfDayTextView = ResourceIdentifiersProvider.getIdOfDayTextView(indexOfRow);
        TextView dayTextView = textViewById(idOfDayTextView);
        dayTextView.setText(nameOfWeekDay);
    }

    private void initWorkingHoursTextView(int indexOfRow, int dayOfWeek) {
        int idOfWorkingHoursTextView = ResourceIdentifiersProvider.getIdOfWorkingHoursTextView(indexOfRow);
        TextView workingHoursTextView = textViewById(idOfWorkingHoursTextView);
        WorkingHours dayWorkingHours = partnerPoint.workingHours.workingHoursOfDay(dayOfWeek);
        workingHoursTextView.setText(dayWorkingHours.toString());
    }

    private void unselectWorkingHoursRow(int indexOfRow) {
        int transparentColorId = android.R.color.transparent;
        setRowColor(indexOfRow, transparentColorId);
    }

    private void setRowColor(int indexOfRow, int colorId) {
        int idOfIndicatorTextView = ResourceIdentifiersProvider.getIdOfIndicatorTextView(indexOfRow);
        textViewById(idOfIndicatorTextView).setBackgroundResource(colorId);
    }

    private int getIndexOfCurrentDayRow() {
        Calendar now = Calendar.getInstance();
        int currentDayOfWeek = now.get(Calendar.DAY_OF_WEEK);
        return Arrays.binarySearch(daysOfWeek, currentDayOfWeek);
    }

    private void selectWorkingHoursRow(int indexOfRow) {
        setRowColor(indexOfRow, getColorIdOfCurrentDayIndicator());
    }

    private int getColorIdOfCurrentDayIndicator() {
        return partnerPointIsOpened()
                ? R.color.opened_indicator_color
                : R.color.closed_indicator_color;
    }

    private boolean partnerPointIsOpened() {
        Calendar now = Calendar.getInstance();
        return partnerPoint.workingHours.includes(now);
    }

}
