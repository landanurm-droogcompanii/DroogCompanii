package ru.droogcompanii.application.util;

/**
 * Created by ls on 13.03.14.
 */
public class MutablePair<F,S> {
    public F first;
    public S second;

    public static <F,S> MutablePair<F,S> create(F first, S second) {
        return new MutablePair<F, S>(first, second);
    }

    public MutablePair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        MutablePair<F,S> other = (MutablePair<F,S>) obj;
        return Objects.equals(first, other.first) && Objects.equals(second, other.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return getClass().getName() + "(" + String.valueOf(first) + ", " + String.valueOf(second) + ")";
    }
}
