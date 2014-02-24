package ru.droogcompanii.application.ui.fragment.search_result_list;

import android.os.AsyncTask;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchResult;
import ru.droogcompanii.application.util.Snorlax;
import ru.droogcompanii.application.util.WeakReferenceWrapper;

/**
 * Created by ls on 24.02.14.
 */
public class SortTask extends AsyncTask<Void, Void, Void> {
    private final WeakReferenceWrapper<List<SearchResult<Partner>>> wrapperOfListToSort;
    private final Comparator<Partner> comparator;
    private final Runnable runnable;

    public SortTask(List<SearchResult<Partner>> searchResults,
                    Comparator<Partner> comparator,
                    Runnable runnableOnPostExecute) {
        this.wrapperOfListToSort = new WeakReferenceWrapper<List<SearchResult<Partner>>>(searchResults);
        this.comparator = comparator;
        this.runnable = runnableOnPostExecute;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Snorlax.sleep();
        wrapperOfListToSort.handleIfExist(
                new WeakReferenceWrapper.Handler<List<SearchResult<Partner>>>() {
                    @Override
                    public void handle(List<SearchResult<Partner>> searchResults) {
                        Collections.sort(searchResults, new Comparator<SearchResult<Partner>>() {
                            @Override
                            public int compare(SearchResult<Partner> res1, SearchResult<Partner> res2) {
                                return comparator.compare(res1.value(), res2.value());
                            }
                        });
                    }
                }
        );
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        wrapperOfListToSort.handleIfExist(new WeakReferenceWrapper.Handler<List<SearchResult<Partner>>>() {
            @Override
            public void handle(List<SearchResult<Partner>> ref) {
                runnable.run();
            }
        });
    }
}