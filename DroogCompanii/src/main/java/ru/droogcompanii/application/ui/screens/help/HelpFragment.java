package ru.droogcompanii.application.ui.screens.help;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.util.constants.HotlineNumbersProvider;
import ru.droogcompanii.application.util.constants.WebsiteAddressProvider;
import ru.droogcompanii.application.util.ui.view.IconAndLabelItemMaker;
import ru.droogcompanii.application.util.workers.NavigatorToWebsite;
import ru.droogcompanii.application.util.workers.caller.CallerHelper;

/**
 * Created by ls on 14.02.14.
 */
public class HelpFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_help, container, false);
        ViewFiller viewFiller = new ViewFiller();
        viewFiller.fill(view);
        return view;
    }

    private class ViewFiller {
        final IconAndLabelItemMaker inflater;

        ViewFiller() {
            inflater = new IconAndLabelItemMaker(getActivity());
        }

        void fill(ViewGroup viewGroup) {
            viewGroup.addView(prepareHotlineView());
            viewGroup.addView(prepareWebsiteView());
        }

        private View prepareHotlineView() {
            final String title = getString(R.string.hotline);
            return inflater.make(R.drawable.phone, title, new Runnable() {
                @Override
                public void run() {
                    CallerHelper callerHelper = new CallerHelper(getActivity());
                    callerHelper.call(title, HotlineNumbersProvider.get());
                }
            });
        }

        private View prepareWebsiteView() {
            return inflater.make(R.drawable.ic_web_site, getString(R.string.website), new Runnable() {
                @Override
                public void run() {
                    NavigatorToWebsite helper = new NavigatorToWebsite(getActivity());
                    helper.navigateToSite(WebsiteAddressProvider.getWebsiteAddress());
                }
            });
        }

    }
}
