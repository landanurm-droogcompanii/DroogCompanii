package ru.droogcompanii.application.ui.screens.search;

import android.content.Context;
import android.os.Bundle;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerCategoriesReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;

/**
 * Created by Leonid on 17.12.13.
 */
class PartnerCategoryListAdapter extends SimpleArrayAdapter<PartnerCategory> {

    private static final String KEY_PARTNER_CATEGORIES = "KEY_PARTNER_CATEGORIES";

    public static PartnerCategoryListAdapter newInstance(Context context, Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return createAdapter(context);
        }
        return createAdapter(context, savedInstanceState);
    }

    private static PartnerCategoryListAdapter createAdapter(Context context) {
        PartnerCategoriesReader partnerCategoriesReader = new PartnerCategoriesReader(context);
        List<PartnerCategory> partnerCategories = partnerCategoriesReader.getAllPartnerCategories();
        return new PartnerCategoryListAdapter(context, partnerCategories);
    }

    private static PartnerCategoryListAdapter createAdapter(Context context, Bundle savedInstanceState) {
        @SuppressWarnings("unchecked")
        List<PartnerCategory> partnerCategories =
                (List<PartnerCategory>) savedInstanceState.getSerializable(KEY_PARTNER_CATEGORIES);
        return new PartnerCategoryListAdapter(context, partnerCategories);
    }


    private final List<PartnerCategory> partnerCategories;

    private PartnerCategoryListAdapter(Context context, List<PartnerCategory> partnerCategories) {
        super(context, partnerCategories,
                new ItemToTitleConverter<PartnerCategory>() {
                    @Override
                    public String getTitle(PartnerCategory item) {
                        return item.getTitle();
                    }
                });
        this.partnerCategories = partnerCategories;
    }

    public void saveStateInto(Bundle outState) {
        outState.putSerializable(KEY_PARTNER_CATEGORIES, (Serializable) partnerCategories);
    }
}
