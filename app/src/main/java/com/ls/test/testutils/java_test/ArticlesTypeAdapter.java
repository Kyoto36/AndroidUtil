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

public class ArticlesTypeAdapter extends TypeAdapter<Articles> {

    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (type.getRawType() == Articles.class) {
                return (TypeAdapter<T>) new ArticlesTypeAdapter(gson);
            }
            return null;
        }
    };

    private Gson mGson;

    ArticlesTypeAdapter(Gson gson) {
        mGson = gson;
    }

    @Override
    public void write(JsonWriter out, Articles value) throws IOException {
        if(value == null){
            out.nullValue();
        }
        else{
            out.jsonValue(mGson.toJson(value.getArticles()));
        }
    }

    @Override
    public Articles read(JsonReader in) throws IOException {
        Articles value = null;
        if(in.peek() == JsonToken.BEGIN_ARRAY){
            List<Article> articles = mGson.fromJson(in,new TypeToken<List<Articles>>(){}.getType());
            value = new Articles(articles);
        }
        return value;
    }
}