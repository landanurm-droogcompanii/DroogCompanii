package ru.droogcompanii.application.ui.fragment.help;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.util.view.IconAndLabelItemMaker;
import ru.droogcompanii.application.util.HotlineNumbersProvider;
import ru.droogcompanii.application.ui.util.workers.NavigatorToWebsite;
import ru.droogcompanii.application.util.WebsiteAddressProvider;
import ru.droogcompanii.application.ui.util.workers.caller.CallerHelper;

/**
 * Created by ls on 14.02.14.
 */
public class HelpFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        ViewGroup rootLayout = (ViewGroup) view.findViewById(R.id.rootLayout);
        fill(rootLayout);
        return view;
    }

    private void fill(ViewGroup viewGroup) {
        IconAndLabelItemMaker inflater = new IconAndLabelItemMaker(getActivity());
        viewGroup.addView(prepareHotlineView(inflater));
        viewGroup.addView(prepareWebsiteView(inflater));
    }

    private View prepareHotlineView(IconAndLabelItemMaker inflater) {
        final String title = getString(R.string.hotline);
        return inflater.make(R.drawable.phone, title, new Runnable() {
            @Override
            public void run() {
                CallerHelper callerHelper = new CallerHelper(getActivity());
                callerHelper.call(title, HotlineNumbersProvider.get());
            }
        });
    }

    private View prepareWebsiteView(IconAndLabelItemMaker inflater) {
        return inflater.make(R.drawable.ic_web_site, getString(R.string.website), new Runnable() {
            @Override
            public void run() {
                NavigatorToWebsite helper = new NavigatorToWebsite(getActivity());
                helper.navigateToSite(WebsiteAddressProvider.getWebsiteAddress());
            }
        });
    }
}
