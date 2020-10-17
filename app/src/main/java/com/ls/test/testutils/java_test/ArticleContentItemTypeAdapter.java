package com.ls.test.testutils.java_test;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * @ClassName: ArticleContentItemTypeAdapter
 * @Description:
 * @Author: ls
 * @Date: 2020/9/22 10:09
 */
public class ArticleContentItemTypeAdapter extends TypeAdapter<ArticleContentItem> {
    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (type.getRawType() == ArticleContentItem.class) {
                return (TypeAdapter<T>) new ArticleContentItemTypeAdapter(gson);
            }
            return null;
        }
    };

    private Gson mGson;

    ArticleContentItemTypeAdapter(Gson gson) {
        mGson = gson;
    }

    @Override
    public void write(JsonWriter out, ArticleContentItem value) throws IOException {
        if(value == null){
            out.nullValue();
        }
        else{
            out.beginObject();
            switch (value.getType()){
                case TEXT:
                    out.name("text");
                    break;
                case IMAGE:
                    out.name("image");
                    break;
                case LINK:
                    out.name("link");
                    break;
                case NULL:
                default:
                    out.name("unknown");
                    break;

            }
            out.jsonValue(mGson.toJson(value.getContents()));
            out.endObject();
        }

    }

    @Override
    public ArticleContentItem read(JsonReader in) throws IOException {
        ArticleContentItem value = null;
        if(in.peek() == JsonToken.BEGIN_OBJECT){
            in.beginObject();
            ArticleContentItem.Type type = ArticleContentItem.Type.NULL;
            if(in.hasNext()){
                switch (in.nextName()){
                    case "image":
                        type = ArticleContentItem.Type.IMAGE;
                        break;
                    case "text":
                        type = ArticleContentItem.Type.TEXT;
                        break;
                    case "link":
                        type = ArticleContentItem.Type.LINK;
                        break;
                }
                value = new ArticleContentItem(type,mGson.fromJson(in,String[].class));
            }
            in.endObject();
        }
        return value;
    }
}
