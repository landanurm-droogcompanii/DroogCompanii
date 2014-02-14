package ru.droogcompanii.application.ui.activity.partner_details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.activity.base_menu_helper.MenuHelper;
import ru.droogcompanii.application.ui.activity.base_menu_helper.MenuHelperItemsProvider;
import ru.droogcompanii.application.ui.activity.base_menu_helper.menu_item_helper.MenuItemHelper;
import ru.droogcompanii.application.ui.activity.base_menu_helper.menu_item_helper.MenuItemHelpers;
import ru.droogcompanii.application.ui.fragment.partner_details.PartnerDetailsFragment;
import ru.droogcompanii.application.ui.fragment.partner_point_list.PartnerPointListFragment;
import ru.droogcompanii.application.ui.fragment.partner_points_map.PartnerPointsProvider;
import ru.droogcompanii.application.ui.helpers.ActionBarActivityWithGoToMapItem;
import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;
import ru.droogcompanii.application.ui.util.PartnerPointsProviderHolder;

/**
 * Created by ls on 15.01.14.
 */
public class PartnerDetailsActivity extends ActionBarActivityWithGoToMapItem
                     implements PartnerPointListFragment.Callbacks,
                                TaskFragmentHolder.Callbacks,
                                PartnerPointsProviderHolder {

    private static final String TAG_TASK_FRAGMENT_HOLDER = "TaskFragmentHolder";

    public static interface PartnerAndPartnerPointsProvider {
        Partner getPartner(Context context);
        List<PartnerPoint> getPartnerPoints(Context context);
    }

    public static class Key {
        public static final String PARTNER = "partner";
        public static final String PARTNER_POINT = PARTNER + "point";
        public static final String PARTNER_POINTS = PARTNER_POINT + "s";
        public static final String PARTNER_AND_PARTNER_POINTS_PROVIDER = PARTNER + PARTNER_POINTS + " provider";
        public static final String RESULT_FROM_TASK = "result from task";
    }

    private static final String LIST_FRAGMENT_TAG = "List Fragment Tag";
    private static final String DETAILS_FRAGMENT_TAG = "Details Fragment Tag";


    private PartnerDetailsTask.Result resultFromTask;


    public static void start(Context context, PartnerAndPartnerPointsProvider partnerAndPartnerPointsProvider) {
        Intent intent = new Intent(context, PartnerDetailsActivity.class);
        intent.putExtra(Key.PARTNER_AND_PARTNER_POINTS_PROVIDER, (Serializable) partnerAndPartnerPointsProvider);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner);

        if (savedInstanceState == null) {
            startTask();
        } else {
            restoreState(savedInstanceState);
        }
    }

    private void restoreState(Bundle savedInstanceState) {
        resultFromTask = (PartnerDetailsTask.Result)
                savedInstanceState.getSerializable(Key.RESULT_FROM_TASK);
        initTitle();
    }

    private void initTitle() {
        if (resultFromTask != null && resultFromTask.partner != null) {
            setTitle(resultFromTask.partner.getTitle());
        }
    }

    private void startTask() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.taskFragmentContainer, prepareTaskFragment(), TAG_TASK_FRAGMENT_HOLDER);
        transaction.commit();
    }

    private Fragment prepareTaskFragment() {
        Fragment taskFragment = new PartnerDetailsTaskFragmentHolder();
        taskFragment.setArguments(prepareArgumentsForTaskFragment());
        return taskFragment;
    }

    private Bundle prepareArgumentsForTaskFragment() {
        Intent intent = getIntent();
        Serializable provider = intent.getSerializableExtra(Key.PARTNER_AND_PARTNER_POINTS_PROVIDER);
        Bundle args = new Bundle();
        args.putSerializable(Key.PARTNER_AND_PARTNER_POINTS_PROVIDER, provider);
        return args;
    }

    @Override
    public void onTaskFinished(int resultCode, Serializable result) {
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }
        resultFromTask = (PartnerDetailsTask.Result) result;
        init();
    }

    @Override
    public void onPartnerPointClick(PartnerPoint partnerPoint) {
        startPartnerDetailsFragment(partnerPoint);
    }

    private void init() {
        if (isNeedToShowPartnerPointListFirst()) {
            startPartnerPointListFragment();
        } else {
            startPartnerDetailsFragment(onlyExistingPartnerPoint());
        }
        updateGoToMapItemVisible();
        initTitle();
    }

    private boolean isNeedToShowPartnerPointListFirst() {
        List<PartnerPoint> partnerPoints = resultFromTask.partnerPoints;
        return (partnerPoints != null) && (partnerPoints.size() > 1);
    }

    private void startPartnerPointListFragment() {
        PartnerPointListFragment partnerPointListFragment = preparePartnerPointListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.containerOfFragment, partnerPointListFragment, LIST_FRAGMENT_TAG);
        transaction.commit();
    }

    private PartnerPointListFragment preparePartnerPointListFragment() {
        PartnerPointListFragment fragment = new PartnerPointListFragment();
        Bundle args = new Bundle();
        args.putSerializable(Key.PARTNER_POINTS, (Serializable) resultFromTask.partnerPoints);
        fragment.setArguments(args);
        return fragment;
    }

    private PartnerPoint onlyExistingPartnerPoint() {
        return resultFromTask.partnerPoints.get(0);
    }

    private void startPartnerDetailsFragment(PartnerPoint partnerPoint) {
        PartnerDetailsFragment partnerDetailsFragment = preparePartnerDetailsFragment(partnerPoint);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (isListFragmentDisplayed()) {
            transaction.replace(R.id.containerOfFragment, partnerDetailsFragment, DETAILS_FRAGMENT_TAG);
            transaction.addToBackStack(null);
        } else {
            transaction.add(R.id.containerOfFragment, partnerDetailsFragment, DETAILS_FRAGMENT_TAG);
        }
        transaction.commit();
    }

    private PartnerDetailsFragment preparePartnerDetailsFragment(PartnerPoint partnerPoint) {
        PartnerDetailsFragment partnerDetailsFragment = new PartnerDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(Key.PARTNER, (Serializable) resultFromTask.partner);
        args.putSerializable(Key.PARTNER_POINT, (Serializable) partnerPoint);
        partnerDetailsFragment.setArguments(args);
        return partnerDetailsFragment;
    }

    private boolean isListFragmentDisplayed() {
        return getSupportFragmentManager().findFragmentByTag(LIST_FRAGMENT_TAG) != null;
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable(Key.RESULT_FROM_TASK, resultFromTask);
    }

    @Override
    protected boolean isGoToMapItemVisible() {
        return (resultFromTask != null) &&
                (resultFromTask.partnerPoints != null) &&
                !resultFromTask.partnerPoints.isEmpty();
    }

    @Override
    protected MenuHelper getMenuHelper() {
        return new MenuHelperItemsProvider(this) {
            @Override
            protected MenuItemHelper[] getMenuItemHelpers() {
                return new MenuItemHelper[] {
                        MenuItemHelpers.GO_TO_MAP,
                        MenuItemHelpers.PERSONAL_ACCOUNT,
                        MenuItemHelpers.SETTINGS,
                        MenuItemHelpers.HELP
                };
            }
        };
    }

    @Override
    public PartnerPointsProvider getPartnerPointsProvider() {
        return new PartnerPointsProviderImpl(resultFromTask.partner, getPartnerPointsDisplayedNow());
    }

    private List<PartnerPoint> getPartnerPointsDisplayedNow() {
        PartnerPointsProvider partnerPointsProvider = (PartnerPointsProvider) findDisplayedFragment();
        return partnerPointsProvider.getPartnerPoints(this);
    }

    private Fragment findDisplayedFragment() {
        return getDetailsFragmentIfItIsDisplayedAndListFragmentOtherwise();
    }

    private Fragment getDetailsFragmentIfItIsDisplayedAndListFragmentOtherwise() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(DETAILS_FRAGMENT_TAG);
        if (fragment == null) {
            fragment = getSupportFragmentManager().findFragmentByTag(LIST_FRAGMENT_TAG);
        }
        return fragment;
    }

    private static class PartnerPointsProviderImpl implements PartnerPointsProvider, Serializable {
        private final Partner partner;
        private final List<PartnerPoint> partnerPoints;

        PartnerPointsProviderImpl(Partner partner, List<PartnerPoint> partnerPoints) {
            this.partner = partner;
            this.partnerPoints = partnerPoints;
        }

        @Override
        public String getTitle(Context context) {
            return partner.getTitle();
        }

        @Override
        public List<PartnerPoint> getPartnerPoints(Context context) {
            return partnerPoints;
        }
    }

}
