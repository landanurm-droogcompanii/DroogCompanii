package ru.droogcompanii.application.data.hierarchy_of_partners;

import java.io.Serializable;

import ru.droogcompanii.application.util.ConvertorToString;
import ru.droogcompanii.application.util.Objects;

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
        return Objects.equals(id, other.id) && Objects.equals(title, other.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public String toString() {
        return ConvertorToString.buildFor(this)
                .withFieldNames("id", "title")
                .withFieldValues(id, title)
                .toString();
    }
}
