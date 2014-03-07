package ru.droogcompanii.application.ui.fragment.offer_list;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.google.common.base.Optional;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.db_util.DBUtils;
import ru.droogcompanii.application.data.db_util.offers.OffersContract;
import ru.droogcompanii.application.data.db_util.offers.SpecialOffersDBUtils;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.ui.activity.offer_list.OfferListActivity;
import ru.droogcompanii.application.ui.activity.offer_list.OffersReceiverTask;
import ru.droogcompanii.application.ui.util.able_to_start_task.FragmentAbleToStartTask;
import ru.droogcompanii.application.ui.util.able_to_start_task.TaskNotBeInterrupted;
import ru.droogcompanii.application.util.CalendarUtils;

/**
 * Created by ls on 10.02.14.
 */
public class OfferListFragment extends FragmentAbleToStartTask implements AdapterView.OnItemClickListener {

    private static final String KEY_IS_NEED_TO_START_TASK = "KEY_IS_NEED_TO_START_TASK";
    private boolean isNeedToStartTask;

    public static enum OfferType {
        ALL,
        SPECIAL,
        ACTUAL,
        PAST
    }

    private static final int TASK_REQUEST_CODE_RECEIVING_OFFERS = 461;

    public static interface Callbacks {
        void onOfferItemClick(Offer offer);
    }

    private static final String KEY_OFFERS = "KEY_OFFERS";

    public static Fragment newInstance(OfferType offerType, String where) {
        if (offerType != OfferType.ALL) {
            where = combineConditions(offerType, where);
        }
        return newInstance(where);
    }

    private static String combineConditions(OfferType offerType, String where) {
        String offerTypeCondition = getConditionByOfferType(offerType);
        if (where.trim().length() > 0) {
            return DBUtils.combineConditions("AND", where, offerTypeCondition);
        }
        return offerTypeCondition;
    }

    private static String getConditionByOfferType(OfferType offerType) {
        switch (offerType) {
            case SPECIAL:
                return prepareConditionForSpecialOffers();

            case ACTUAL:
                return prepareConditionForActualOffers();

            case PAST:
                return prepareConditionForPastOffers();

            default:
                throw new IllegalArgumentException("Unknown offer type: " + offerType);
        }
    }

    private static String prepareConditionForSpecialOffers() {
        return OffersContract.COLUMN_NAME_FROM + " = " + SpecialOffersDBUtils.getFrom() +
                " AND " + OffersContract.COLUMN_NAME_TO + " = " + SpecialOffersDBUtils.getTo();
    }

    private static String prepareConditionForActualOffers() {
        long now = CalendarUtils.now().getTimeInMillis();
        return SpecialOffersDBUtils.getTo() + " != " + OffersContract.COLUMN_NAME_TO + " AND " +
                now + " <= " + OffersContract.COLUMN_NAME_TO;
    }

    private static String prepareConditionForPastOffers() {
        long now = CalendarUtils.now().getTimeInMillis();
        return SpecialOffersDBUtils.getTo() + " != " + OffersContract.COLUMN_NAME_TO + " AND " +
                now + " > " + OffersContract.COLUMN_NAME_TO;
    }

    private static Fragment newInstance(String where) {
        OfferListFragment fragment = new OfferListFragment();
        Bundle args = new Bundle();
        args.putString(OfferListActivity.KEY_WHERE, where);
        fragment.setArguments(args);
        return fragment;
    }

    private ArrayAdapter<Offer> adapter;
    private Callbacks callbacks;
    private GridView gridView;
    private Optional<List<Offer>> optionalOffers;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (Callbacks) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_offers, null);
        gridView = (GridView) root.findViewById(R.id.gridView);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            initStateByDefault();
        } else {
            restoreState(savedInstanceState);
        }
        initList();
    }

    private void initStateByDefault() {
        optionalOffers = Optional.absent();
        isNeedToStartTask = true;
    }

    private void restoreState(Bundle savedInstanceState) {
        optionalOffers = (Optional<List<Offer>>) savedInstanceState.getSerializable(KEY_OFFERS);
        isNeedToStartTask = savedInstanceState.getBoolean(KEY_IS_NEED_TO_START_TASK);
    }

    private void initList() {
        gridView.setOnItemClickListener(this);
        if (optionalOffers.isPresent()) {
            initList(optionalOffers.get());
        }
    }

    private void initList(List<Offer> offers) {
        gridView.setEmptyView(getEmptyListView());
        adapter = new OffersAdapter(getActivity(), offers);
        gridView.setAdapter(adapter);
    }

    private View getEmptyListView() {
        return getView().findViewById(R.id.noOffersView);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isNeedToStartTask) {
            isNeedToStartTask = false;
            startTaskReceivingOffers();
        }
    }

    private void startTaskReceivingOffers() {
        String where = getArguments().getString(OfferListActivity.KEY_WHERE);
        TaskNotBeInterrupted task = new OffersReceiverTask(where, getActivity());
        startTask(TASK_REQUEST_CODE_RECEIVING_OFFERS, task);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveStateInto(outState);
    }

    private void saveStateInto(Bundle outState) {
        outState.putSerializable(KEY_OFFERS, optionalOffers);
        outState.putBoolean(KEY_IS_NEED_TO_START_TASK, isNeedToStartTask);
    }

    public void setOffers(Serializable result) {
        List<Offer> offers = (List<Offer>) result;
        setOffers(offers);
    }

    private void setOffers(List<Offer> offers) {
        this.optionalOffers = Optional.of(offers);
        initList();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Offer offer = adapter.getItem(position);
        callbacks.onOfferItemClick(offer);
    }

    @Override
    public void onTaskResult(int requestCode, int resultCode, Serializable result) {
        if (requestCode == TASK_REQUEST_CODE_RECEIVING_OFFERS) {
            onReceivingOffersTaskResult(resultCode, result);
        }
    }

    private void onReceivingOffersTaskResult(int resultCode, Serializable result) {
        if (resultCode == Activity.RESULT_OK) {
            setOffers(result);
        } else {
            getActivity().finish();
        }
    }
}
