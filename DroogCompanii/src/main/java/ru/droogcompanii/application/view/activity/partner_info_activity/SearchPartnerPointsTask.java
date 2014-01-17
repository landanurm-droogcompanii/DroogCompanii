package ru.droogcompanii.application.view.activity.partner_info_activity;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.Marker;

import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;

/**
 * Created by ls on 24.12.13.
 */
public class SearchPartnerPointsTask extends AsyncTask<String, Void, VisibilityOfMarkers> {

    public static interface OnTaskIsDoneListener {
        void onTaskIsDone();
    }

    private final CalculatorOfVisibilityOfMarkers calculatorOfVisibilityOfMarkers;
    private final OnTaskIsDoneListener onTaskIsDoneListener;


    public SearchPartnerPointsTask(List<Marker> markers,
                                   List<PartnerPoint> partnerPoints,
                                   OnTaskIsDoneListener onTaskIsDoneListener) {
        this.calculatorOfVisibilityOfMarkers = new CalculatorOfVisibilityOfMarkers(markers, partnerPoints);
        this.onTaskIsDoneListener = onTaskIsDoneListener;
    }

    @Override
    protected VisibilityOfMarkers doInBackground(String... args) {
        String query = args[0];
        return calculatorOfVisibilityOfMarkers.calculate(query);
    }

    @Override
    protected void onPostExecute(VisibilityOfMarkers visibilityOfMarkers) {
        super.onPostExecute(visibilityOfMarkers);
        visibilityOfMarkers.update();
        onTaskIsDoneListener.onTaskIsDone();
    }
}