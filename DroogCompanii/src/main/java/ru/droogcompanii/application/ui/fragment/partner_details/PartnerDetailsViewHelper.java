package ru.droogcompanii.application.ui.fragment.partner_details;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerCategoriesReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.activity.offer_list.OfferListActivity;
import ru.droogcompanii.application.ui.activity.partner_list.PartnerListActivity;
import ru.droogcompanii.application.ui.activity.search_2.InputProviderByPartnerCategory;
import ru.droogcompanii.application.ui.util.ImageDownloader;
import ru.droogcompanii.application.ui.util.FavoriteViewUtils;
import ru.droogcompanii.application.ui.util.Router;
import ru.droogcompanii.application.ui.util.caller.CallerHelper;

/**
 * Created by ls on 12.02.14.
 */
public class PartnerDetailsViewHelper {

    private static interface ViewMaker {
        View make();
    }

    private final CallerHelper callerHelper;
    private final ContactsViewMaker contactsViewMaker;
    private final Context context;
    private final FavoriteViewUtils favoriteViewUtils;
    private final ImageDownloader imageDownloader;
    private final Router router;
    private final ViewMaker viewMaker;
    private final WorkingHoursViewMaker workingHoursViewMaker;

    private Partner partner;
    private PartnerPoint partnerPoint;
    private View view;

    public PartnerDetailsViewHelper(FragmentActivity activity,
                                    final LayoutInflater inflater, final ViewGroup container) {
        context = activity;
        imageDownloader = new ImageDownloader();
        contactsViewMaker = new ContactsViewMaker(activity);
        workingHoursViewMaker = new WorkingHoursViewMaker(activity);
        callerHelper = new CallerHelper(activity);
        router = new Router(activity);
        favoriteViewUtils = new FavoriteViewUtils(activity);
        viewMaker = new ViewMaker() {
            @Override
            public View make() {
                return inflater.inflate(R.layout.fragment_partner_details, container, false);
            }
        };
    }

    public View getViewFilledBy(Partner partner, PartnerPoint partnerPoint) {
        this.partner = partner;
        this.partnerPoint = partnerPoint;
        this.view = viewMaker.make();
        fill();
        return view;
    }

    private void fill() {
        setLogo();
        setTextDetails();
        setGoToOffersButton();
        setContacts();
        setWorkingHours();
        setPhoneButton();
        setRouteButton();
        setLinkToCategory();
        setIsFavorite();
    }

    private void setTextDetails() {
        setText(R.id.fullPartnerTitle, partner.getFullTitle());
        setText(R.id.partnerDescription, partner.getDescription());
        setText(R.id.discount, prepareDiscountText());
        setText(R.id.partnerPointTitle, partnerPoint.getTitle());
        setText(R.id.partnerPointAddress, partnerPoint.getAddress());
        setText(R.id.paymentMethods, partnerPoint.getPaymentMethods());
    }

    private void setText(int idOfTextView, String text) {
        TextView textView = (TextView) findViewById(idOfTextView);
        textView.setText(text.trim());
    }

    private View findViewById(int id) {
        return view.findViewById(id);
    }

    private void setGoToOffersButton() {
        findViewById(R.id.goToOffersButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGoToOffersOfPartner(partner);
            }
        });
    }

    private void onGoToOffersOfPartner(Partner partner) {
        OfferListActivity.start(context, partner);
    }

    private void setContacts() {
        ViewGroup containerOfContacts = (ViewGroup) findViewById(R.id.containerOfContacts);
        for (String webSite : partner.getWebSites()) {
            containerOfContacts.addView(contactsViewMaker.makeViewByWebSite(webSite));
        }
        for (String email : partner.getEmails()) {
            containerOfContacts.addView(contactsViewMaker.makeViewByEmail(email));
        }
    }

    private void setLogo() {
        ImageView logoImageView = (ImageView) findViewById(R.id.logo);
        imageDownloader.download(partner.getImageUrl(), logoImageView);
    }

    private String prepareDiscountText() {
        return partner.getDiscountType() + ": " + partner.getDiscountSize() + "%";
    }

    private void setWorkingHours() {
        ViewGroup containerOfWorkingHours = (ViewGroup) findViewById(R.id.containerOfWorkingHours);
        View workingHoursView = workingHoursViewMaker.makeView(partnerPoint.getWorkingHours());
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
                router.routeTo(partnerPoint);
            }
        });
    }

    private void setLinkToCategory() {
        final PartnerCategory category = defineCategory();
        Button categoryButton = (Button) findViewById(R.id.categoryButton);
        categoryButton.setText(category.getTitle());
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
        PartnerListActivity.start(context, new InputProviderByPartnerCategory(category));
    }

    private void setIsFavorite() {
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.isFavorite);
        favoriteViewUtils.init(checkBox, partner.getId());
    }

}
