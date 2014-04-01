package ru.droogcompanii.application.data.db_util.hierarchy_of_partners;

import android.provider.BaseColumns;

/**
 * Created by Leonid on 09.12.13.
 */
public final class PartnerHierarchyContracts {

    public PartnerHierarchyContracts() { }

    public static class PartnerCategoriesContract implements BaseColumns {
        public static final String TABLE_NAME = "partner_categories";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
    }

    public static class PartnersContract implements BaseColumns {
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
    }

    public static class PartnerPointsContract implements BaseColumns {
        public static final String TABLE_NAME = "partner_points";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_PAYMENT_METHODS = "payment_methods";
        public static final String COLUMN_PHONES = "phones";
        public static final String COLUMN_WORKING_HOURS = "working_hours";
        public static final String COLUMN_PARTNER_ID = "partner_id";
    }
}