package ru.droogcompanii.application.ui.main_screen_2.filters_dialog.filters;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.util.location.ActualBaseLocationProvider;
import ru.droogcompanii.application.ui.util.view.RadioGroupMaker;

/**
 * Created by ls on 25.03.14.
 */
public class DistanceFilterHelper implements FilterHelper, Serializable {

    private static class DistanceOption {
        public final int labelId;
        public final double maxRadius;

        public DistanceOption(int labelId, double maxRadius) {
            this.labelId = labelId;
            this.maxRadius = maxRadius;
        }
    }

    private static final List<DistanceOption> DISTANCE_OPTIONS = Arrays.asList(
            new DistanceOption(R.string.distanceOption_No, -1.0),
            new DistanceOption(R.string.distanceOption_1000meters, 1000.0),
            new DistanceOption(R.string.distanceOption_3000meters, 3000.0),
            new DistanceOption(R.string.distanceOption_5000meters, 5000.0)
    );

    private static class Key {
        public static final String INDEX = "INDEX" + DistanceFilterHelper.class.getName();
    }

    private static final int RADIO_GROUP_LAYOUT_ID = R.layout.view_filter_distance_radio_group;
    private static final int RADIO_GROUP_ID = R.id.distanceFilterRadioGroup;

    private static final int INDEX_OF_INACTIVE_DISTANCE_OPTION = 0;
    private static final int DEFAULT_INDEX = INDEX_OF_INACTIVE_DISTANCE_OPTION;

    private int checkedIndex;

    public static DistanceFilterHelper getCurrent(Context context) {
        SharedPreferences sharedPreferences = FiltersSharedPreferencesProvider.get(context);
        DistanceFilterHelper distanceFilter = new DistanceFilterHelper();
        distanceFilter.readFrom(sharedPreferences);
        return distanceFilter;
    }

    public DistanceFilterHelper() {
        checkedIndex = DEFAULT_INDEX;
    }

    public boolean isActive() {
        return checkedIndex != INDEX_OF_INACTIVE_DISTANCE_OPTION;
    }

    public Collection<String> getColumnsOfPartnerPoint() {
        return Arrays.asList(
                PARTNER_POINTS.COLUMN_NAME_LATITUDE,
                PARTNER_POINTS.COLUMN_NAME_LONGITUDE
        );
    }

    @Override
    public Filter getFilter() {
        return new FilterImpl();
    }

    private class FilterImpl implements Filter {
        private final LatLng actualPosition;
        private final double maxRadius;

        FilterImpl() {
            actualPosition = ActualBaseLocationProvider.getPositionOfActualBaseLocation();
            maxRadius = getMaxRadius();
        }

        @Override
        public boolean isPassedThroughFilter(PartnerPoint partnerPoint, Cursor cursor) {
            double distance = SphericalUtil.computeDistanceBetween(
                    actualPosition, partnerPoint.getPosition()
            );
            return distance <= maxRadius;
        }
    }

    public double getMaxRadius() {
        return DISTANCE_OPTIONS.get(checkedIndex).maxRadius;
    }

    @Override
    public void readFrom(SharedPreferences sharedPreferences) {
        checkedIndex = sharedPreferences.getInt(Key.INDEX, DEFAULT_INDEX);
    }

    @Override
    public void writeTo(SharedPreferences.Editor editor) {
        editor.putInt(Key.INDEX, checkedIndex);
    }

    @Override
    public View inflateContentView(Context context) {
        RadioGroupMaker radioGroupMaker = new RadioGroupMaker(context);
        return radioGroupMaker.make(RADIO_GROUP_LAYOUT_ID, new RadioGroupMaker.RadioButtonsMaker() {
            @Override
            public List<RadioButton> make(Context context) {
                int numberOfDistanceOptions = DISTANCE_OPTIONS.size();
                List<RadioButton> radioButtons = new ArrayList<RadioButton>(numberOfDistanceOptions);
                for (int i = 0; i < numberOfDistanceOptions; ++i) {
                    radioButtons.add(prepareRadioButton(context, i));
                }
                return radioButtons;
            }
        });
    }

    private static RadioButton prepareRadioButton(Context context, int index) {
        DistanceOption distanceOption = DISTANCE_OPTIONS.get(index);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        RadioButton radioButton = (RadioButton)
                layoutInflater.inflate(R.layout.view_filter_distance_radio_button, null);
        radioButton.setText(distanceOption.labelId);
        radioButton.setId(index);
        return radioButton;
    }


    @Override
    public void displayOn(View view) {
        RadioGroup radioGroup = (RadioGroup) view.findViewById(RADIO_GROUP_ID);
        radioGroup.check(checkedIndex);
    }

    @Override
    public void readFrom(View view) {
        RadioGroup radioGroup = (RadioGroup) view.findViewById(RADIO_GROUP_ID);
        checkedIndex = radioGroup.getCheckedRadioButtonId();
    }

}
