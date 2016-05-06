package com.test.wllnon.sirenxinyi.utils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.test.wllnon.sirenxinyi.constant.Constant;
import com.test.wllnon.sirenxinyi.viewdata.BaseCardData;
import com.test.wllnon.sirenxinyi.viewdata.ChatCardData;
import com.test.wllnon.sirenxinyi.viewdata.ClassifyCardData;
import com.test.wllnon.sirenxinyi.viewdata.CommentCardData;
import com.test.wllnon.sirenxinyi.viewdata.FriendCardData;
import com.test.wllnon.sirenxinyi.viewdata.MessageCardData;
import com.test.wllnon.sirenxinyi.viewdata.AnswerCardData;
import com.test.wllnon.sirenxinyi.viewdata.QuestionCardData;
import com.test.wllnon.sirenxinyi.viewholder.BaseCardViewHolder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/4/20.
 */
public class CardDataListTypeUtils {
    private static Map<Integer, Class> typeMap = new TreeMap<>();
    private Type type = new TypeToken<List<BaseCardData>>(){}.getType();

    static {
        typeMap.put(Constant.ANSWER, AnswerCardData.class);
        typeMap.put(Constant.FRIEND, FriendCardData.class);
        typeMap.put(Constant.COMMENT, CommentCardData.class);
        typeMap.put(Constant.QUESTION, QuestionCardData.class);
        typeMap.put(Constant.MESSAGE, MessageCardData.class);
        typeMap.put(Constant.CHAT, ChatCardData.class);
    }

    private static class CardDataListTypeUtilsHolder {
        private static final CardDataListTypeUtils instance = new CardDataListTypeUtils();
    }

    private CardDataListTypeUtils() {
    }

    public static CardDataListTypeUtils newInstance() {
        return CardDataListTypeUtilsHolder.instance;
    }

    public void register(GsonBuilder gsonBuilder) {
        gsonBuilder.registerTypeAdapter(type, new Serializer());
        gsonBuilder.registerTypeAdapter(type, new Deserializer());
    }

    public Type getType() {
        return type;
    }

    public class Serializer implements JsonSerializer<List<BaseCardData>> {
        @Override
        public JsonElement serialize(List<BaseCardData> src, Type typeOfSrc, JsonSerializationContext context) {
            if (src == null)
                return null;
            else {
                JsonArray ja = new JsonArray();
                for (BaseCardData data : src) {
                    Class clazz = typeMap.get(data.getCardKind());
                    if (clazz == null) {
                        throw new RuntimeException("Unknown data number: " + data.getCardKind());
                    }
                    ja.add(context.serialize(data, clazz));

                }
                return ja;
            }
        }
    }

    public class Deserializer implements JsonDeserializer<List<BaseCardData>> {
        @Override
        public List<BaseCardData> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            List<BaseCardData> list = new ArrayList<>();
            JsonArray ja = json.getAsJsonArray();

            for (JsonElement je : ja) {
                int cardKind = je.getAsJsonObject().get("cardKind").getAsInt();
                Class clazz = typeMap.get(cardKind);
                if (clazz == null) {
                    throw new RuntimeException("Unknown data number: " + type);
                }
                list.add((BaseCardData) context.deserialize(je, clazz));
            }
            return list;
        }
    }
}
