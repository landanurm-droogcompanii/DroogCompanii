package ru.droogcompanii.application.ui.activity.search;

import android.content.Context;

import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;

/**
 * Created by ls on 14.01.14.
 */
public interface SearchResultProvider {
    String getTitle(Context context);
    List<Partner> getPartners(Context context);
    List<PartnerPoint> getPointsOfPartner(Context context, Partner partner);
    List<PartnerPoint> getAllPartnerPoints(Context context);
}
