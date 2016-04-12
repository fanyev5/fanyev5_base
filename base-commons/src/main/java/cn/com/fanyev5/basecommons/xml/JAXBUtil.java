package cn.com.fanyev5.basecommons.xml;

import com.google.common.io.Closeables;
import com.google.common.io.Resources;

import javax.xml.bind.JAXBContext;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;

/**
 * XML解析的工具类
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-19
 */
public final class JAXBUtil {

    private JAXBUtil() {
    }

    /**
     * 从指定的xml文件中解析出<code>T</code>类型的对象
     *
     * @param clazz
     * @param xmlInClassPath
     * @param <T>
     * @return
     * @throws RuntimeException
     */
    public static <T> T unmarshal(Class<T> clazz, String xmlInClassPath) {
        Reader reader = null;
        try {
            URL xmlUrl = Resources.getResource(xmlInClassPath);
            reader = new InputStreamReader(xmlUrl.openStream(), "UTF-8");
            JAXBContext context = JAXBContext.newInstance(clazz);
            return (T) context.createUnmarshaller().unmarshal(reader);
        } catch (Exception e) {
            throw new RuntimeException("Failed to unmarshal object for class:" + clazz + " xml:" + xmlInClassPath, e);
        } finally {
            Closeables.closeQuietly(reader);
        }
    }

    /**
     * 从指定的xml字符串中解析出<code>T</code>类型的对象
     *
     * @param clazz
     * @param xmlContent
     * @param <T>
     * @return
     * @throws RuntimeException
     */
    public static <T> T unmarshalByContent(Class<T> clazz, String xmlContent) {
        Reader reader = null;
        try {
            reader = new StringReader(xmlContent);
            JAXBContext context = JAXBContext.newInstance(clazz);
            return (T) context.createUnmarshaller().unmarshal(reader);
        } catch (Exception e) {
            throw new RuntimeException("Failed to unmarshal object for class:" + clazz + " xmlContent:" + xmlContent, e);
        } finally {
            Closeables.closeQuietly(reader);
        }
    }

}
