package ru.droogcompanii.application.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by ls on 22.01.14.
 */
public class MultiMap<K,V> {

    public static class Entry<K, V> {
        private final K key;
        private final Set<V> values;

        private Entry(K key, Set<V> values) {
            this.key = key;
            this.values = values;
        }

        public K getKey() {
            return key;
        }

        public Set<V> getValues() {
            return values;
        }
    }

    public static interface OnEachHandler<K, V> {
        void onEach(Entry<K, V> entry);
    }

    protected final Map<K, Set<V>> map;

    public MultiMap() {
        map = new HashMap<K, Set<V>>();
    }

    public void putAll(K key, Collection<V> values) {
        Set<V> existingValues = get(key);
        existingValues.addAll(values);
        map.put(key, existingValues);
    }

    public void put(K key, V value) {
        Set<V> values = get(key);
        values.add(value);
        map.put(key, values);
    }

    public Set<V> get(K key) {
        Set<V> values = map.get(key);
        return (values == null) ? noValues() : values;
    }

    private Set<V> noValues() {
        return new HashSet<V>();
    }

    public boolean isContainKey(K key) {
        return !get(key).isEmpty();
    }

    public Set<K> keySet() {
        return map.keySet();
    }

    public void forEach(OnEachHandler<K, V> onEachHandler) {
        for (Map.Entry<K, Set<V>> each : map.entrySet()) {
            onEachHandler.onEach(new Entry<K, V>(each.getKey(), each.getValue()));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MultiMap<?,?>)) {
            return false;
        }
        MultiMap<?,?> other = (MultiMap<?,?>) obj;
        return map.equals(other);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }
}
