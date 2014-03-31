package ru.droogcompanii.application.ui.screens.offer_list.offers_provider;

import android.content.Context;
import android.content.res.Resources;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.data.xml_parser.offers_xml_parser.OffersXmlParser;
import ru.droogcompanii.application.util.DownloadingFailedException;
import ru.droogcompanii.application.util.LogUtils;

/**
 * Created by ls on 10.02.14.
 */
public class DummyOffersDownloder implements OffersDownloader, Serializable {

    @Override
    public List<Offer> downloadOffers(Context context) {
        try {
            return tryDownloadOffers(context);
        } catch (Exception e) {
            LogUtils.debug(e.getMessage());
            throw new DownloadingFailedException(e.getMessage(), e);
        }
    }

    private List<Offer> tryDownloadOffers(Context context) throws Exception {
        OffersXmlParser parser = new OffersXmlParser();
        InputStream inputStream = prepareInputStream(context);
        return parser.parse(inputStream);
    }

    private static InputStream prepareInputStream(Context context) {
        Resources resources = context.getResources();
        return resources.openRawResource(R.raw.test_offers);
    }
}
