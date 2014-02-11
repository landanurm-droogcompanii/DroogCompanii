package ru.droogcompanii.application.ui.activity.offer_list.offers_provider;

import android.content.Context;
import android.content.res.Resources;

import java.io.InputStream;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.data.xml_parser.offers_xml_parser.OffersXmlParser;
import ru.droogcompanii.application.util.LogUtils;

/**
 * Created by ls on 10.02.14.
 */
public class DummyOffersDownloader implements OffersProvider {

    private final Context context;
    private final OffersXmlParser parser;

    public DummyOffersDownloader(Context context) {
        this.context = context;
        parser = new OffersXmlParser();
    }

    public Result getOffers() {
        try {
            List<Offer> parsedOffers = parser.parse(prepareInputStream());
            return new OffersResultImpl(parsedOffers);
        } catch (Exception e) {
            LogUtils.debug(e.getMessage());
            return OffersResultImpl.noOffers();
        }
    }

    private InputStream prepareInputStream() {
        Resources resources = context.getResources();
        return resources.openRawResource(R.raw.test_offers);
    }
}
