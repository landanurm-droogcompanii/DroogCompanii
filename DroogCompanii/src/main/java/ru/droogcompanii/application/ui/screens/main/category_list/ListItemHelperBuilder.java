package ru.droogcompanii.application.ui.screens.main.category_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.FavoriteDBUtils;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerHierarchyContracts;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;

/**
 * Created by ls on 24.03.14.
 */
class ListItemHelperBuilder {

    public static ListItemHelper getFavoriteListItemHelper() {
        return new ListItemHelperImpl() {
            @Override
            public String getTitle(Context context) {
                return context.getString(R.string.favorite);
            }

            @Override
            protected void initIcon(ImageView imageView) {
                imageView.setImageResource(R.drawable.ic_favorite);
            }

            @Override
            public String getConditionToReceivePartners() {
                return FavoriteDBUtils.getIsFavoriteCondition();
            }
        };
    }

    public static ListItemHelper getPartnerCategoryListItemHelper(final PartnerCategory partnerCategory) {
        return new ListItemHelperImpl() {
            @Override
            public String getTitle(Context context) {
                return partnerCategory.getTitle();
            }

            @Override
            protected void initIcon(ImageView imageView) {
                imageView.setImageDrawable(null);
            }

            @Override
            public String getConditionToReceivePartners() {
                return PartnerHierarchyContracts.PartnersContract.COLUMN_NAME_CATEGORY_ID +
                        " = " + partnerCategory.getId();
            }
        };
    }

    public static ListItemHelper getAllPartnersListItemHelper() {
        return new ListItemHelperImpl() {
            @Override
            public String getTitle(Context context) {
                return context.getString(R.string.all_partners);
            }

            @Override
            protected void initIcon(ImageView imageView) {
                imageView.setImageDrawable(null);
            }

            @Override
            public String getConditionToReceivePartners() {
                return "";
            }
        };
    }

    private static abstract class ListItemHelperImpl implements ListItemHelper, Serializable {

        public View makeView(Context context, View convertView) {
            View itemView = (convertView != null) ? convertView : inflateView(context);
            TextView textView = (TextView) itemView.findViewById(R.id.text);
            textView.setText(getTitle(context));
            ImageView imageView = (ImageView) itemView.findViewById(R.id.icon);
            initIcon(imageView);
            return itemView;
        }

        private View inflateView(Context context) {
            LayoutInflater inflater = LayoutInflater.from(context);
            return inflater.inflate(CategoryListAdapter.ROW_LAYOUT_ID, null);
        }

        public abstract String getTitle(Context context);

        protected abstract void initIcon(ImageView imageView);

        public abstract String getConditionToReceivePartners();
    }
}
