package ru.droogcompanii.application.data.hierarchy_of_partners;

import java.io.Serializable;

import ru.droogcompanii.application.util.Objects;

/**
 * Created by Leonid on 02.12.13.
 */
public class PartnerCategoryImpl implements PartnerCategory, Serializable {
    public int id;
    public String title;

    public PartnerCategoryImpl() {
        id = 0;
        title = "";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PartnerCategory)) {
            return false;
        }
        PartnerCategory other = (PartnerCategory) obj;
        return Objects.equals(getId(), other.getId()) && Objects.equals(getTitle(), other.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper(this)
                .add("id", id)
                .add("title", title)
                .toString();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
