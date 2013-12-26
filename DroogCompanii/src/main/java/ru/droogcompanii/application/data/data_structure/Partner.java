package ru.droogcompanii.application.data.data_structure;

import java.io.Serializable;

/**
 * Created by Leonid on 02.12.13.
 */
public class Partner implements Serializable {
    public final int id;
    public final String title;
    public final String fullTitle;
    public final String saleType;
    public final int categoryId;

    public Partner(int id,
                   String title,
                   String fullTitle,
                   String saleType,
                   int categoryId) {
        this.id = id;
        this.title = title;
        this.fullTitle = fullTitle;
        this.saleType = saleType;
        this.categoryId = categoryId;
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
        return (id == other.id) &&
               (title.equals(other.title)) &&
               (fullTitle.equals(other.fullTitle)) &&
               (saleType.equals(other.saleType)) &&
               (categoryId == other.categoryId);
    }

    @Override
    public int hashCode() {
        return id + title.hashCode() + fullTitle.hashCode() + saleType.hashCode() + categoryId;
    }


}
