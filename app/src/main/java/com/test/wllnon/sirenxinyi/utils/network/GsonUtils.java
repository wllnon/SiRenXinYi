package com.test.wllnon.sirenxinyi.utils.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.test.wllnon.sirenxinyi.utils.CardDataListTypeUtils;

/**
 * Created by Administrator on 2016/4/20.
 */
public class GsonUtils {
    private GsonBuilder gsonBuilder = new GsonBuilder();
    private Gson gson = null;

    private static class GsonUtilsHolder {
        private static GsonUtils instance = new GsonUtils();
    }

    private GsonUtils() {
        CardDataListTypeUtils.newInstance().register(gsonBuilder);
    }

    public static GsonUtils newInstance() {
        return GsonUtilsHolder.instance;
    }

    public Gson getGson() {
        if (gson == null) {
            gson = gsonBuilder.create();
        }
        return gson;
    }

}
