package ru.droogcompanii.application.data.db_util.hierarchy_of_partners;

import android.provider.BaseColumns;

/**
 * Created by Leonid on 09.12.13.
 */
public final class PartnerHierarchyContracts {

    public PartnerHierarchyContracts() { }

    public static class PartnerCategoriesContract implements BaseColumns {
        public static final String TABLE_NAME = "partner_categories";

        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
    }

    public static class PartnersContract implements BaseColumns {
        public static final String TABLE_NAME = "partners";

        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_FULL_TITLE = "full_title";
        public static final String COLUMN_NAME_IMAGE_URL = "image_url";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_WEB_SITES = "web_sites";
        public static final String COLUMN_NAME_EMAILS = "emails";
        public static final String COLUMN_NAME_DISCOUNT_TYPE = "discount_type";
        public static final String COLUMN_NAME_DISCOUNT_SIZE = "discount_size";
        public static final String COLUMN_NAME_CATEGORY_ID = "category_id";
        public static final String COLUMN_NAME_IS_FAVORITE = "is_favorite";
    }

    public static class PartnerPointsContract implements BaseColumns {
        public static final String TABLE_NAME = "partner_points";

        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_ADDRESS = "address";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_PAYMENT_METHODS = "payment_methods";
        public static final String COLUMN_NAME_PHONES = "phones";
        public static final String COLUMN_NAME_WORKING_HOURS = "working_hours";
        public static final String COLUMN_NAME_PARTNER_ID = "partner_id";
    }
}