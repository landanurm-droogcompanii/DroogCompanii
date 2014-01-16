package ru.droogcompanii.application.view.activity.partner_category_list_activity;

import android.content.Context;
import android.os.Bundle;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.data.data_structure.PartnerCategory;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnerCategoriesReader;
import ru.droogcompanii.application.view.helpers.SimpleArrayAdapter;
import ru.droogcompanii.application.util.Keys;

/**
 * Created by Leonid on 17.12.13.
 */
public class PartnerCategoryListAdapter extends SimpleArrayAdapter<PartnerCategory> {

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
        List<PartnerCategory> partnerCategories = (List<PartnerCategory>)
                savedInstanceState.getSerializable(Keys.partnerCategoryListAdapterState);
        return new PartnerCategoryListAdapter(context, partnerCategoriesReader, partnerCategories);
    }


    private final List<PartnerCategory> partnerCategories;
    private final PartnerCategoriesReader partnerCategoriesReader;

    private PartnerCategoryListAdapter(Context context,
                                       PartnerCategoriesReader partnerCategoriesReader,
                                       List<PartnerCategory> partnerCategories) {
        super(context, partnerCategories,
                new ItemToTitleConvertor<PartnerCategory>() {
                    @Override
                    public String getTitle(PartnerCategory item) {
                        return item.title;
                    }
                });
        this.partnerCategoriesReader = partnerCategoriesReader;
        this.partnerCategories = partnerCategories;
    }

    public void updateListFromDatabase() {
        partnerCategories.clear();
        partnerCategories.addAll(partnerCategoriesReader.getPartnerCategories());
        notifyDataSetChanged();
    }

    public void saveStateInto(Bundle outState) {
        outState.putSerializable(Keys.partnerCategoryListAdapterState, (Serializable) partnerCategories);
    }
}