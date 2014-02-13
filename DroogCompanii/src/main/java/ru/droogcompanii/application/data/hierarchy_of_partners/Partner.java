package ru.droogcompanii.application.data.hierarchy_of_partners;

import java.util.List;

/**
 * Created by ls on 13.02.14.
 */
public interface Partner {
    int getId();
    int getCategoryId();
    int getDiscountSize();
    String getDiscountType();
    String getTitle();
    String getFullTitle();
    String getImageUrl();
    String getDescription();
    List<String> getWebSites();
    List<String> getEmails();
}
