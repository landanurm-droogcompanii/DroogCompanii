package ru.droogcompanii.application.ui.activity.main_screen_2;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
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

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.FavoriteDBUtils;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerCategoriesReader;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerHierarchyContracts;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.ui.util.able_to_start_task.FragmentAbleToStartTask;
import ru.droogcompanii.application.ui.util.able_to_start_task.TaskNotBeInterruptedDuringConfigurationChange;
import ru.droogcompanii.application.util.LogUtils;
import ru.droogcompanii.application.util.Snorlax;

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

    private static class CategoryListItemSelector {
        private static final int SELECTED_BACKGROUND = R.color.backgroundOfCheckedCategoryListItem;
        private static final int UNSELECTED_BACKGROUND = android.R.color.transparent;

        private final CategoryListFragment fragment;

        private Optional<View> lastSelectedItem;

        public CategoryListItemSelector(CategoryListFragment fragment) {
            this.fragment = fragment;
            this.lastSelectedItem = Optional.absent();
        }

        public void select(View itemView) {
            if (lastSelectedItem.isPresent()) {
                lastSelectedItem.get().setBackgroundResource(UNSELECTED_BACKGROUND);
            }
            itemView.setBackgroundResource(SELECTED_BACKGROUND);
            lastSelectedItem = Optional.of(itemView);
        }

        public void unselect(View itemView) {
            itemView.setBackgroundResource(UNSELECTED_BACKGROUND);
            if (lastSelectedItem.isPresent() && CategoryListAdapter.areAtTheSamePosition(lastSelectedItem.get(), itemView)) {
                lastSelectedItem.get().setBackgroundResource(UNSELECTED_BACKGROUND);
                lastSelectedItem = Optional.absent();
            }
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
            itemView.setTag(Integer.valueOf(position));
            updateSelection(itemView, position);
            return itemView;
        }

        private void updateSelection(View itemView, int position) {
            if (position == fragment.currentSelection) {
                fragment.selector.select(itemView);
            } else {
                fragment.selector.unselect(itemView);
            }
        }

        public static boolean areAtTheSamePosition(View itemView1, View itemView2) {
            int position1 = (Integer) itemView1.getTag();
            int position2 = (Integer) itemView2.getTag();
            return position1 == position2;
        }
    }


    private static final int TASK_REQUEST_CODE = 142;

    private static final String KEY_HELPERS = "KEY_HELPERS";
    private static final String KEY_CURRENT_SELECTION = "KEY_CURRENT_SELECTION";
    private static final String KEY_LIST_VIEW_STATE = "KEY_LIST_VIEW_STATE";


    private final CategoryListItemSelector selector = new CategoryListItemSelector(this);

    private Callbacks callbacks;
    private CategoryListAdapter adapter;
    private int currentSelection;
    private List<ListItemHelper> listItemHelpers;
    private ListView listView;
    private Optional<Parcelable> stateOfListView;


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
        currentSelection = 0;
        listItemHelpers = null;
        stateOfListView = Optional.absent();
    }

    private void restoreState(Bundle savedInstanceState) {
        currentSelection = savedInstanceState.getInt(KEY_CURRENT_SELECTION);
        listItemHelpers = (List<ListItemHelper>) savedInstanceState.getSerializable(KEY_HELPERS);
        stateOfListView = Optional.of(savedInstanceState.getParcelable(KEY_LIST_VIEW_STATE));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_HELPERS, (Serializable) listItemHelpers);
        outState.putInt(KEY_CURRENT_SELECTION, currentSelection);
        outState.putParcelable(KEY_LIST_VIEW_STATE, listView.onSaveInstanceState());
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
        currentSelection = position;
        selector.select(itemView);
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


    private static int counter = 1;

    private void startTaskReceivingCategories() {
        startTask(TASK_REQUEST_CODE, new TaskNotBeInterruptedDuringConfigurationChange() {
            @Override
            protected Serializable doInBackground(Void... voids) {
                debug();
                return (Serializable) prepareListItemHelpers();
            }
        });
    }

    private static void debug() {
        Snorlax.sleep(3000L);
        LogUtils.debug("Counter: " + counter);
        counter += 1;
    }

    private List<ListItemHelper> prepareListItemHelpers() {
        final ArrayList<ListItemHelper> helpers = new ArrayList<ListItemHelper>();
        helpers.add(ListItemHelperBuilder.getAllPartnersListItemHelper());
        helpers.add(ListItemHelperBuilder.getFavoriteListItemHelper());
        readCategoriesAndAddHelpers(helpers);
        return helpers;
    }

    private void readCategoriesAndAddHelpers(ArrayList<ListItemHelper> helpers) {
        PartnerCategoriesReader reader = new PartnerCategoriesReader(DroogCompaniiApplication.getContext());
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
        if (stateOfListView.isPresent()) {
            listView.onRestoreInstanceState(stateOfListView.get());
            //stateOfListView = Optional.absent();
        }
        //restoreListViewState();
        callbacks.onListInitialized();
    }

    private void restoreListViewState() {
        if (currentSelection != ListView.INVALID_POSITION) {
            listView.post(new Runnable() {
                @Override
                public void run() {
                    listView.smoothScrollToPosition(currentSelection);
                }
            });
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
