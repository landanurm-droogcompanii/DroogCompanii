package ru.droogcompanii.application.data.db_util.contracts;

import android.provider.BaseColumns;

/**
 * Created by ls on 03.04.14.
 */
public class PartnerCategoriesContract implements BaseColumns {
    public static final String TABLE_NAME = "partner_categories";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TEXT_SEARCH_IN = "text_search_in";
}