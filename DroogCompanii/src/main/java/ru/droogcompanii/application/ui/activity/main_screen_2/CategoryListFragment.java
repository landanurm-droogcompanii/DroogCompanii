package ru.droogcompanii.application.ui.activity.main_screen_2;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.FavoriteDBUtils;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerCategoriesReader;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnersContracts;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.ui.util.ToastUtils;
import ru.droogcompanii.application.ui.util.able_to_start_task.FragmentAbleToStartTask;
import ru.droogcompanii.application.ui.util.able_to_start_task.TaskNotBeInterruptedDuringConfigurationChange;
import ru.droogcompanii.application.util.Objects;
import ru.droogcompanii.application.util.WeakReferenceWrapper;

/**
 * Created by ls on 14.03.14.
 */
public class CategoryListFragment extends FragmentAbleToStartTask implements AdapterView.OnItemClickListener {

    private static int counter = 0;

    private boolean isFirstLaunch;


    public static interface Callbacks {
        void onCurrentCategoryChanged(String conditionToReceivePartners);
    }

    private static final Callbacks DUMMY_CALLBACKS = new Callbacks() {
        @Override
        public void onCurrentCategoryChanged(String conditionToReceivePartners) {
            // do nothing
        }
    };

    private static abstract class ListItemHelper {

        public View makeView(Context context, View convertView) {
            String tag = getClass().getName();
            View itemView = convertView;
            if (itemView == null || !Objects.equals(tag, itemView.getTag())) {
                itemView = inflateView(context);
            }
            itemView.setTag(tag);
            fillItemView(context, itemView);

            return itemView;
        }

        private void fillItemView(Context context, View itemView) {
            TextView textView = (TextView) itemView.findViewById(R.id.text);
            setText(textView);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.icon);
            imageView.setImageDrawable(getIconDrawable(context));
        }

        protected Drawable getIconDrawable(Context context) {
            return null;
        }

        private View inflateView(Context context) {
            LayoutInflater inflater = LayoutInflater.from(context);
            return inflater.inflate(R.layout.view_list_item_favorite, null);
        }

        protected abstract void setText(TextView textView);

