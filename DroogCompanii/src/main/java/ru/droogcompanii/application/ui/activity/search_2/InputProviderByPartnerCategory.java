package ru.droogcompanii.application.ui.activity.search_2;

import android.content.Context;
import android.database.Cursor;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.data.db_util.CursorHandler;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerHierarchyContracts;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerPointsReader;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnersHierarchyReaderFromDatabase;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnersReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.activity.partner_list.PartnerListActivity;
import ru.droogcompanii.application.util.Holder;

/**
 * Created by ls on 11.03.14.
 */
public class InputProviderByPartnerCategory implements PartnerListActivity.InputProvider, Serializable {

    private final PartnerCategory category;

    public InputProviderByPartnerCategory(PartnerCategory category) {
        this.category = category;
    }

    @Override
    public List<Partner> getPartners(Context context) {
        PartnersReader partnersReader = new PartnersReader(context);
        return partnersReader.getPartnersOf(category);
    }

    @Override
    public List<PartnerPoint> getAllPartnerPoints(Context context) {
        final PartnerHierarchyContracts.PartnersContract PARTNERS = new PartnerHierarchyContracts.PartnersContract();
        final PartnerHierarchyContracts.PartnerPointsContract PARTNER_POINTS = new PartnerHierarchyContracts.PartnerPointsContract();
        PartnersHierarchyReaderFromDatabase reader = new PartnersHierarchyReaderFromDatabase(context);

        String sql = "SELECT " + PARTNER_POINTS.TABLE_NAME + ".* FROM " + PARTNER_POINTS.TABLE_NAME +
            " WHERE " + PARTNER_POINTS.TABLE_NAME + "." + PARTNER_POINTS.COLUMN_NAME_PARTNER_ID +
            " IN (" +
                " SELECT " + PARTNERS.TABLE_NAME + "." + PARTNERS.COLUMN_NAME_ID + " FROM " + PARTNERS.TABLE_NAME +
                " WHERE " + PARTNERS.TABLE_NAME + "." + PARTNERS.COLUMN_NAME_CATEGORY_ID + " = " + category.getId() +
            " )";

        final Holder<List<PartnerPoint>> resultHolder = Holder.from(null);

        reader.handleCursorByQuery(sql, new CursorHandler() {
            @Override
            public void handle(Cursor cursor) {
                resultHolder.value = PartnerPointsReader.getPartnerPointsFromCursor(cursor);
            }
        });

        return resultHolder.value;
    }

    @Override
    public String getTitle(Context context) {
        return null;
    }
}