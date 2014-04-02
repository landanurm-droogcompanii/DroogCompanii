package ru.droogcompanii.application.ui.screens.main.category_list;

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
import java.util.Arrays;
import java.util.List;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerCategoriesReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.util.ListUtils;
import ru.droogcompanii.application.util.StateManager;
import ru.droogcompanii.application.util.ui.able_to_start_task.FragmentAbleToStartTask;
import ru.droogcompanii.application.util.ui.able_to_start_task.TaskNotBeInterruptedDuringConfigurationChange;

/**
 * Created by ls on 14.03.14.
 */
public class CategoryListFragment extends FragmentAbleToStartTask implements AdapterView.OnItemClickListener {

    public static interface Callbacks {
        void onReceivingCategoriesTaskCompleted();
        void onListInitialized();
        void onCurrentCategoryChanged();
    }

    public static enum Mode {
        DEFAULT(
            Arrays.asList(
                ListItemHelperBuilder.getAllPartnersListItemHelper(),
                ListItemHelperBuilder.getFavoriteListItemHelper()
            )
        ),

        WITHOUT_ALL_PARTNERS(
            Arrays.asList(
                ListItemHelperBuilder.getFavoriteListItemHelper()
            )
        );

        private final List<ListItemHelper> helpers;
        private boolean showSelection;

        Mode(List<ListItemHelper> helpers) {
            this.helpers = helpers;
            this.showSelection = true;
        }

        List<ListItemHelper> getHelpers() {
            return helpers;
        }

        public Mode doNotShowSelection() {
            showSelection = false;
            return this;
        }

        boolean isShowSelection() {
            return showSelection;
        }
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

    private static class TaskRequestCode {
        public static final int RECEIVING_CATEGORIES = 142;
    }

    private static class Key {
        public static final String HELPERS = "KEY_HELPERS";
        public static final String CURRENT_SELECTION = "KEY_CURRENT_SELECTION";
        public static final String LIST_VIEW_STATE = "KEY_LIST_VIEW_STATE";
        public static final String MODE = "MODE";
    }

    private Callbacks callbacks;
    private CategoryListAdapter adapter;
    private CategoryListItemSelector selector;
    private int currentSelection;
    private ListView listView;
    private Mode mode;
    private Optional<List<ListItemHelper>> listItemHelpers;
    private Optional<Parcelable> stateOfListView;


    private final StateManager STATE_MANAGER = new StateManager() {

        @Override
        public void initStateByDefault() {
            currentSelection = 0;
            listItemHelpers = Optional.absent();
            stateOfListView = Optional.absent();
            mode = (Mode) getArguments().getSerializable(Key.MODE);
            selector = createListItemSelectorByMode(mode);
        }

        private CategoryListItemSelector createListItemSelectorByMode(Mode mode) {
            return mode.isShowSelection()
                    ? new ActualCategoryListItemSelector()
                    : new DummyCategoryListItemSelector();
        }

        @Override
        public void restoreState(Bundle savedInstanceState) {
            currentSelection = savedInstanceState.getInt(Key.CURRENT_SELECTION);
            listItemHelpers = (Optional<List<ListItemHelper>>) savedInstanceState.getSerializable(Key.HELPERS);
            stateOfListView = Optional.of(savedInstanceState.getParcelable(Key.LIST_VIEW_STATE));
            mode = (Mode) savedInstanceState.getSerializable(Key.MODE);
            selector = createListItemSelectorByMode(mode);
        }

        @Override
        public void saveState(Bundle outState) {
            outState.putInt(Key.CURRENT_SELECTION, currentSelection);
            outState.putSerializable(Key.HELPERS, listItemHelpers);
            outState.putParcelable(Key.LIST_VIEW_STATE, listView.onSaveInstanceState());
            outState.putSerializable(Key.MODE, mode);
        }
    };

    public static CategoryListFragment newDefaultInstance() {
        return newInstance(Mode.DEFAULT);
    }

    public static CategoryListFragment newInstanceWithoutAllPartners() {
        return newInstance(Mode.WITHOUT_ALL_PARTNERS);
    }

    public static CategoryListFragment newInstance(Mode mode) {
        CategoryListFragment fragment = new CategoryListFragment();
        Bundle args = new Bundle();
        args.putSerializable(Key.MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }


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
        } else if (listItemHelpers.isPresent()) {
            initList();
        }
    }

    private void startTaskReceivingCategories() {
        startTask(TaskRequestCode.RECEIVING_CATEGORIES, new TaskNotBeInterruptedDuringConfigurationChange() {
            @Override
            protected Serializable doInBackground(Void... voids) {
                return (Serializable) prepareListItemHelpers();
            }
        });
    }

    private List<ListItemHelper> prepareListItemHelpers() {
        final List<ListItemHelper> helpers = new ArrayList<ListItemHelper>();
        helpers.addAll(mode.getHelpers());
        addPartnerCategoryHelpersIn(helpers);
        return helpers;
    }

    private void addPartnerCategoryHelpersIn(List<ListItemHelper> helpers) {
        List<PartnerCategory> categories = readAllPartnerCategories();
        ListUtils.ensureCapacityOfArrayListByCount(helpers, categories.size());
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
        if (resultCode != Activity.RESULT_OK) {
            getActivity().finish();
            return;
        }
        if (requestCode == TaskRequestCode.RECEIVING_CATEGORIES) {
            onHelpersReceived(result);
        }
    }

    private void onHelpersReceived(Serializable result) {
        this.listItemHelpers = Optional.fromNullable((List<ListItemHelper>) result);
        initList();
        callbacks.onReceivingCategoriesTaskCompleted();
    }

    private void initList() {
        adapter = new CategoryListAdapter(this, listItemHelpers.get());
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
        if (isNoSelection()) {
            return Optional.absent();
        }
        ListItemHelper item = listItemHelpers.get().get(currentSelection);
        String condition = item.getConditionToReceivePartners();
        return Optional.of(condition);
    }

    private boolean isNoSelection() {
        return (currentSelection < 0) || (currentSelection >= listItemHelpers.get().size());
    }

    public Optional<String> getSelectedCategoryName() {
        if (isNoSelection()) {
            return Optional.absent();
        }
        ListItemHelper item = listItemHelpers.get().get(currentSelection);
        String title = item.getTitle(getActivity());
        return Optional.of(title);
    }

    public void setCategorySize(int size) {
        // TODO
    }

    public void setCategorySizeIsUnknown() {
        // TODO
    }
}
