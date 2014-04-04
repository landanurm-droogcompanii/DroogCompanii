package ru.droogcompanii.application.data.db_util.contracts;

import android.provider.BaseColumns;

/**
 * Created by ls on 03.04.14.
 */
public class PartnersContract implements BaseColumns {
    public static final String TABLE_NAME = "partners";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_FULL_TITLE = "full_title";
    public static final String COLUMN_IMAGE_URL = "image_url";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_WEB_SITES = "web_sites";
    public static final String COLUMN_EMAILS = "emails";
    public static final String COLUMN_DISCOUNT_TYPE = "discount_type";
    public static final String COLUMN_DISCOUNT_SIZE = "discount_size";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_IS_FAVORITE = "is_favorite";
    public static final String COLUMN_TEXT_SEARCH_IN = "text_search_in";
}
