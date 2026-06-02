package io.theurl.framework.utility;

import java.util.Map;
import java.util.function.Consumer;

public class MapUtility {
    public static <K, V> void tryGet(Map<K, V> criteria, K key, Consumer<? super V> function) {
        if (criteria.containsKey(key)) {
            function.accept(criteria.get(key));
        }
    }
}
