package com.ls.test.testutils.java_test;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.List;

public class TopicsTypeAdapter extends TypeAdapter<Topics> {

    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (type.getRawType() == Topics.class) {
                return (TypeAdapter<T>) new TopicsTypeAdapter(gson);
            }
            return null;
        }
    };

    private Gson mGson;

    TopicsTypeAdapter(Gson gson) {
        mGson = gson;
    }

    @Override
    public void write(JsonWriter out, Topics value) throws IOException {
        if(value == null){
            out.nullValue();
        }
        else{
            out.jsonValue(mGson.toJson(value.getTopicList()));
        }
    }

    @Override
    public Topics read(JsonReader in) throws IOException {
        Topics value = null;
        if(in.peek() == JsonToken.BEGIN_ARRAY){
            List<Topic> topicList = mGson.fromJson(in,new TypeToken<List<Topic>>(){}.getType());
            value = new Topics(topicList);
        }
        return value;
    }
}