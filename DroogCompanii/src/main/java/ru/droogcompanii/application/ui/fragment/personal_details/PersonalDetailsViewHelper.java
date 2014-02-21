package ru.droogcompanii.application.ui.fragment.personal_details;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.personal_details.PersonalDetails;

/**
 * Created by ls on 21.02.14.
 */
public class PersonalDetailsViewHelper {

    private static interface ViewMaker {
        View make(int layoutId);
    }

    private final ViewMaker viewMaker;
    private View detailsView;
    private View rootView;


    public PersonalDetailsViewHelper(final LayoutInflater inflater) {
        viewMaker = new ViewMaker() {
            @Override
            public View make(int layoutId) {
                return inflater.inflate(layoutId, null);
            }
        };
    }

    public View getView() {
        if (rootView == null) {
            prepareView();
        }
        return rootView;
    }

    private void prepareView() {
        rootView = viewMaker.make(R.layout.fragment_personal_details);
        connectDetailsViewToRoot();
    }

    public void connectDetailsViewToRoot() {
        ViewGroup containerOfDetails = (ViewGroup) rootView.findViewById(R.id.containerOfPersonalDetails);
        containerOfDetails.removeAllViews();
        detailsView = viewMaker.make(R.layout.view_personal_details);
        containerOfDetails.addView(detailsView);
    }

    public void displayDetails(PersonalDetails personalDetails) {
        setText(R.id.firstName, personalDetails.getFirstName());
        setText(R.id.lastName, personalDetails.getLastName());
    }

    private void setText(int textViewId, String text) {
        TextView textView = (TextView) findView(textViewId);
        textView.setText(text);
    }

    private View findView(int id) {
        return detailsView.findViewById(id);
    }
}
