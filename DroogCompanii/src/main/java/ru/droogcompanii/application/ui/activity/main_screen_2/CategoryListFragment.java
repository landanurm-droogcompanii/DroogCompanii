package ru.droogcompanii.application.ui.activity.main_screen_2;

import android.app.Activity;
import android.content.Context;
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

import com.google.common.base.Optional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.FavoriteDBUtils;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerCategoriesReader;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerHierarchyContracts;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.ui.util.able_to_start_task.FragmentAbleToStartTask;
import ru.droogcompanii.application.ui.util.able_to_start_task.TaskNotBeInterruptedDuringConfigurationChange;

/**
 * Created by ls on 14.03.14.
 */
public class CategoryListFragment extends FragmentAbleToStartTask implements AdapterView.OnItemClickListener {


    public static interface Callbacks {
        void onReceivingCategoriesTaskCompleted();
        void onListInitialized();
        void onCurrentCategoryChanged();
    }


    private static final Callbacks DUMMY_CALLBACKS = new Callbacks() {

        @Override
        public void onReceivingCategoriesTaskCompleted() {
            // do nothing
        }

        @Override
        public void onListInitialized() {
            // do nothing
        }

        @Override
        public void onCurrentCategoryChanged() {
            // do nothing
        }
    };

    private static interface ListItemHelper {
        View makeView(Context context, View convertView);
        String getTitle(Context context);
        String getConditionToReceivePartners();
    }

    private static abstract class ListItemHelperImpl implements ListItemHelper, Serializable {

        public static final int ROW_LAYOUT_ID = R.layout.view_list_item_category;

        public View makeView(Context context, View convertView) {
            View itemView = (convertView != null) ? convertView : inflateView(context);
            TextView textView = (TextView) itemView.findViewById(R.id.text);
            textView.setText(getTitle(context));
            ImageView imageView = (ImageView) itemView.findViewById(R.id.icon);
            init(imageView);
            return itemView;
        }

        private View inflateView(Context context) {
            LayoutInflater inflater = LayoutInflater.from(context);
            return inflater.inflate(ROW_LAYOUT_ID, null);
        }

        public abstract String getTitle(Context context);

        protected abstract void init(ImageView imageView);

        public abstract String getConditionToReceivePartners();
    }

    private static class ListItemHelperBuilder {

        public static ListItemHelper getFavoriteListItemHelper() {
            return new ListItemHelperImpl() {
                @Override
                public String getTitle(Context context) {
                    return context.getString(R.string.favorite);
                }

                @Override
                protected void init(ImageView imageView) {
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
                protected void init(ImageView imageView) {
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
                protected void init(ImageView imageView) {
                    imageView.setImageDrawable(null);
                }

                @Override
                public String getConditionToReceivePartners() {
                    return "";
                }
            };
        }
    }

    private static class CategoryListAdapter extends ArrayAdapter<ListItemHelper> {

        private final CategoryListFragment fragment;

        public CategoryListAdapter(CategoryListFragment fragment, int rowLayoutId, List<ListItemHelper> items) {
            super(fragment.getActivity(), rowLayoutId, items);
            this.fragment = fragment;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListItemHelper itemHelper = getItem(position);
            View itemView = itemHelper.makeView(getContext(), convertView);
            if (fragment.currentSelection == position) {
                itemView.setSelected(true);
            }
            return itemView;
        }
    }


    private static final int TASK_REQUEST_CODE = 142;

    private static final String KEY_HELPERS = "KEY_HELPERS";

    private static final String KEY_CURRENT_SELECTION = "KEY_CURRENT_SELECTION";


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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            initStateByDefault();
        } else {
            restoreState(savedInstanceState);
        }
    }

    private void initStateByDefault() {
        currentSelection = ListView.INVALID_POSITION;
        listItemHelpers = null;
    }

    private void restoreState(Bundle savedInstanceState) {
        currentSelection = savedInstanceState.getInt(KEY_CURRENT_SELECTION);
        listItemHelpers = (List<ListItemHelper>) savedInstanceState.getSerializable(KEY_HELPERS);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_HELPERS, (Serializable) listItemHelpers);
        outState.putInt(KEY_CURRENT_SELECTION, currentSelection);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listView = (ListView) inflater.inflate(R.layout.fragment_category_list, null);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(this);
        return listView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View itemView, int position, long id) {
        itemView.setSelected(true);
        currentSelection = position;
        callbacks.onCurrentCategoryChanged();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            startTaskReceivingCategories();
        } else if (isListItemsReceived()) {
            initList();
        }
    }

    private boolean isListItemsReceived() {
        return (listItemHelpers != null);
    }

    private void startTaskReceivingCategories() {
        startTask(TASK_REQUEST_CODE, new TaskNotBeInterruptedDuringConfigurationChange() {
            @Override
            protected Serializable doInBackground(Void... voids) {
                return (Serializable) prepareListItemHelpers();
            }
        });
    }

    private List<ListItemHelper> prepareListItemHelpers() {
        final ArrayList<ListItemHelper> helpers = new ArrayList<ListItemHelper>();
        helpers.add(ListItemHelperBuilder.getAllPartnersListItemHelper());
        helpers.add(ListItemHelperBuilder.getFavoriteListItemHelper());
        readCategoriesAndAddHelpers(helpers);
        return helpers;
    }

    private void readCategoriesAndAddHelpers(ArrayList<ListItemHelper> helpers) {
        PartnerCategoriesReader reader = new PartnerCategoriesReader(getActivity());
        List<PartnerCategory> categories = reader.getAllPartnerCategories();
        helpers.ensureCapacity(categories.size() + helpers.size());
        for (PartnerCategory each : categories) {
            helpers.add(ListItemHelperBuilder.getPartnerCategoryListItemHelper(each));
        }
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
        callbacks.onReceivingCategoriesTaskCompleted();
    }

    private void initList() {
        adapter = new CategoryListAdapter(this, ListItemHelperImpl.ROW_LAYOUT_ID, listItemHelpers);
        listView.setAdapter(adapter);
        callbacks.onListInitialized();
    }

    @Override
    public void onResume() {
        super.onResume();
        restoreSelectionIfNeed();
    }

    private void restoreSelectionIfNeed() {
        if (currentSelection != ListView.INVALID_POSITION && listView.getSelectedItemPosition() != currentSelection) {
            listView.smoothScrollToPosition(currentSelection);
            listView.setSelection(currentSelection);
        }
    }

    public Optional<String> getConditionToReceivePartners() {
        if (currentSelection == ListView.INVALID_POSITION) {
            return Optional.absent();
        }
        ListItemHelper item = listItemHelpers.get(currentSelection);
        String condition = item.getConditionToReceivePartners();
        return Optional.of(condition);
    }

    public String getSelectedCategoryName() {
        ListItemHelper item = listItemHelpers.get(currentSelection);
        return item.getTitle(getActivity());
    }
}
