package ir;

/**
 * Created by hwaxxer on 2017-03-23.
 */
import java.util.Map;
import java.util.LinkedHashMap;

public final class SimpleCache<K,V> extends LinkedHashMap<K,V> {
    private int maxSize;

    public SimpleCache(final int maxSize) {
        super(1, 0.75f, true);
        this.maxSize = maxSize;
    }

    protected boolean removeEldestEntry(final Map.Entry eldest) {
        return size() > maxSize;
    }
}
