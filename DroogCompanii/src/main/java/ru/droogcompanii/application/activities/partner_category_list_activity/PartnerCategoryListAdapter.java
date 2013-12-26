package ru.droogcompanii.application.activities.partner_category_list_activity;

import android.content.Context;
import android.util.Log;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.data.data_structure.PartnerCategory;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnerCategoriesReader;
import ru.droogcompanii.application.util.SimpleArrayAdapter;

/**
 * Created by Leonid on 17.12.13.
 */
class PartnerCategoryListAdapter extends SimpleArrayAdapter<PartnerCategory> {
    private final List<PartnerCategory> partnerCategories;
    private final PartnerCategoriesReader partnerCategoriesLoader;

    public static PartnerCategoryListAdapter newInstance(Context context) {
        PartnerCategoriesReader partnerCategoriesLoader = new PartnerCategoriesReader(context);
        List<PartnerCategory> partnerCategories = partnerCategoriesLoader.getPartnerCategories();
        return new PartnerCategoryListAdapter(context, partnerCategoriesLoader, partnerCategories);
    }

    public static PartnerCategoryListAdapter newInstance(Context context, Serializable savedState) {
        PartnerCategoriesReader partnerCategoriesLoader = new PartnerCategoriesReader(context);
        List<PartnerCategory> partnerCategories = (List<PartnerCategory>) savedState;
        return new PartnerCategoryListAdapter(context, partnerCategoriesLoader, partnerCategories);

    }

    private PartnerCategoryListAdapter(Context context,
                                       PartnerCategoriesReader partnerCategoriesLoader,
                                       List<PartnerCategory> partnerCategories) {
        super(context, partnerCategories,
              new ItemToTitleConvertor<PartnerCategory>() {
                  @Override
                  public String getTitle(PartnerCategory item) {
                      return item.title;
                  }
              });
        this.partnerCategoriesLoader = partnerCategoriesLoader;
        this.partnerCategories = partnerCategories;
    }

    public void updateListFromDatabase() {
        partnerCategories.clear();
        partnerCategories.addAll(partnerCategoriesLoader.getPartnerCategories());
        notifyDataSetChanged();
    }

    public Serializable getState() {
        return (Serializable) partnerCategories;
    }
}
