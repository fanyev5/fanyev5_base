package cn.com.fanyev5.basecommons.codec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.net.URL;

/**
 * JSON工具
 *
 * @author fanqi427@gmail.com
 * @since 2013-6-18
 */
public final class JSONUtil {

    private JSONUtil() {
    }

    /**
     * Object 转换为 JSON字符串
     *
     * @param obj
     * @return
     */
    public static String obj2Json(Object obj) {
        try {
            return getInstance().writeValueAsString(obj);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * JSON字符串 转换为 Object
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T json2Obj(String json, Class<T> clazz) {
        try {
            return getInstance().readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * JSON字符串 转换为 Object
     *
     * @param json
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T json2Obj(URL json, TypeReference<T> typeReference) {
        try {
            return getInstance().readValue(json, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * JSON字符串 转换为 Object
     *
     * @param json
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T json2Obj(String json, TypeReference<T> typeReference) {
        try {
            return getInstance().readValue(json, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static ObjectMapper getInstance() {
        return SingletonHolder.instance;
    }

    private static final class SingletonHolder {
        private static final ObjectMapper instance = new ObjectMapper();

        static {
            // SerializationFeature for changing how JSON is written

            // to enable standard indentation ("pretty-printing"):
            instance.enable(SerializationFeature.INDENT_OUTPUT);
            // to allow serialization of "empty" POJOs (no properties to serialize)
            // (without this setting, an exception is thrown in those cases)
//            instance.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            // to write java.util.Date, Calendar as number (timestamp):
//            instance.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            // DeserializationFeature for changing how JSON is read as POJOs:

            // to prevent exception when encountering unknown property:
            instance.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            // to allow coercion of JSON empty String ("") to null Object value:
//            instance.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        }
    }
}
