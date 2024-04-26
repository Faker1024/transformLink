package com.unnamed.transformLink.admin.toolkit;

import java.util.Map;

public class RedisUtil {
    /**
     * 移除Map中最小的value，直至map.size() < 3
     */
    public static void removeMinUntilSizeThree(Map<Object, Object> map) {
        while (map.size() >= 3) {
            // 初始化最小值为 Long 的最大值
            long min = Long.MAX_VALUE;
            String minKey = null;

            // 寻找最小值
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                long value = Long.parseLong(entry.getValue().toString());
                if (value < min) {
                    min = value;
                    minKey = (String) entry.getKey();
                }
            }

            // 删除最小值的键值对
            if (minKey != null) {
                map.remove(minKey);
            }
        }
    }
}
