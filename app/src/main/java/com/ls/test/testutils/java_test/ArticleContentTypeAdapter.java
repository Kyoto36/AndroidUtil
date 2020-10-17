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

public class ArticleContentTypeAdapter extends TypeAdapter<ArticleContent> {

    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (type.getRawType() == ArticleContent.class) {
                return (TypeAdapter<T>) new ArticleContentTypeAdapter(gson);
            }
            return null;
        }
    };

    private Gson mGson;

    ArticleContentTypeAdapter(Gson gson) {
        mGson = gson;
    }

    @Override
    public void write(JsonWriter out, ArticleContent value) throws IOException {
        if(value == null){
            out.nullValue();
        }
        else{
            out.jsonValue(mGson.toJson(value.getContentItems()));
        }
    }

    @Override
    public ArticleContent read(JsonReader in) throws IOException {
        ArticleContent value = null;
        List<ArticleContentItem> articles = null;
        if(in.peek() == JsonToken.STRING){
            String json = in.nextString();
            articles = mGson.fromJson(json,new TypeToken<List<ArticleContentItem>>(){}.getType());
        }
        else if(in.peek() == JsonToken.BEGIN_ARRAY){
            articles = mGson.fromJson(in,new TypeToken<List<ArticleContentItem>>(){}.getType());

        }
        if(articles != null){
            value = new ArticleContent(articles);
        }
        return value;
    }
}