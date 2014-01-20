package ru.droogcompanii.application.data.hierarchy_of_partners;

import java.io.Serializable;

/**
 * Created by Leonid on 02.12.13.
 */
public class Partner implements Serializable {
    public final int id;
    public final String title;
    public final String fullTitle;
    public final String discountType;
    public final int discount;

    // При проверке эквивалентности это поле не учитывается,
    // т.к. один партнер может входить в несколько категорий.
    public final int categoryId;


    public Partner(int id,
                   String title,
                   String fullTitle,
                   String discountType,
                   int discount,
                   int categoryId) {
        this.id = id;
        this.title = title;
        this.fullTitle = fullTitle;
        this.discountType = discountType;
        this.discount = discount;
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
               (discount == other.discount) &&
               (title.equals(other.title)) &&
               (fullTitle.equals(other.fullTitle)) &&
               (discountType.equals(other.discountType));
    }

    @Override
    public int hashCode() {
        return id + discount + title.hashCode() + fullTitle.hashCode() + discountType.hashCode();
    }


}
