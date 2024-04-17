package cn.abridge.springframework.util;

import java.util.LinkedHashMap;

/**
 * @Author: lyd
 * @Date: 2024/4/13 13:25
 * @Description:
 */
public abstract class CollectionUtils {
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * 实例化一个新的 {@link LinkedHashMap} ，其初始容量能够在不立即进行调整/重新哈希操作的情况下，容纳指定数量的元素。
     * <p>这与普通的 {@link LinkedHashMap} 构造函数不同，它的初始容量是相对于负载因子的。
     * @param expectedSize 期望的元素数量(具有相应的容量，因此不需要进行resizerehash操作)
     * @return LinkedHashMap
     */
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(int expectedSize) {
        return new LinkedHashMap<>(computeMapInitialCapacity(expectedSize), DEFAULT_LOAD_FACTOR);
    }

    private static int computeMapInitialCapacity(int expectedSize) {
        return (int) Math.ceil(expectedSize / (double) DEFAULT_LOAD_FACTOR);
    }
}
