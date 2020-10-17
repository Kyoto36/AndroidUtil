package com.ls.test.testutils.java_test;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: Test
 * @Description:
 * @Author: ls
 * @Date: 2020/9/22 10:15
 */
public class GsonTest {

    private static Gson mGson =  new GsonBuilder()
            .setLenient()
            .registerTypeAdapterFactory(ArticleContentItemTypeAdapter.FACTORY)
            .registerTypeAdapterFactory(ArticlesTypeAdapter.FACTORY)
            .registerTypeAdapterFactory(ArticleContentTypeAdapter.FACTORY)
            .registerTypeAdapterFactory(TopicsTypeAdapter.FACTORY)
            .create();
    private static String mJson = "{\n" +
            "    \"code\": 200,\n" +
            "    \"message\": \"success\",\n" +
            "    \"data\": {\n" +
            "        \"aid\": 9,\n" +
            "        \"title\": \"在线DOS游戏\",\n" +
            "        \"content\": \"[{\\\"text\\\":[\\\"点进&ldquo;在线DOS游戏&rdquo;的网站，就能在简陋的框架下看见很多熟悉的封面&mdash;&mdash;从《仙剑奇侠传》到《主题医院》，这些古老的DOS游戏就是带给我们最初的欢乐的那一批游戏。\\\",\\\":at10001\\\"]},{\\\"image\\\":[\\\":image8784\\\"]},{\\\"text\\\":[\\\"随手点进《仙剑奇侠传》的页面，洁白的页纸上先是显示出DOSBox的代码，系统随即进入了游戏封面：一行丹顶鹤从青山前飞过，仙剑奇侠传五个大字依次出现在屏幕右侧，右下角还有狂徒创作群的&ldquo;狂徒&rdquo;二字。不知他们在93年创立之时，是否知道那时的一款作品会在将来成为中国国产游戏翘楚之一呢？\\\"]},{\\\"image\\\":[\\\":image4354\\\"]}]\",\n" +
            "        \"at_list\": \"{\\\":at5263\\\":{\\\"uid\\\":\\\"10001\\\",\\\"nickname\\\":\\\"妮妮\\\"}}\",\n" +
            "        \"image_list\": \"{\\\":image8784\\\":{\\\"width\\\":1080,\\\"height\\\":661,\\\"image\\\":\\\"http:\\\\/\\\\/file.qhooplay.com\\\\/ba3340e576060c5c\\\\/9aefafad5c311f71.jpg\\\"},\\\":image4354\\\":{\\\"width\\\":743,\\\"height\\\":554,\\\"image\\\":\\\"http:\\\\/\\\\/file.qhooplay.com\\\\/bb17ee327c4779cc\\\\/1afa845f649387e0.jpg\\\"}}\",\n" +
            "        \"praisenums\": 0,\n" +
            "        \"messagenums\": 0,\n" +
            "        \"views\": 146,\n" +
            "        \"create_time\": \"1577685605\",\n" +
            "        \"is_praise\": 2,\n" +
            "        \"member\": {\n" +
            "            \"nickname\": \"与书管理员\",\n" +
            "            \"headurl\": \"http://file.qhooplay.com/a35947853e6026b2/4c51fa364f0cd627.png\",\n" +
            "            \"signature\": \"\",\n" +
            "            \"uid\": 10004,\n" +
            "            \"is_follow\": 2\n" +
            "        },\n" +
            "        \"tag_list\": [\n" +
            "            {\n" +
            "                \"tid\": 1,\n" +
            "                \"title\": \"传承工艺\",\n" +
            "                \"des\": \"传承工艺传承工艺传承工艺传承工艺传承工艺传承工艺传承工艺传承工艺传承工艺\\r\\n传承工艺传承工艺传承工艺\\r\\n传承工艺传承工艺\\r\\n\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"tid\": 3,\n" +
            "                \"title\": \"风行雅事\",\n" +
            "                \"des\": \"\"\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    \"request_id\": \"4CSLzGBQ5ahcJOBRN0Bsr1601195164307\"\n" +
            "}";
    private static String ajson = "{\"code\":200,\"message\":\"success\",\"data\":{\"list\":[{\"id\":97,\"title\":\"\\u6052\\u661f\",\"des\":null,\"praisenums\":0,\"messagenums\":28,\"views\":138,\"create_time\":\"1602229290\",\"is_full\":2,\"at_list\":null,\"image_list\":[],\"member\":{\"nickname\":\"\\u4e0e\\u4e66\\u7ba1\\u7406\\u5458\",\"headurl\":\"http:\\/\\/file.qhooplay.com\\/a35947853e6026b2\\/4c51fa364f0cd627.png\",\"uid\":10004},\"tag_list\":[{\"tid\":1,\"title\":\"\\u4f20\\u627f\\u5de5\\u827a\"},{\"tid\":2,\"title\":\"\\u8863\\u51a0\\u670d\\u9970\"},{\"tid\":3,\"title\":\"\\u98ce\\u884c\\u96c5\\u4e8b\"},{\"tid\":4,\"title\":\"\\u6587\\u53f2\\u7ecf\\u5178\"}],\"is_praise\":2,\"type\":1},{\"id\":44,\"title\":\"\\u516d\\u5b57\\u5927\\u660e\\u5492\",\"des\":null,\"praisenums\":0,\"messagenums\":0,\"views\":7,\"create_time\":\"1601436014\",\"is_full\":2,\"at_list\":null,\"image_list\":[],\"member\":{\"nickname\":\"\\u4e0e\\u4e66\\u7ba1\\u7406\\u5458\",\"headurl\":\"http:\\/\\/file.qhooplay.com\\/a35947853e6026b2\\/4c51fa364f0cd627.png\",\"uid\":10004},\"tag_list\":[{\"tid\":1,\"title\":\"\\u4f20\\u627f\\u5de5\\u827a\"},{\"tid\":2,\"title\":\"\\u8863\\u51a0\\u670d\\u9970\"},{\"tid\":3,\"title\":\"\\u98ce\\u884c\\u96c5\\u4e8b\"},{\"tid\":4,\"title\":\"\\u6587\\u53f2\\u7ecf\\u5178\"}],\"is_praise\":2,\"type\":1},{\"id\":43,\"title\":\"PHP \\u64cd\\u4f5c Redis \\u7684\\u57fa\\u672c\\u65b9\\u6cd5\",\"des\":null,\"praisenums\":0,\"messagenums\":0,\"views\":2,\"create_time\":\"1601435666\",\"is_full\":2,\"at_list\":null,\"image_list\":[],\"member\":{\"nickname\":\"\\u4e0e\\u4e66\\u7ba1\\u7406\\u5458\",\"headurl\":\"http:\\/\\/file.qhooplay.com\\/a35947853e6026b2\\/4c51fa364f0cd627.png\",\"uid\":10004},\"tag_list\":[{\"tid\":1,\"title\":\"\\u4f20\\u627f\\u5de5\\u827a\"},{\"tid\":2,\"title\":\"\\u8863\\u51a0\\u670d\\u9970\"},{\"tid\":3,\"title\":\"\\u98ce\\u884c\\u96c5\\u4e8b\"},{\"tid\":4,\"title\":\"\\u6587\\u53f2\\u7ecf\\u5178\"}],\"is_praise\":2,\"type\":1},{\"id\":36,\"title\":\"\\u6625\\u98ce\\u4e0d\\u5ea6\",\"des\":[\"\\u9ec4\\u6cb3\\u8fdc\\u4e0a\\u767d\\u4e91\\u95f4\\uff0c\",\"\\u4e00\\u7247\\u5b64\\u57ce\\u4e07\\u4ede\\u5c71\\u3002\",\"\\u7f8c\\u7b1b\\u4f55\\u987b\\u6028\\u6768\\u67f3\\uff0c\",\"\\u6625\\u98ce\\u4e0d\\u5ea6\\u7389\\u95e8\\u5173\\u3002\",\"\\u201c\\u6625\\u98ce\\u4e0d\\u5ea6\\u7389\\u95e8\\u5173\\u201d\\u662f\\u4e00\\u53e5\\u8bd7\\uff0c\\u51fa\\u81ea\\u5510\\u671d\\u738b\\u4e4b\\u6da3\\u7684\\u300a\\u51c9\\u5dde\\u8bcd\\u300b\\uff0c\\u63cf\\u5199\\u4e86\\u8fb9\\u585e\\u51c9\\u5dde\\u96c4\\u4f1f\\u58ee\\u9614\\u53c8\\u8352\\u51c9\\u5bc2\\u5bde\\u7684\\u666f\\u8c61\\u3002\\u738b\\u4e4b\\u6da3\\u8fd9\\u9996\\u8bd7\\u5199\\u620d\\u8fb9\\u58eb\\u5175\\u7684\\u6000\\u4e61\\u60c5\\u3002\\u867d\\u6781\\u529b\\u6e32\\u67d3\\u620d\\u5352\\u4e0d\\u5f97\\u8fd8\\u4e61\\u7684\\u6028\\u60c5\\uff0c\\u4f46\\u4e1d\\u6beb\\u6ca1\\u6709\\u534a\\u70b9\\u9893\\u4e27\\u6d88\\u6c89\\u7684\\u60c5\\u8c03\\u3002\",\":at2601\",\"\\u8fdc\\u8fdc\\u5954\\u6d41\\u800c\\u6765\\u7684\\u9ec4\\u6cb3\\uff0c\",\":at3976\"],\"praisenums\":0,\"messagenums\":0,\"views\":5,\"create_time\":\"1601030436\",\"is_full\":2,\"at_list\":{\":at2601\":{\"uid\":\"10000\",\"nickname\":\"\\u6797\\u5fd7\\u6797\"},\":at3976\":{\"uid\":\"10001\",\"nickname\":\"\\u59ae\\u59ae\"}},\"image_list\":[{\"width\":268,\"height\":201,\"image\":\"http:\\/\\/file.qhooplay.com\\/5f34a1ad6ce1a3e6\\/1c2c11d64997fac6.jpg\"}],\"member\":{\"nickname\":\"\\u4e0e\\u4e66\\u7ba1\\u7406\\u5458\",\"headurl\":\"http:\\/\\/file.qhooplay.com\\/a35947853e6026b2\\/4c51fa364f0cd627.png\",\"uid\":10004},\"tag_list\":[{\"tid\":2,\"title\":\"\\u8863\\u51a0\\u670d\\u9970\"},{\"tid\":3,\"title\":\"\\u98ce\\u884c\\u96c5\\u4e8b\"},{\"tid\":4,\"title\":\"\\u6587\\u53f2\\u7ecf\\u5178\"}],\"is_praise\":2,\"type\":1},{\"id\":28,\"title\":\"\\u8bd5\\u8bd5\\u63d0\\u9192\\u7528\\u6237\\u4e3a\\u7a7a\",\"des\":null,\"praisenums\":0,\"messagenums\":0,\"views\":5,\"create_time\":\"1591346913\",\"is_full\":2,\"at_list\":{\":at9264\":{\"uid\":\"10114\",\"nickname\":\"\\u7528\\u62377308557601\"}},\"image_list\":[{\"width\":1080,\"height\":718,\"image\":\"http:\\/\\/file.qhooplay.com\\/8bf8bf573dfeee7a\\/5db9cca96e3cb58e.jpg\"},{\"width\":1920,\"height\":1080,\"image\":\"http:\\/\\/file.qhooplay.com\\/bb6c6fbdaeaef31d\\/bba37ca9d62a0b1d.jpg\"}],\"member\":{\"nickname\":\"\\u4e0e\\u4e66\\u7ba1\\u7406\\u5458\",\"headurl\":\"http:\\/\\/file.qhooplay.com\\/a35947853e6026b2\\/4c51fa364f0cd627.png\",\"uid\":10004},\"tag_list\":[{\"tid\":3,\"title\":\"\\u98ce\\u884c\\u96c5\\u4e8b\"}],\"is_praise\":2,\"type\":1},{\"id\":23,\"title\":\"\\u767e\\u5ea6\",\"des\":null,\"praisenums\":0,\"messagenums\":0,\"views\":47,\"create_time\":\"1577938971\",\"is_full\":2,\"at_list\":null,\"image_list\":[],\"member\":{\"nickname\":\"\\u738b\\u67cf\\u67971\",\"headurl\":\"http:\\/\\/file.qhooplay.com\\/32928a3013ad90c5\\/eae839dc008a8dea.jpg\",\"uid\":10006},\"tag_list\":[{\"tid\":3,\"title\":\"\\u98ce\\u884c\\u96c5\\u4e8b\"},{\"tid\":4,\"title\":\"\\u6587\\u53f2\\u7ecf\\u5178\"}],\"is_praise\":2,\"type\":1},{\"id\":9,\"title\":\"\\u5728\\u7ebfDOS\\u6e38\\u620f\",\"des\":[\"\\u70b9\\u8fdb\\u201c\\u5728\\u7ebfDOS\\u6e38\\u620f\\u201d\\u7684\\u7f51\\u7ad9\\uff0c\\u5c31\\u80fd\\u5728\\u7b80\\u964b\\u7684\\u6846\\u67b6\\u4e0b\\u770b\\u89c1\\u5f88\\u591a\\u719f\\u6089\\u7684\\u5c01\\u9762\\u2014\\u2014\\u4ece\\u300a\\u4ed9\\u5251\\u5947\\u4fa0\\u4f20\\u300b\\u5230\\u300a\\u4e3b\\u9898\\u533b\\u9662\\u300b\\uff0c\\u8fd9\\u4e9b\\u53e4\\u8001\\u7684DOS\\u6e38\\u620f\\u5c31\\u662f\\u5e26\\u7ed9\\u6211\\u4eec\\u6700\\u521d\\u7684\\u6b22\\u4e50\\u7684\\u90a3\\u4e00\\u6279\\u6e38\\u620f\\u3002\",\":at5176\"],\"praisenums\":2,\"messagenums\":21,\"views\":954,\"create_time\":\"1577685605\",\"is_full\":2,\"at_list\":{\":at9793\":10023},\"image_list\":[{\"width\":1080,\"height\":661,\"image\":\"http:\\/\\/file.qhooplay.com\\/ba3340e576060c5c\\/9aefafad5c311f71.jpg\"},{\"width\":743,\"height\":554,\"image\":\"http:\\/\\/file.qhooplay.com\\/bb17ee327c4779cc\\/1afa845f649387e0.jpg\"}],\"member\":{\"nickname\":\"\\u4e0e\\u4e66\\u7ba1\\u7406\\u5458\",\"headurl\":\"http:\\/\\/file.qhooplay.com\\/a35947853e6026b2\\/4c51fa364f0cd627.png\",\"uid\":10004},\"tag_list\":[{\"tid\":1,\"title\":\"\\u4f20\\u627f\\u5de5\\u827a\"},{\"tid\":2,\"title\":\"\\u8863\\u51a0\\u670d\\u9970\"},{\"tid\":3,\"title\":\"\\u98ce\\u884c\\u96c5\\u4e8b\"}],\"is_praise\":1,\"type\":1},{\"id\":1,\"title\":\"\\u5237\\uff01\\u5237\\uff01\\u5237\\uff01\",\"des\":null,\"praisenums\":2,\"messagenums\":1,\"views\":35,\"create_time\":\"1576489296\",\"is_full\":2,\"at_list\":null,\"image_list\":[],\"member\":{\"nickname\":\"\\u738b\\u67cf\\u67971\",\"headurl\":\"http:\\/\\/file.qhooplay.com\\/32928a3013ad90c5\\/eae839dc008a8dea.jpg\",\"uid\":10006},\"tag_list\":[{\"tid\":2,\"title\":\"\\u8863\\u51a0\\u670d\\u9970\"},{\"tid\":3,\"title\":\"\\u98ce\\u884c\\u96c5\\u4e8b\"},{\"tid\":7,\"title\":\"\\u795e\\u8bdd\\u5fd7\\u602a\"}],\"is_praise\":1,\"type\":1},{\"id\":2,\"title\":\"\\u53e4\\u98ce\\u82b1\\u5f0f\\u7f8e\\u7537\\u5b50\",\"des\":null,\"praisenums\":0,\"messagenums\":1,\"views\":66,\"create_time\":\"1576489296\",\"is_full\":2,\"at_list\":{\":at9793\":10023},\"image_list\":[{\"width\":1000,\"height\":708,\"image\":\"http:\\/\\/file.qhooplay.com\\/32928a3013ad90c5\\/eae839dc008a8dea.jpg\"}],\"member\":{\"nickname\":\"\\u6797\\u5fd7\\u6797\",\"headurl\":\"http:\\/\\/file.qhooplay.com\\/yushu\\/20191205\\/1\\/ad1713e872f99a908137203c41c6ea85.jpg\",\"uid\":10000},\"tag_list\":[{\"tid\":1,\"title\":\"\\u4f20\\u627f\\u5de5\\u827a\"},{\"tid\":3,\"title\":\"\\u98ce\\u884c\\u96c5\\u4e8b\"}],\"is_praise\":2,\"type\":1}],\"next_index\":9,\"total\":9},\"request_id\":\"oDaXnbKsK9fR62LE9VmxiT1ofe6oD1602593999987\"}";
    public static void main(String[] args) throws IOException {
        JsonReader reader = new JsonReader(new StringReader(mJson));
        reader.setLenient(true);
        ApiResult<ArticleDetail> array = mGson.fromJson(reader,new TypeToken<ApiResult<ArticleDetail>>(){}.getType());
        System.out.println(array.toString());
        String json = mGson.toJson(array);
        System.out.println(json);
//
//        System.out.println(((984F / 1024) * 1024));
//        BaseA a = new C("cc","gg");
//        String json = mGson.toJson(a);
//        System.out.println(json);
//        BaseA jsonA = mGson.fromJson(json,BaseA.class);
//        System.out.println(jsonA.getClass());
//        System.out.println(jsonA.toString());

//        JsonArray array = mGson.fromJson(mJson, JsonArray.class);
//        System.out.println(array.toString());
    }

