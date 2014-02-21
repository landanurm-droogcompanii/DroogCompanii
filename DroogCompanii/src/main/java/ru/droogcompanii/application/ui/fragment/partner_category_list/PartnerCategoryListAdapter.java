package ru.droogcompanii.application.ui.fragment.partner_category_list;

import android.content.Context;
import android.os.Bundle;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerCategoriesReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.ui.helpers.SimpleArrayAdapter;
import ru.droogcompanii.application.util.Keys;

/**
 * Created by Leonid on 17.12.13.
 */
class PartnerCategoryListAdapter extends SimpleArrayAdapter<PartnerCategory> {

    public static PartnerCategoryListAdapter newInstance(Context context, Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return createAdapter(context);
        }
        return createAdapter(context, savedInstanceState);
    }

    private static PartnerCategoryListAdapter createAdapter(Context context) {
        PartnerCategoriesReader partnerCategoriesReader = new PartnerCategoriesReader(context);
        List<PartnerCategory> partnerCategories = partnerCategoriesReader.getPartnerCategories();
        return new PartnerCategoryListAdapter(context, partnerCategoriesReader, partnerCategories);
    }

    private static PartnerCategoryListAdapter createAdapter(Context context, Bundle savedInstanceState) {
        PartnerCategoriesReader partnerCategoriesReader = new PartnerCategoriesReader(context);
        @SuppressWarnings("unchecked")
        List<PartnerCategory> partnerCategories =
                (List<PartnerCategory>) savedInstanceState.getSerializable(Keys.partnerCategories);
        return new PartnerCategoryListAdapter(context, partnerCategoriesReader, partnerCategories);
    }


    private final List<PartnerCategory> partnerCategories;
    private final PartnerCategoriesReader partnerCategoriesReader;

    private PartnerCategoryListAdapter(Context context,
                                       PartnerCategoriesReader partnerCategoriesReader,
                                       List<PartnerCategory> partnerCategories) {
        super(context, partnerCategories,
                new ItemToTitleConverter<PartnerCategory>() {
                    @Override
                    public String getTitle(PartnerCategory item) {
                        return item.getTitle();
                    }
                });
        this.partnerCategoriesReader = partnerCategoriesReader;
        this.partnerCategories = partnerCategories;
    }

    public void saveStateInto(Bundle outState) {
        outState.putSerializable(Keys.partnerCategories, (Serializable) partnerCategories);
    }
}
