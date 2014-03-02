package ru.droogcompanii.application.util;

/**
 * Created by ls on 18.02.14.
 */
public class Holder<T> {
    public T value;

    public static <T> Holder<T> from(T value) {
        return new Holder<T>(value);
    }

    public Holder(T value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Holder other = (Holder) obj;

        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