    static abstract class BaseA{
        protected String aa;

        public String getAa() {
            return aa;
        }

        public void setAa(String aa) {
            this.aa = aa;
        }

        public abstract boolean isCompatible();
    }

    static class B extends BaseA{
        private String bb;
        private String ss;

        public B(String bb, String ss) {
            this.aa = "aa";
            this.bb = bb;
            this.ss = ss;
        }

        public String getBb() {
            return bb;
        }

        public void setBb(String bb) {
            this.bb = bb;
        }

        public String getSs() {
            return ss;
        }

        public void setSs(String ss) {
            this.ss = ss;
        }

        @Override
        public String toString() {
            return "B{" +
                    "aa='" + aa + '\'' +
                    ", bb='" + bb + '\'' +
                    ", ss='" + ss + '\'' +
                    '}';
        }

        @Override
        public boolean isCompatible() {
            return bb != null && ss != null;
        }
    }

    static class C extends BaseA{
        private String cc;
        private String gg;

        public C(String cc, String gg) {
            this.aa = "aa";
            this.cc = cc;
            this.gg = gg;
        }

        public String getCc() {
            return cc;
        }

        public void setCc(String cc) {
            this.cc = cc;
        }

        public String getGg() {
            return gg;
        }

        public void setGg(String gg) {
            this.gg = gg;
        }

