package cn.com.trends.basecommons.codec;

import org.junit.Assert;
import org.junit.Test;

import cn.com.fanyev5.basecommons.codec.CodecUtil;

/**
 * CodecUtil Test
 *
 * @author fanqi427@gmail.com
 * @since 2013-08-14
 */
public class CodecUtilTest {

    @Test
    public void test() {
        Assert.assertEquals(CodecUtil.decodeBase64("aVBhZDMsNA=="), "iPad3,4");
        Assert.assertEquals(CodecUtil.decodeBase64("QXBwbGU="), "Apple");
        Assert.assertEquals(CodecUtil.decodeBase64("aVBob25lIE9TNy4w"), "iPhone OS7.0");
    }
}
