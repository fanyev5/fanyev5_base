package cn.com.fanyev5.basecommons.codec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import cn.com.fanyev5.basecommons.constant.Constants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 通用编码工具
 *
 * @author fanqi427@gmail.com
 * @since 2013-6-18
 */
public final class CodecUtil {

    private CodecUtil() {
    }

    private static final String EMPTY = "";
    private static final char CHAR_BLANK = ' ';
    private static final char CHAR_PLUS = '+';

    /**
     * 编码 Base64
     *
     * @param str 普通字符串
     * @return
     */
    public static String encodeBase64(String str) {
        if (StringUtils.isNotEmpty(str)) {
            return Base64.encodeBase64String(str.getBytes(Constants.DEFAULT_CHARSET));
        }
        return EMPTY;
    }

    /**
     * 解码 Base64
     *
     * @param str Base64字符串
     * @return
     */
    public static String decodeBase64(String str) {
        if (StringUtils.isNotEmpty(str)) {
            byte[] bytes = Base64.decodeBase64(str);
            return new String(bytes, Constants.DEFAULT_CHARSET);
        }
        return EMPTY;
    }

    /**
     * 解码 Base64, 修正URI传输过程中问题
     *
     * @param str Base64字符串
     * @return
     */
    public static String decodeBase64FixUri(String str) {
        if (StringUtils.isNotEmpty(str)) {
            // 修正base64 "+"被转为" "问题
            byte[] bytes = Base64.decodeBase64(fixBase64(str));
            return new String(bytes, Constants.DEFAULT_CHARSET);
        }
        return EMPTY;
    }

    /**
     * 编码 MD5 (32位)
     *
     * @param str 普通字符串
     * @return
     */
    public static String encodeMD5Hex(String str) {
        if (StringUtils.isNotEmpty(str)) {
            return DigestUtils.md5Hex(str);
        }
        return EMPTY;
    }

    /**
     * 编码 URI
     *
     * @param uri
     * @return
     */
    public static String encodeURI(String uri) {
        if (StringUtils.isNotEmpty(uri)) {
            try {
                return URLEncoder.encode(uri, Constants.DEFAULT_CHARSET_NAME);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return EMPTY;
    }

    /**
     * 修正Base64加号问题
     *
     * @param str base64后可能出现" "串
     * @return 返回将" "转为"+"的串
     */
    private static String fixBase64(String str) {
        return StringUtils.replaceChars(str, CHAR_BLANK, CHAR_PLUS);
    }

}