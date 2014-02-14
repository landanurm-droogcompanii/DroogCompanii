package ru.droogcompanii.application.ui.activity.offer_list.offers_provider;

import android.content.Context;
import android.content.res.Resources;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.data.xml_parser.offers_xml_parser.OffersXmlParser;
import ru.droogcompanii.application.util.LogUtils;

/**
 * Created by ls on 10.02.14.
 */
public class DummyOffersProviderFromInet implements OffersProvider, Serializable {

    public Result getOffers(Context context) {
        try {
            OffersXmlParser parser = new OffersXmlParser();
            InputStream inputStream = prepareInputStream(context);
            List<Offer> parsedOffers = parser.parse(inputStream);
            return new OffersResultImpl(parsedOffers);
        } catch (Exception e) {
            LogUtils.debug(e.getMessage());
            return OffersResultImpl.noOffers();
        }
    }

    private static InputStream prepareInputStream(Context context) {
        Resources resources = context.getResources();
        return resources.openRawResource(R.raw.test_offers);
    }
}
