package cn.com.fanyev5.basecommons.codec;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Hessian工具
 *
 * @author fanqi427@gmail.com
 * @since 2013-6-18
 */
public final class HessianUtil {

    private HessianUtil() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(HessianUtil.class);

    /**
     * 对象变成byte数组
     *
     * @param obj
     * @return
     */
    public static byte[] encode(Object obj) {
        ByteArrayOutputStream byteArray = null;
        Hessian2Output output = null;
        try {
            byteArray = new ByteArrayOutputStream();
            output = new Hessian2Output(byteArray);

            output.writeObject(obj);
            output.flush();
            return byteArray.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (byteArray != null) {
                try {
                    byteArray.close();
                } catch (IOException e) {
                    LOGGER.error("Close input error.", e);
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    LOGGER.error("Close output error.", e);
                }
            }
        }
    }

    /**
     * 将byte数组转回对象
     *
     * @param data
     * @return
     */

    public static Object decode(byte[] data) {
        Hessian2Input input = null;
        try {
            input = new Hessian2Input(new ByteArrayInputStream(data));
            return input.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    LOGGER.error("Close output error.", e);
                }
            }
        }
    }

}
