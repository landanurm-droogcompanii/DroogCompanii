package ru.droogcompanii.application.ui.activity.offers_activity;

import android.content.Context;
import android.content.res.Resources;

import java.io.InputStream;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.data.xml_parser.offers_xml_parser.OffersXmlParser;
import ru.droogcompanii.application.util.DummyOfferUtils;
import ru.droogcompanii.application.util.LogUtils;
import ru.droogcompanii.application.util.StringToInputStreamConvertor;

/**
 * Created by ls on 10.02.14.
 */
public class DummyOffersProvider implements OffersProvider {

    private final Context context;

    public DummyOffersProvider(Context context) {
        this.context = context;
    }

    public List<Offer> getOffers() {
        OffersXmlParser parser = new OffersXmlParser();
        try {
            return parser.parse(prepareInputStream());
        } catch (Exception e) {
            LogUtils.debug(e.getMessage());
            // TODO: return something other instead of <null>
            return null;
        }
    }

    private InputStream prepareInputStream() {
        Resources resources = context.getResources();
        return resources.openRawResource(R.raw.test_offers);
    }

    private InputStream prepareDummyInputStream() {
        DummyOfferUtils dummyOfferUtils = new DummyOfferUtils(20);
        return StringToInputStreamConvertor.convert(dummyOfferUtils.getXmlText());
    }
}
