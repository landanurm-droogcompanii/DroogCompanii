package ru.droogcompanii.application.ui.fragment.partner_details;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.readers_from_database.PartnerCategoriesReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.activity.search.search_result_provider_impl.SearchResultProviderByPartnerCategory;
import ru.droogcompanii.application.ui.activity.search_result_list.SearchResultListActivity;
import ru.droogcompanii.application.ui.fragment.partner_points_info_panel.CallerHelper;
import ru.droogcompanii.application.ui.fragment.partner_points_info_panel.RouteHelper;

/**
 * Created by ls on 12.02.14.
 */
public class PartnerDetailsViewHelper {

    private final CallerHelper callerHelper;
    private final Context context;
    private final RouteHelper routeHelper;
    private final View view;
    private final WorkingHoursViewMaker workingHoursViewMaker;

    private Partner partner;
    private PartnerPoint partnerPoint;


    public PartnerDetailsViewHelper(FragmentActivity activity, LayoutInflater inflater, ViewGroup container) {
        context = activity;
        workingHoursViewMaker = new WorkingHoursViewMaker(activity);
        callerHelper = new CallerHelper(activity);
        routeHelper = new RouteHelper(activity);
        view = inflater.inflate(R.layout.fragment_partner_details, container, false);
    }

    public View getViewFilledBy(Partner partner, PartnerPoint partnerPoint) {
        this.partner = partner;
        this.partnerPoint = partnerPoint;
        fill();
        return view;
    }

    private void fill() {
        setText(R.id.fullPartnerTitle, partner.fullTitle);
        setText(R.id.discount, prepareDiscountText());
        setText(R.id.partnerPointTitle, partnerPoint.title);
        setText(R.id.partnerPointAddress, partnerPoint.address);
        setPaymentMethods();
        setWorkingHours();
        setPhoneButton();
        setRouteButton();
        setCategory();
    }

    private void setText(int idOfTextView, String text) {
        TextView textView = (TextView) findViewById(idOfTextView);
        textView.setText(text);
    }

    private View findViewById(int id) {
        return view.findViewById(id);
    }

    private String prepareDiscountText() {
        return partner.discountType + ": " + partner.discount + "%";
    }

    private void setPaymentMethods() {
        TextView paymentMethodsTextView = (TextView) findViewById(R.id.paymentMethods);
        paymentMethodsTextView.setText(partnerPoint.paymentMethods);
    }

    private void setWorkingHours() {
        ViewGroup containerOfWorkingHours = (ViewGroup) findViewById(R.id.containerOfWorkingHours);
        View workingHoursView = workingHoursViewMaker.makeView(partnerPoint.workingHours);
        containerOfWorkingHours.addView(workingHoursView);
    }

    private void setPhoneButton() {
        View phoneButton = findViewById(R.id.phoneButton);
        callerHelper.initPhoneButton(phoneButton, partnerPoint);
    }

    private void setRouteButton() {
        View routeButton = findViewById(R.id.routeButton);
        routeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                routeHelper.showRouteTo(partnerPoint);
            }
        });
    }

    private void setCategory() {
        final PartnerCategory category = defineCategory();
        Button categoryButton = (Button) findViewById(R.id.categoryButton);
        categoryButton.setText(category.title);
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCategory(category);
            }
        });
    }

    private PartnerCategory defineCategory() {
        PartnerCategoriesReader reader = new PartnerCategoriesReader(context);
        return reader.getPartnerCategoryOf(partner);
    }

    private void goToCategory(PartnerCategory category) {
        SearchResultListActivity.start(context, new SearchResultProviderByPartnerCategory(category));
    }

}