        public abstract String getConditionToReceivePartners();
    }

    private static class FavoriteListItemHelper extends ListItemHelper implements Serializable {

        @Override
        protected void setText(TextView textView) {
            textView.setText(R.string.favorite);
        }

        protected Drawable getIconDrawable(Context context) {
            Resources resources = context.getResources();
            return resources.getDrawable(R.drawable.ic_favorite);
        }

        @Override
        public String getConditionToReceivePartners() {
            return FavoriteDBUtils.getIsFavoriteCondition();
        }
    }

    private static class PartnerCategoryListItemHelper extends ListItemHelper implements Serializable {

        private final PartnerCategory partnerCategory;

        public PartnerCategoryListItemHelper(PartnerCategory partnerCategory) {
            this.partnerCategory = partnerCategory;
        }

        @Override
        protected void setText(TextView textView) {
            textView.setText(partnerCategory.getTitle());
        }

        @Override
        public String getConditionToReceivePartners() {
            return PartnersContracts.PartnersContract.COLUMN_NAME_CATEGORY_ID + " = " + partnerCategory.getId();
        }
    }

    private static class AllPartnersListItemHelper extends ListItemHelper implements Serializable {

        @Override
        protected void setText(TextView textView) {
            textView.setText(R.string.all_partners);
        }

        @Override
        public String getConditionToReceivePartners() {
            return "";
        }
    }

    private static class CategoryListAdapter extends ArrayAdapter<ListItemHelper> {

        public CategoryListAdapter(Context context, List<ListItemHelper> items) {
            super(context, R.layout.view_list_item_category, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListItemHelper itemHelper = getItem(position);
            return itemHelper.makeView(getContext(), convertView);
        }
    }



    private static final int TASK_REQUEST_CODE = 142;

    private static final String KEY_IS_FIRST_LIST_INITIALIZATION = "KEY_IS_FIRST_LIST_INITIALIZATION";

    private static final String KEY_HELPERS = "KEY_HELPERS";

    private static final String KEY_CURRENT_SELECTION = "KEY_CURRENT_SELECTION";


    private boolean isFirstListInitialization;
    private Callbacks callbacks;
    private CategoryListAdapter adapter;
    private int currentSelection;
    private List<ListItemHelper> listItemHelpers;
    private ListView listView;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        callbacks = DUMMY_CALLBACKS;
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listView = (ListView) inflater.inflate(R.layout.fragment_category_list, null);
        return listView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isFirstLaunch = savedInstanceState == null;
        if (savedInstanceState == null) {
            initStateByDefault();
        } else {
            restoreState(savedInstanceState);
        }
    }

    private void initStateByDefault() {
        currentSelection = 0;
        isFirstListInitialization = true;
        startTaskReceivingCategories();
    }

    private void startTaskReceivingCategories() {
        final WeakReferenceWrapper<Context> contextWrapper = WeakReferenceWrapper.from((Context) getActivity());
        startTask(TASK_REQUEST_CODE, new TaskNotBeInterruptedDuringConfigurationChange() {
            @Override
            protected Serializable doInBackground(Void... voids) {
                return (Serializable) prepareListItemHelpers(contextWrapper);
            }
        });
    }

    private static List<ListItemHelper> prepareListItemHelpers(WeakReferenceWrapper<Context> contextWrapper) {
        final ArrayList<ListItemHelper> helpers = new ArrayList<ListItemHelper>();
        helpers.add(new AllPartnersListItemHelper());
        helpers.add(new FavoriteListItemHelper());
        contextWrapper.handleIfExist(new WeakReferenceWrapper.Handler<Context>() {
            @Override
            public void handle(Context context) {
                readCategoriesAndAddHelpers(context, helpers);
            }
        });
        return helpers;
    }

    private static void readCategoriesAndAddHelpers(Context context, ArrayList<ListItemHelper> helpers) {
        PartnerCategoriesReader reader = new PartnerCategoriesReader(context);
        List<PartnerCategory> categories = reader.getAllPartnerCategories();
        helpers.ensureCapacity(categories.size() + helpers.size());
        for (PartnerCategory each : categories) {
            helpers.add(new PartnerCategoryListItemHelper(each));
        }
    }

    private void restoreState(Bundle savedInstanceState) {
        isFirstListInitialization = savedInstanceState.getBoolean(KEY_IS_FIRST_LIST_INITIALIZATION);
        currentSelection = savedInstanceState.getInt(KEY_CURRENT_SELECTION);
        listItemHelpers = (List<ListItemHelper>) savedInstanceState.getSerializable(KEY_HELPERS);
        if (listItemHelpers != null) {
            initList();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_FIRST_LIST_INITIALIZATION, isFirstListInitialization);
        outState.putSerializable(KEY_HELPERS, (Serializable) listItemHelpers);
        outState.putInt(KEY_CURRENT_SELECTION, currentSelection);
    }

    @Override
    public void onTaskResult(int requestCode, int resultCode, Serializable result) {
        if (requestCode == TASK_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                onListItemHelpersReceived((List<ListItemHelper>) result);
            } else {
                onReceivingHelpersCancelled();
            }
        }
    }

    private void onReceivingHelpersCancelled() {
        getActivity().finish();
    }

    private void onListItemHelpersReceived(List<ListItemHelper> helpers) {
        this.listItemHelpers = helpers;
        initList();
    }

    private void initList() {
        adapter = new CategoryListAdapter(getActivity(), listItemHelpers);
        listView.setAdapter(adapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        initSelection();
        listView.setOnItemClickListener(this);
        onCurrentCategoryChanged();
        if (isFirstListInitialization) {
            isFirstListInitialization = false;
        }
    }

    private void initSelection() {
        listView.setItemChecked(currentSelection, true);
        listView.smoothScrollToPosition(currentSelection);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        currentSelection = position;
        onCurrentCategoryChanged();
    }

    private void onCurrentCategoryChanged() {
        ToastUtils.SHORT.show(getActivity(), String.valueOf(counter++) + " " + isFirstLaunch);

        ListItemHelper item = adapter.getItem(currentSelection);
        String condition = item.getConditionToReceivePartners();
        callbacks.onCurrentCategoryChanged(condition);
    }
}
