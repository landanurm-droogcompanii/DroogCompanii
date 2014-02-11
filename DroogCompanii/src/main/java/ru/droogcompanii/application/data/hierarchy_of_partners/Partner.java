package ru.droogcompanii.application.data.hierarchy_of_partners;

import java.io.Serializable;

import ru.droogcompanii.application.util.ConvertorToString;
import ru.droogcompanii.application.util.Objects;

/**
 * Created by Leonid on 02.12.13.
 */
public class Partner implements Serializable {
    public final int id;
    public final int discount;
    public final String discountType;
    public final String fullTitle;
    public final String title;

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
        return (Objects.equals(id, other.id) &&
                Objects.equals(discount, other.discount) &&
                Objects.equals(title, other.title) &&
                Objects.equals(fullTitle, other.fullTitle) &&
                Objects.equals(discountType, other.discountType));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, discount, title, fullTitle, discountType);
    }


    @Override
    public String toString() {
        return ConvertorToString.buildFor(this)
                .withFieldNames("id", "title", "fullTitle", "discount", "discountType")
                .withFieldValues(id, title, fullTitle, discount, discountType)
                .toString();
    }
}
