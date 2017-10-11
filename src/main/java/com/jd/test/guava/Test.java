package com.jd.test.guava;

import com.google.common.cache.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by sunboyu on 2017/10/10.
 */
public class Test {

    public static void main(String[] args) throws Exception {

        final List<Person> list = new ArrayList<Person>();
        list.add(new Person("1", "zhangsan"));
        list.add(new Person("2", "lisi"));
        list.add(new Person("3", "wangwu"));

        LoadingCache<String, Person> cache = CacheBuilder.newBuilder()
                // 设置大小，条目数
                .maximumSize(20)
                // 设置失效时间，创建时间
                .expireAfterWrite(20, TimeUnit.SECONDS)
                // 设置时效时间，最后一次被访问
                .expireAfterAccess(20, TimeUnit.HOURS)

                .removalListener(new RemovalListener() {
                                     public void onRemoval(RemovalNotification removalNotification) {
                                         System.out.println("有缓存数据被移除了");
                                     }
                                 }
                ).build(new CacheLoader<String, Person>() {
                    public Person load(String key) throws Exception {
                        System.out.println(key + " load in cache");
                        return getPerson(key);
                    }

                    private Person getPerson(String key) throws Exception {
                        System.out.println(key + " query");
                        for (Person p : list) {
                            if (p.getId().equals(key))
                                return p;
                        }
                        return null;
                    }
                });

        System.out.println(cache.get("1"));
        System.out.println(cache.get("2"));
        System.out.println(cache.get("3"));
        System.out.println("-=-=-=-=-=-=-=second-=-=-=-=-=-=-=-=-=-=");
        System.out.println(cache.get("1"));
        System.out.println(cache.get("2"));
        System.out.println(cache.get("3"));
    }
}

class Person {

    private String id;
    private String name;

    public Person(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
