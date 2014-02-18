package ru.droogcompanii.application.ui.fragment.search_result_list;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.FavoriteUtils;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchResult;

/**
 * Created by ls on 14.01.14.
 */
public class PartnerSearchResultListAdapter extends ArrayAdapter<SearchResult<Partner>> {

    private static final int ROW_LAYOUT_ID = R.layout.view_search_result_list_item;

    public PartnerSearchResultListAdapter(Context context, List<SearchResult<Partner>> partners) {
        super(context, ROW_LAYOUT_ID, partners);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = inflateItemView();
        }
        SearchResult<Partner> item = getItem(position);
        TextView textView = (TextView) itemView.findViewById(R.id.textView);
        textView.setTextColor(textColorOf(item));
        Partner partner = item.value();
        textView.setText(partner.getTitle());
        setIsFavorite(itemView, partner);
        return itemView;
    }

    private View inflateItemView() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        return layoutInflater.inflate(ROW_LAYOUT_ID, null);
    }

    private int textColorOf(SearchResult<Partner> item) {
        Resources resources = getContext().getResources();
        return resources.getColor(textColorIdOf(item));
    }

    private static int textColorIdOf(SearchResult<Partner> item) {
        if (item.meetsSearchCriteria()) {
            return R.color.textColorOfItemWhichMeetsCriteria;
        } else {
            return R.color.textColorOfItemWhichDoesNotMeetCriteria;
        }
    }

    private void setIsFavorite(View itemView, final Partner partner) {
        CheckBox isFavoriteCheckBox = (CheckBox) itemView.findViewById(R.id.isFavorite);
        final FavoriteUtils favoriteUtils = new FavoriteUtils(getContext());
        isFavoriteCheckBox.setChecked(favoriteUtils.isFavorite(partner));
        isFavoriteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                favoriteUtils.setFavorite(partner, checked);
            }
        });
    }
}
