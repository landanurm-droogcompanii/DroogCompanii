package ru.droogcompanii.application.ui.fragment.search_result_list;

import android.app.Activity;
import android.content.Context;
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
import ru.droogcompanii.application.util.WeakReferenceWrapper;

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
            initSearchResultList();
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
        initSearchResultList();
    }

    private void initSearchResultList() {
        WeakReferenceWrapper.Handler<ListFragment> onAfterSort = new WeakReferenceWrapper.Handler<ListFragment>() {
            @Override
            public void handle(ListFragment ref) {
                SearchResultListFragment fragment = (SearchResultListFragment) ref;
                if (fragment.adapter.isEmpty()) {
                    fragment.callbacks.onNotFound();
                    getListView().setEmptyView(prepareEmptyView());
                } else {
                    fragment.callbacks.onFound();
                }
            }
        };
        setSortedAdapter(onAfterSort);
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

    private void setSortedAdapter(final WeakReferenceWrapper.Handler<ListFragment> handler) {
        final WeakReferenceWrapper<ListFragment> fragmentWrapper =
                new WeakReferenceWrapper<ListFragment>(this);
        final WeakReferenceWrapper<Context> contextWrapper =
                new WeakReferenceWrapper<Context>(getActivity());
        SortTask task = new SortTask(searchResults, currentComparator, new Runnable() {
            @Override
            public void run() {
                contextWrapper.handleIfExist(new WeakReferenceWrapper.Handler<Context>() {
                    @Override
                    public void handle(final Context context) {
                        fragmentWrapper.handleIfExist(new WeakReferenceWrapper.Handler<ListFragment>() {
                            @Override
                            public void handle(final ListFragment fragment) {
                                adapter = new PartnerSearchResultListAdapter(context, searchResults);
                                fragment.setListAdapter(adapter);
                                handler.handle(fragment);
                            }
                        });
                    }
                });
            }
        });
        task.execute();
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

    public void setComparator(Comparator<Partner> newComparator) {
        currentComparator = newComparator;
        setSortedAdapter();
    }

    private void setSortedAdapter() {
        final WeakReferenceWrapper.Handler<ListFragment> dummyHandler =
                new WeakReferenceWrapper.Handler<ListFragment>() {
                    @Override
                    public void handle(ListFragment ref) {
                        // do nothing
                    }
                };
        setSortedAdapter(dummyHandler);
    }

}
