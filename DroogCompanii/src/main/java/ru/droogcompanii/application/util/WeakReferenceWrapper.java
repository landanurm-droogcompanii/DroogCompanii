package ru.droogcompanii.application.util;

import java.lang.ref.WeakReference;

/**
 * Created by ls on 20.02.14.
 */
public class WeakReferenceWrapper<T> {

    public static interface Handler<T> {
        void handle(T ref);
    }

    private final WeakReference<T> weakReference;


    public static <E> WeakReferenceWrapper<E> from(E elem) {
        return new WeakReferenceWrapper<E>(elem);
    }

    private WeakReferenceWrapper(T obj) {
        weakReference = new WeakReference<T>(obj);
    }

    public void handleIfExist(Handler<T> handler) {
        if (weakReference != null) {
            T ref = weakReference.get();
            if (ref != null) {
                handler.handle(ref);
            }
        }
    }

}
