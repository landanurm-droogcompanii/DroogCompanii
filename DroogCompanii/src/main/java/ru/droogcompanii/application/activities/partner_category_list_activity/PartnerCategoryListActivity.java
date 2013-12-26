package ru.droogcompanii.application.activities.partner_category_list_activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.io.Serializable;

import ru.droogcompanii.application.activities.partner_list_activity.PartnerListActivity;
import ru.droogcompanii.application.data.data_structure.PartnerCategory;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.util.ProgressDialogProvider;
import ru.droogcompanii.application.activities.helpers.ActionBarListActivity;

public class PartnerCategoryListActivity extends ActionBarListActivity
        implements OnDataUpdatingProgressListener, AdapterView.OnItemClickListener {

    private boolean updatedData;
    private PartnerCategoryListAdapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = prepareAdapter(savedInstanceState);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

        updatedData = updatedData(savedInstanceState);
        if (!updatedData) {
            startUpdate();
        }
    }

    private PartnerCategoryListAdapter prepareAdapter(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return PartnerCategoryListAdapter.newInstance(this);
        } else {
            Serializable state = savedInstanceState.getSerializable(Keys.partnerCategoryListAdapterState);
            return PartnerCategoryListAdapter.newInstance(this, state);
        }
    }

    private boolean updatedData(Bundle savedInstanceState) {
        //noinspection SimplifiableConditionalExpression
        return (savedInstanceState == null) ? false : savedInstanceState.getBoolean(Keys.updatedData);
    }

    private void startUpdate() {
        final PartnerCategoriesUpdatingTask task = new PartnerCategoriesUpdatingTask(this, this);
        progressDialog = ProgressDialogProvider.prepareProgressDialog(this);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                task.cancel(true);
            }
        });
        task.execute();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Keys.updatedData, updatedData);
        outState.putSerializable(Keys.partnerCategoryListAdapterState, adapter.getState());
    }

    @Override
    public void onPreDataUpdating() {
        updatedData = false;
        progressDialog.show();
    }

    @Override
    public void onPostDataUpdating() {
        updatedData = true;
        adapter.updateListFromDatabase();
        progressDialog.dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PartnerCategory selectedCategory = adapter.getItem(position);
        Intent intent = new Intent(this, PartnerListActivity.class);
        intent.putExtra(Keys.partnerCategory, selectedCategory);
        startActivity(intent);
    }
}
