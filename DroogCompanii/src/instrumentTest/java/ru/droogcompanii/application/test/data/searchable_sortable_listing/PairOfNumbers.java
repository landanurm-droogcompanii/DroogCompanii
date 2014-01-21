package ru.droogcompanii.application.test.data.searchable_sortable_listing;

import java.io.Serializable;

/**
 * Created by ls on 21.01.14.
 */
class PairOfNumbers implements Serializable {
    public final int first;
    public final int second;

    public PairOfNumbers(int first, int second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PairOfNumbers)) {
            return false;
        }
        PairOfNumbers other = (PairOfNumbers) obj;
        return (first == other.first) && (second == other.second);
    }

    @Override
    public int hashCode() {
        return first + second + first * second;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}