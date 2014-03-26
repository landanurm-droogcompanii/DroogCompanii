package ru.droogcompanii.application.ui.main_screen_2.category_list;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.common.base.Optional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerCategoriesReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.ui.util.StateManager;
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

    private static class RequestCode {
        public static final int TASK_RECEIVING_CATEGORIES = 142;
    }

    private static class Key {
        public static final String HELPERS = "KEY_HELPERS";
        public static final String CURRENT_SELECTION = "KEY_CURRENT_SELECTION";
        public static final String LIST_VIEW_STATE = "KEY_LIST_VIEW_STATE";
    }

    private final CategoryListItemSelector selector = new CategoryListItemSelector();
    private Callbacks callbacks;
    private CategoryListAdapter adapter;
    private int currentSelection;
    private List<ListItemHelper> listItemHelpers;
    private ListView listView;
    private Optional<Parcelable> stateOfListView;

    private final StateManager STATE_MANAGER = new StateManager() {
        @Override
        public void initStateByDefault() {
            currentSelection = 0;
            listItemHelpers = null;
            stateOfListView = Optional.absent();
        }

        @Override
        public void restoreState(Bundle savedInstanceState) {
            currentSelection = savedInstanceState.getInt(Key.CURRENT_SELECTION);
            listItemHelpers = (List<ListItemHelper>) savedInstanceState.getSerializable(Key.HELPERS);
            stateOfListView = Optional.of(savedInstanceState.getParcelable(Key.LIST_VIEW_STATE));
        }

        @Override
        public void saveState(Bundle outState) {
            outState.putInt(Key.CURRENT_SELECTION, currentSelection);
            outState.putSerializable(Key.HELPERS, (Serializable) listItemHelpers);
            outState.putParcelable(Key.LIST_VIEW_STATE, listView.onSaveInstanceState());
        }
    };


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
        STATE_MANAGER.initState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        STATE_MANAGER.saveState(outState);
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

    public int getCurrentSelection() {
        return currentSelection;
    }

    public CategoryListItemSelector getSelector() {
        return selector;
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
        startTask(RequestCode.TASK_RECEIVING_CATEGORIES, new TaskNotBeInterruptedDuringConfigurationChange() {
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
        addPartnerCategoryHelpersIn(helpers);
        return helpers;
    }

    private void addPartnerCategoryHelpersIn(ArrayList<ListItemHelper> helpers) {
        List<PartnerCategory> categories = readAllPartnerCategories();
        final int totalSize = categories.size() + helpers.size();
        helpers.ensureCapacity(totalSize);
        for (PartnerCategory each : categories) {
            ListItemHelper eachHelper = ListItemHelperBuilder.getPartnerCategoryListItemHelper(each);
            helpers.add(eachHelper);
        }
    }

    private List<PartnerCategory> readAllPartnerCategories() {
        Context context = DroogCompaniiApplication.getContext();
        PartnerCategoriesReader reader = new PartnerCategoriesReader(context);
        return reader.getAllPartnerCategories();
    }

    @Override
    public void onTaskResult(int requestCode, int resultCode, Serializable result) {
        if (requestCode == RequestCode.TASK_RECEIVING_CATEGORIES) {
            if (resultCode == Activity.RESULT_OK) {
                onHelpersReceived(result);
            } else {
                onReceivingHelpersCancelled();
            }
        }
    }

    private void onReceivingHelpersCancelled() {
        getActivity().finish();
    }

    private void onHelpersReceived(Serializable result) {
        this.listItemHelpers = (List<ListItemHelper>) result;
        initList();
        callbacks.onReceivingCategoriesTaskCompleted();
    }

    private void initList() {
        adapter = new CategoryListAdapter(this, listItemHelpers);
        listView.setAdapter(adapter);
        restoreStateOfListViewIfNeed();
        callbacks.onListInitialized();
    }

    private void restoreStateOfListViewIfNeed() {
        if (stateOfListView.isPresent()) {
            listView.onRestoreInstanceState(stateOfListView.get());
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

    public void setCategorySize(int size) {
        // TODO
    }

    public void setCategorySizeIsUnknown() {
        // TODO
    }
}
