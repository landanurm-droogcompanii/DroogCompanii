package ru.droogcompanii.application.activities.partner_category_list_activity;

import android.content.Context;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.data.data_structure.PartnerCategory;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnerCategoriesReader;
import ru.droogcompanii.application.activities.helpers.SimpleArrayAdapter;

/**
 * Created by Leonid on 17.12.13.
 */
class PartnerCategoryListAdapter extends SimpleArrayAdapter<PartnerCategory> {

    public static PartnerCategoryListAdapter newInstance(Context context) {
        PartnerCategoriesReader partnerCategoriesReader = new PartnerCategoriesReader(context);
        List<PartnerCategory> partnerCategories = partnerCategoriesReader.getPartnerCategories();
        return new PartnerCategoryListAdapter(context, partnerCategoriesReader, partnerCategories);
    }

    public static PartnerCategoryListAdapter newInstance(Context context, Serializable savedState) {
        PartnerCategoriesReader partnerCategoriesLoader = new PartnerCategoriesReader(context);
        @SuppressWarnings("unchecked")
        List<PartnerCategory> partnerCategories = (List<PartnerCategory>) savedState;
        return new PartnerCategoryListAdapter(context, partnerCategoriesLoader, partnerCategories);
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

    public Serializable getState() {
        return (Serializable) partnerCategories;
    }
}
