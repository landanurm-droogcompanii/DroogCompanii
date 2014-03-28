package ru.droogcompanii.application.ui.activity.partner_details_2;

import android.app.Activity;
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
import ru.droogcompanii.application.ui.activity.offer_list.OfferListActivity;
import ru.droogcompanii.application.ui.activity.partner_list.PartnerListActivity;
import ru.droogcompanii.application.ui.activity.search_2.InputProviderByPartnerCategory;
import ru.droogcompanii.application.ui.fragment.partner_details.ContactsViewMaker;
import ru.droogcompanii.application.util.view.FavoriteViewUtils;
import ru.droogcompanii.application.util.view.ImageDownloader;

/**
 * Created by ls on 27.03.14.
 */
public class PartnerDetailsDisplay {

    private static final int LAYOUT_ID = R.layout.fragment_partner_details_2;


    private final ContactsViewMaker contactsViewMaker;
    private final Activity activity;
    private final FavoriteViewUtils favoriteViewUtils;
    private final ImageDownloader imageDownloader;
    private final View view;

    private Partner partner;
    private boolean hasOffers;


    public PartnerDetailsDisplay(Activity activity, LayoutInflater inflater) {
        this.contactsViewMaker = new ContactsViewMaker(activity);
        this.activity = activity;
        this.favoriteViewUtils = new FavoriteViewUtils(activity);
        this.imageDownloader = new ImageDownloader();
        this.view = inflater.inflate(LAYOUT_ID, null);
    }

    public View getView() {
        return view;
    }

    private View findViewById(int id) {
        return view.findViewById(id);
    }

    public void display(Partner partner, boolean hasOffers) {
        this.partner = partner;
        this.hasOffers = hasOffers;
        display();
    }

    private void display() {
        setActivityTitle();
        setLogo();
        setIsFavorite();
        setTextDetails();
        setGoToOffersButton();
        setContacts();
        setLinkToCategory();
    }

    private void setActivityTitle() {
        activity.setTitle(partner.getTitle());
    }

    private void setTextDetails() {
        setText(R.id.fullPartnerTitle, partner.getFullTitle());
        setText(R.id.partnerDescription, partner.getDescription());
        setText(R.id.discount, prepareDiscountText());
    }

    private void setText(int idOfTextView, String text) {
        TextView textView = (TextView) findViewById(idOfTextView);
        textView.setText(text.trim());
    }

    private void setGoToOffersButton() {
        View goToOffers = findViewById(R.id.goToOffersButton);
        if (hasOffers) {
            goToOffers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onGoToOffersByPartner(partner);
                }
            });
            goToOffers.setVisibility(View.VISIBLE);
        } else {
            goToOffers.setVisibility(View.GONE);
        }
    }

    private void onGoToOffersByPartner(Partner partner) {
        OfferListActivity.start(activity, partner);
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
        PartnerCategoriesReader reader = new PartnerCategoriesReader(activity);
        return reader.getPartnerCategoryOf(partner);
    }

    private void goToCategory(PartnerCategory category) {
        PartnerListActivity.start(activity, new InputProviderByPartnerCategory(category));
    }

    private void setIsFavorite() {
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.isFavorite);
        favoriteViewUtils.init(checkBox, partner.getId());
    }
}
