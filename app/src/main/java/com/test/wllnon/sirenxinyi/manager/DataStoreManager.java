package com.test.wllnon.sirenxinyi.manager;

/**
 * Created by Administrator on 2016/3/31.
 */
public class DataStoreManager {
    private static DataStoreManager ourInstance = new DataStoreManager();

    public static DataStoreManager getInstance() {
        return ourInstance;
    }

    private DataStoreManager() {
    }
}
