package ru.droogcompanii.application.data.hierarchy_of_partners;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.util.ConverterToString;
import ru.droogcompanii.application.util.Objects;

/**
 * Created by Leonid on 02.12.13.
 */
public class PartnerImpl implements Partner, Serializable {
    public int id;
    public int discountSize;
    public String discountType;
    public String fullTitle;
    public String title;
    public String imageUrl;
    public String description;
    public List<String> webSites;
    public List<String> emails;

    // При проверке эквивалентности это поле не учитывается,
    // т.к. один партнер может входить в несколько категорий.
    public int categoryId;



    public PartnerImpl() {
        id = 0;
        categoryId = 0;
        title = "";
        fullTitle = "";
        description = "";
        imageUrl = "";
        discountType = "";
        discountSize = 0;
        webSites = new ArrayList<String>();
        emails = new ArrayList<String>();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Partner)) {
            return false;
        }
        Partner other = (Partner) obj;
        return (Objects.equals(id, other.getId()) &&
                Objects.equals(title, other.getTitle()) &&
                Objects.equals(fullTitle, other.getFullTitle()) &&
                Objects.equals(discountSize, other.getDiscountSize()) &&
                Objects.equals(discountType, other.getDiscountType()) &&
                Objects.equals(imageUrl, other.getImageUrl()) &&
                Objects.equals(description, other.getDescription()) &&
                Objects.equals(webSites, other.getWebSites()) &&
                Objects.equals(emails, other.getEmails()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id, discountSize, title, fullTitle, discountType, imageUrl, description, webSites, emails
        );
    }


    @Override
    public String toString() {
        return ConverterToString.buildFor(this)
            .withFieldNames("id", "title",
                    "fullTitle", "discountSize",
                    "discountType", "imageUrl",
                    "description", "webSites", "emails")
            .withFieldValues(id, title,
                    fullTitle, discountSize,
                    discountType, imageUrl,
                    description, webSites, emails)
            .toString();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getCategoryId() {
        return categoryId;
    }

    @Override
    public int getDiscountSize() {
        return discountSize;
    }

    @Override
    public String getDiscountType() {
        return discountType;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getFullTitle() {
        return fullTitle;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<String> getWebSites() {
        return webSites;
    }

    @Override
    public List<String> getEmails() {
        return emails;
    }
}
