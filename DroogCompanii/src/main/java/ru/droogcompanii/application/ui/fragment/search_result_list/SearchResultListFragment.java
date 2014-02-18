package ru.droogcompanii.application.ui.fragment.search_result_list;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchResult;
import ru.droogcompanii.application.util.Keys;

/**
 * Created by ls on 22.01.14.
 */
public class SearchResultListFragment extends ListFragment
        implements AdapterView.OnItemClickListener {

    public static interface Callbacks {
        void onPartnerClick(Partner partner);
        void onFound();
        void onNotFound();
    }

    private static final Comparator<Partner> DUMMY_COMPARATOR = new Comparator<Partner>() {
        @Override
        public int compare(Partner partner1, Partner partner2) {
            return 0;
        }
    };

    private static final String KEY_CURRENT_COMPARATOR = "CurrentComparator";

    private boolean isSearchResultReady;
    private Callbacks callbacks;
    private Comparator<Partner> currentComparator;
    private List<SearchResult<Partner>> searchResults;
    private PartnerSearchResultListAdapter adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (Callbacks) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            initSearchResultIsNotReadyState();
        } else {
            restoreState(savedInstanceState);
        }

        getListView().setOnItemClickListener(this);

        if (isSearchResultReady) {
            updateSearchResultList();
        }
    }

    private void initSearchResultIsNotReadyState() {
        currentComparator = DUMMY_COMPARATOR;
        isSearchResultReady = false;
        searchResults = new ArrayList<SearchResult<Partner>>();
    }

    private void restoreState(Bundle savedInstanceState) {
        currentComparator = (Comparator<Partner>) savedInstanceState.getSerializable(KEY_CURRENT_COMPARATOR);
        searchResults = (List<SearchResult<Partner>>) savedInstanceState.getSerializable(Keys.partners);
        isSearchResultReady = savedInstanceState.getBoolean(Keys.isSearchResultReady);

        int visibility = savedInstanceState.getInt(Keys.visibility);
        getView().setVisibility(visibility);
    }

    private View prepareEmptyView() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View emptyView = layoutInflater.inflate(R.layout.view_no_search_results, null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );
        getActivity().addContentView(emptyView, layoutParams);
        return emptyView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.partners, (Serializable) searchResults);
        outState.putBoolean(Keys.isSearchResultReady, isSearchResultReady);
        outState.putInt(Keys.visibility, getView().getVisibility());
        outState.putSerializable(KEY_CURRENT_COMPARATOR, (Serializable) currentComparator);
    }

    public void setSearchResult(List<SearchResult<Partner>> searchResults) {
        this.searchResults = searchResults;
        isSearchResultReady = true;
        updateSearchResultList();
    }

    private void updateSearchResultList() {
        getListView().setEmptyView(prepareEmptyView());
        adapter = new PartnerSearchResultListAdapter(getActivity(), searchResults);
        sortAdapter();
        setListAdapter(adapter);
        if (adapter.isEmpty()) {
            callbacks.onNotFound();
        } else {
            callbacks.onFound();
        }
    }

    private void sortAdapter() {
        final Comparator<Partner> comparator = currentComparator;
        adapter.sort(new Comparator<SearchResult<Partner>>() {
            @Override
            public int compare(SearchResult<Partner> res1, SearchResult<Partner> res2) {
                return comparator.compare(res1.value(), res2.value());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Partner partner = adapter.getItem(position).value();
        callbacks.onPartnerClick(partner);
    }

    public void show() {
        getView().setVisibility(View.VISIBLE);
    }

    public void hide() {
        getView().setVisibility(View.INVISIBLE);
    }

    public void updateList(Comparator<Partner> newComparator) {
        currentComparator = newComparator;
        sortAdapter();
        adapter.notifyDataSetChanged();
    }

}
