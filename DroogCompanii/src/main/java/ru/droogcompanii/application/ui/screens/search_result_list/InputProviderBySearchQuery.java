package ru.droogcompanii.application.ui.screens.search_result_list;

import android.content.Context;
import android.database.Cursor;

import java.io.Serializable;

import ru.droogcompanii.application.data.db_util.CursorHandler;
import ru.droogcompanii.application.data.db_util.SearchingInDBUtils;
import ru.droogcompanii.application.data.db_util.contracts.*;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerCategoriesReader;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerPointsReader;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnersHierarchyReaderFromDatabase;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnersReader;

/**
 * Created by ls on 11.03.14.
 */
public class InputProviderBySearchQuery implements SearchResultListFragment.InputProvider, Serializable {

    private final String sqlCategoriesReceiver;
    private final String sqlPartnersReceiver;
    private final String sqlPointsReceiver;

    public InputProviderBySearchQuery(String searchQuery) {
        String correctedSearchQuery = SearchingInDBUtils.convertInTheSameCharacterCase(searchQuery.trim());
        sqlCategoriesReceiver = prepareSqlQuery(correctedSearchQuery,
                                                PartnerCategoriesContract.TABLE_NAME,
                                                PartnerCategoriesContract.COLUMN_TEXT_SEARCH_IN);
        sqlPartnersReceiver = prepareSqlQuery(correctedSearchQuery,
                                              PartnersContract.TABLE_NAME,
                                              PartnersContract.COLUMN_TEXT_SEARCH_IN);
        sqlPointsReceiver = prepareSqlQuery(correctedSearchQuery,
                                            PartnerPointsContract.TABLE_NAME,
                                            PartnerPointsContract.COLUMN_TEXT_SEARCH_IN);
    }

    private static String prepareSqlQuery(String searchQuery, String tableName, String columnName) {
        String pattern = quote("%" + searchQuery + "%");
        return "SELECT * FROM " + tableName + " WHERE " + columnName + " LIKE " + pattern + " ;";
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
