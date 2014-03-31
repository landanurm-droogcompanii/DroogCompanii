package ru.droogcompanii.application.ui.screens.search_result_list;

import android.content.Context;
import android.database.Cursor;

import java.io.Serializable;

import ru.droogcompanii.application.data.db_util.CursorHandler;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerCategoriesReader;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerHierarchyContracts;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerPointsReader;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnersHierarchyReaderFromDatabase;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnersReader;

/**
 * Created by ls on 11.03.14.
 */
public class InputProviderBySearchQuery implements SearchResultListFragment.InputProvider, Serializable {
    private static final PartnerHierarchyContracts.PartnerCategoriesContract
            CATEGORIES = new PartnerHierarchyContracts.PartnerCategoriesContract();
    private static final PartnerHierarchyContracts.PartnersContract
            PARTNERS = new PartnerHierarchyContracts.PartnersContract();
    private static final PartnerHierarchyContracts.PartnerPointsContract
            POINTS = new PartnerHierarchyContracts.PartnerPointsContract();


    private final String sqlCategoriesReceiver;
    private final String sqlPartnersReceiver;
    private final String sqlPointsReceiver;

    public InputProviderBySearchQuery(String searchQuery) {
        sqlCategoriesReceiver = prepareSqlQuery(searchQuery, CATEGORIES.TABLE_NAME, CATEGORIES.COLUMN_NAME_TITLE);
        sqlPartnersReceiver = prepareSqlQuery(searchQuery, PARTNERS.TABLE_NAME, PARTNERS.COLUMN_NAME_TITLE);
        sqlPointsReceiver = prepareSqlQuery(searchQuery, POINTS.TABLE_NAME, POINTS.COLUMN_NAME_TITLE);
    }

    private static String prepareSqlQuery(String searchQuery, String tableName, String columnName) {
        return "SELECT * FROM " + tableName + " WHERE " +
                columnName + " LIKE " + quote("%" + searchQuery + "%") + " ;";
    }

    private static String quote(String stringToQuote) {
        return "\'" + stringToQuote + "\'";
    }

    @Override
    public SearchResultListFragment.Input getInput(Context context) {
        final SearchResultListFragment.Input input = new SearchResultListFragment.Input();
        PartnersHierarchyReaderFromDatabase reader = new PartnersHierarchyReaderFromDatabase(context);
        reader.handleCursorByQuery(sqlCategoriesReceiver, new CursorHandler() {
            @Override
            public void handle(Cursor cursor) {
                input.categories.addAll(
                        PartnerCategoriesReader.getPartnerCategoriesFromCursor(cursor)
                );
            }
        });
        reader.handleCursorByQuery(sqlPartnersReceiver, new CursorHandler() {
            @Override
            public void handle(Cursor cursor) {
                input.partners.addAll(
                        PartnersReader.getPartnersFromCursor(cursor)
                );
            }
        });
        reader.handleCursorByQuery(sqlPointsReceiver, new CursorHandler() {
            @Override
            public void handle(Cursor cursor) {
                input.points.addAll(
                        PartnerPointsReader.getPartnerPointsFromCursor(cursor)
                );
            }
        });
        return input;
    }

}
