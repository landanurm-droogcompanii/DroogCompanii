package ru.droogcompanii.application.ui.screens.partner_list;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.FavoriteDBUtils;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.util.WeakReferenceWrapper;

/**
 * Created by Leonid on 27.02.14.
 */
class PartnerListItemViewMaker {

    public static final int ROW_LAYOUT_ID = R.layout.view_partner_list_item;

    private final Context context;

    public PartnerListItemViewMaker(Context context) {
        this.context = context;
    }

    public View make(View convertView, Partner item) {
        View itemView = (convertView != null) ? convertView : inflateItemView();
        fill(itemView, item);
        return itemView;
    }

    private View inflateItemView() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return layoutInflater.inflate(ROW_LAYOUT_ID, null);
    }

    private void fill(View itemView, Partner partner) {
        TextView textView = (TextView) itemView.findViewById(R.id.textView);
        textView.setText(partner.getTitle());
        CheckBox checkBox = (CheckBox) itemView.findViewById(R.id.isFavoriteCheckBox);
        ProgressBar progressBar = (ProgressBar) itemView.findViewById(R.id.isFavoriteProgressBar);
        checkBox.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        initIsFavoriteCheckBox(checkBox, progressBar, partner.getId());
    }

    private static class CheckBoxTagger {
        public static void tag(CheckBox checkBox, int partnerId) {
            checkBox.setTag(partnerId);
        }

        public static boolean isTaggedByTag(CheckBox checkBox, int partnerId) {
            Object tag = checkBox.getTag();
            if (tag == null) {
                return false;
            }
            int attachedPartnerId = (Integer) tag;
            return attachedPartnerId == partnerId;
        }
    }

    private void initIsFavoriteCheckBox(CheckBox checkBox, ProgressBar progressBar, int partnerId) {
        CheckBoxTagger.tag(checkBox, partnerId);
        setInitialCheckedState(checkBox, progressBar, partnerId);
        setOnCheckedListener(checkBox, progressBar, partnerId);
    }

    private void setInitialCheckedState(CheckBox checkBox, final ProgressBar progressBar, final int partnerId) {
        final WeakReferenceWrapper<CheckBox> checkBoxWrapper = WeakReferenceWrapper.from(checkBox);
        AsyncTask<Void,Void,Boolean> task = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                checkBoxWrapper.handleIfExist(new WeakReferenceWrapper.Handler<CheckBox>() {
                    @Override
                    public void handle(CheckBox checkBoxIsFavorite) {
                        if (!CheckBoxTagger.isTaggedByTag(checkBoxIsFavorite, partnerId)) {
                            return;
                        }
                        setLoadedCheckBox(checkBoxIsFavorite, progressBar);
                    }
                });
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                FavoriteDBUtils favoriteDBUtils = new FavoriteDBUtils(DroogCompaniiApplication.getContext());
                return favoriteDBUtils.isFavorite(partnerId);
            }

            @Override
            protected void onPostExecute(final Boolean isFavorite) {
                super.onPostExecute(isFavorite);
                checkBoxWrapper.handleIfExist(new WeakReferenceWrapper.Handler<CheckBox>() {
                    @Override
                    public void handle(CheckBox checkBoxIsFavorite) {
                        if (!CheckBoxTagger.isTaggedByTag(checkBoxIsFavorite, partnerId)) {
                            return;
                        }
                        checkBoxIsFavorite.setChecked(isFavorite);
                        setCheckBoxIsReady(checkBoxIsFavorite, progressBar);
                    }
                });
            }
        };
        task.execute();
    }

    private void setLoadedCheckBox(CheckBox checkBox, ProgressBar progressBar) {
        checkBox.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setCheckBoxIsReady(CheckBox checkBox, ProgressBar progressBar) {
        checkBox.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void setOnCheckedListener(final CheckBox checkBox, final ProgressBar progressBar, final int partnerId) {
        final WeakReferenceWrapper<CheckBox> checkBoxWrapper = WeakReferenceWrapper.from(checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean isFavorite) {
                if (!CheckBoxTagger.isTaggedByTag(checkBox, partnerId)) {
                    return;
                }
                AsyncTask<Void,Void,Void> task = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        checkBoxWrapper.handleIfExist(new WeakReferenceWrapper.Handler<CheckBox>() {
                            @Override
                            public void handle(CheckBox checkBoxIsFavorite) {
                                if (!CheckBoxTagger.isTaggedByTag(checkBoxIsFavorite, partnerId)) {
                                    return;
                                }
                                setLoadedCheckBox(checkBoxIsFavorite, progressBar);
                            }
                        });
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        FavoriteDBUtils favoriteDBUtils = new FavoriteDBUtils(DroogCompaniiApplication.getContext());
                        favoriteDBUtils.setFavorite(partnerId, isFavorite);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        checkBoxWrapper.handleIfExist(new WeakReferenceWrapper.Handler<CheckBox>() {
                            @Override
                            public void handle(CheckBox checkBox) {
                                if (CheckBoxTagger.isTaggedByTag(checkBox, partnerId)) {
                                    setCheckBoxIsReady(checkBox, progressBar);
                                }
                            }
                        });
                    }
                };
                task.execute();
            }
        });
    }

}