        @Override
        public String toString() {
            return "C{" +
                    "aa='" + aa + '\'' +
                    ", cc='" + cc + '\'' +
                    ", gg='" + gg + '\'' +
                    '}';
        }

        @Override
        public boolean isCompatible() {
            return cc != null && gg != null;
        }
    }

    static class Json{
        enum Type{
            IMAGE,TEXT,NULL
        }

        private Type type;
        private String[] contents;

        public Json(Type type, String[] contents) {
            this.type = type;
            this.contents = contents;
        }

        public Type getType() {
            return type;
        }

        public String[] getContents() {
            return contents;
        }

        @Override
        public String toString() {
            return "Json{" +
                    "type=" + type +
                    ", contents=" + Arrays.toString(contents) +
                    '}';
        }
    }

    static class BaseAAdapter extends TypeAdapter<BaseA>{
        public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
            @SuppressWarnings("unchecked")
            @Override
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
                if (type.getRawType() == BaseA.class) {
                    return (TypeAdapter<T>) new BaseAAdapter(gson);
                }
                return null;
            }
        };

        private Gson mGson;

        BaseAAdapter(Gson gson) {
            mGson = gson;
        }

        @Override
        public void write(JsonWriter out, BaseA value) throws IOException {
            out.jsonValue(mGson.toJson(value));
        }

        @Override
        public BaseA read(JsonReader in) throws IOException {
            BaseA value = null;
            JsonObject json = mGson.fromJson(in,JsonObject.class);
            if(json.has("cc")) {
                value = mGson.fromJson(json, C.class);
            }
            else if(json.has("bb")){
                value = mGson.fromJson(json, B.class);
            }
//            if(value == null || !value.isCompatible()){
//                value = mGson.fromJson(in,C.class);
//            }
            return value;
        }
    }

    static class Adapter extends TypeAdapter<Json>{
        public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
            @SuppressWarnings("unchecked")
            @Override
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
                if (type.getRawType() == Json.class) {
                    return (TypeAdapter<T>) new Adapter(gson);
                }
                return null;
            }
        };

        private Gson mGson;

        Adapter(Gson gson) {
            mGson = gson;
        }
        @Override
        public void write(JsonWriter out, Json value) throws IOException {
            System.out.println("Adapter.write");
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
                    case NULL:
                        out.name("unknown");
                        break;
                }
                out.jsonValue(mGson.toJson(value.getContents()));
                out.endObject();
            }
        }

        @Override
        public Json read(JsonReader in) throws IOException {
            System.out.println("Adapter.read");
            Json value = null;
            if(in.peek() == JsonToken.BEGIN_OBJECT){
                in.beginObject();
                Json.Type type = Json.Type.NULL;
                if(in.hasNext()){
                    switch (in.nextName()){
                        case "image":
                            type = Json.Type.IMAGE;
                            break;
                        case "text":
                            type = Json.Type.TEXT;
                            break;
                    }
                    value = new Json(type,mGson.fromJson(in,String[].class));
                }
                in.endObject();
            }
            return value;
        }
    }


}
