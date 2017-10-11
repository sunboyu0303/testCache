package com.jd.test.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by sunboyu on 2017/10/9.
 */
public class Test {

    private static final String cacheName = "test";
    private static final int maxSize = 1000;
    private static final String key = "testKey";

    /**
     * EhCahce的核心类：
     * A、CacheManager：Cache的管理类；
     * B、Cache：具体的cache类信息，负责缓存的get和put等操作
     * C、CacheConfiguration ：cache的配置信息，包含策略、最大值等信息
     * D、Element：cache中单条缓存数据的单位
     *
     * @param args
     */
    public static void main(String[] args) {

        // EhCache的缓存，是通过CacheManager来进行管理的
        CacheManager cacheManager = CacheManager.getInstance();

        // 缓存的配置，也可以通过xml文件进行
        CacheConfiguration conf = new CacheConfiguration();

        // 设置名字
        conf.name(cacheName);
        // 最大的缓存数量
        conf.setMaxEntriesLocalHeap(maxSize);
        // 设置失效策略
        conf.memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LRU);

        // 创建一个缓存对象，并把设置的信息传入进去
        Cache cache = new Cache(conf);

        // 将缓存对象添加到管理器中
        cacheManager.addCache(cache);

        for (int i = 0; i < 1500; i++)
            cache.put(new Element(key + i, new Date()));

        System.out.println(cache.getSize());
        System.out.println(cache.getName());

        List<String> keyList = cache.getKeys();
        Map<Object, Element> elementMap = cache.getAll(keyList);

        for (Object key : elementMap.keySet()) {
            System.out.println(cache.get(key).toString());
        }
    }
}
