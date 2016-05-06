package com.test.wllnon.sirenxinyi.manager;

/**
 * Created by Administrator on 2016/3/31.
 */
public class DataProcessManager {
    private static DataProcessManager ourInstance = new DataProcessManager();

    public static DataProcessManager getInstance() {
        return ourInstance;
    }

    private DataProcessManager() {
    }


}
