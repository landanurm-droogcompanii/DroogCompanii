package ru.droogcompanii.application.ui.fragment.partner_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.ui.util.view.FavoriteViewUtils;

/**
 * Created by Leonid on 27.02.14.
 */
class PartnerListItemViewMaker {
    private final Context context;
    private final int rowLayoutId;
    private final FavoriteViewUtils favoriteViewUtils;

    public PartnerListItemViewMaker(Context context, int rowLayoutId) {
        this.context = context;
        this.rowLayoutId = rowLayoutId;
        this.favoriteViewUtils = new FavoriteViewUtils(context);
    }

    public View make(View convertView, Partner item) {
        View itemView = (convertView != null) ? convertView : inflateItemView();
        fill(itemView, item);
        return itemView;
    }

    private void fill(View itemView, Partner partner) {
        TextView textView = (TextView) itemView.findViewById(R.id.textView);
        textView.setText(partner.getTitle());
        CheckBox checkBox = (CheckBox) itemView.findViewById(R.id.isFavorite);
        favoriteViewUtils.init(checkBox, partner.getId());
    }

    private View inflateItemView() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return layoutInflater.inflate(rowLayoutId, null);
    }

}
