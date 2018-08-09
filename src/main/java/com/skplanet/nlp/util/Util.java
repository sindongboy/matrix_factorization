package com.skplanet.nlp.util;

import com.skplanet.nlp.Prop;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

import java.util.*;

/**
 * Some Utilities
 * @author Donghun Shin / donghun.shin@sk.com
 * @date 12/24/15
 */
public final class Util {
    private static RandomGenerator random = new MersenneTwister();


    /**
     * Sort the given {@link Map} by its value
     *
     * @param map map instance to be sorted
     * @param <K> key type
     * @param <V> value type
     * @return map object sorted by value
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, final int order) {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                if (order == Prop.SORT_DESCENDING) {
                    /** for descending order */
                    return (o1.getValue()).compareTo(o2.getValue()) * -1;
                } else {
                    /** for ascending order */
                    return (o1.getValue()).compareTo(o2.getValue());
                }
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * Random number generate from Gaussian Distribution
     * @return random number
     */
    public static double nextGaussian() {
        return random.nextGaussian();
    }

    /**
     * Private Constructor
     */
    private Util() {

    }

}
