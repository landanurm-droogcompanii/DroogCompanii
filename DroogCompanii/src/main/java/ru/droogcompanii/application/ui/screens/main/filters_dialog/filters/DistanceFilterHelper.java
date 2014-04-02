package ru.droogcompanii.application.ui.screens.main.filters_dialog.filters;

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
import ru.droogcompanii.application.util.location.ActualBaseLocationProvider;
import ru.droogcompanii.application.util.ui.view.RadioGroupMaker;

/**
 * Created by ls on 25.03.14.
 */
public class DistanceFilterHelper implements FilterHelper, Serializable {

    private abstract static class DistanceOption {

        public static DistanceOption none() {
            return new DistanceOption() {

                @Override
                public double getMaxRadius() {
                    return -1.0;
                }

                @Override
                public String getLabel(Context context) {
                    return context.getString(R.string.distanceOption_No);
                }
            };
        }

        public static DistanceOption byMeters(final int meters) {
            return new DistanceOption() {
                @Override
                public double getMaxRadius() {
                    return meters;
                }

                @Override
                public String getLabel(Context context) {
                    String metersText = context.getString(R.string.meters_in_filters);
                    return meters + " " + metersText;
                }
            };
        }

        public abstract double getMaxRadius();
        public abstract String getLabel(Context context);
    }

    private static final List<DistanceOption> DISTANCE_OPTIONS = Arrays.asList(
            DistanceOption.none(),
            DistanceOption.byMeters(1000),
            DistanceOption.byMeters(3000),
            DistanceOption.byMeters(5000)
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
                PARTNER_POINTS.COLUMN_LATITUDE,
                PARTNER_POINTS.COLUMN_LONGITUDE
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
            actualPosition = ActualBaseLocationProvider.getActualBasePosition();
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
        return DISTANCE_OPTIONS.get(checkedIndex).getMaxRadius();
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
        radioButton.setText(distanceOption.getLabel(context));
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
