package ru.droogcompanii.application.data.hierarchy_of_partners;

import java.io.Serializable;

/**
 * Created by Leonid on 02.12.13.
 */
public class PartnerCategory implements Serializable {
    public final int id;
    public final String title;

    public PartnerCategory(int id, String title) {
        this.id = id;
        this.title = title;
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
        return (id == other.id) && title.equals(other.title);
    }

    @Override
    public int hashCode() {
        return id + title.hashCode();
    }
}
