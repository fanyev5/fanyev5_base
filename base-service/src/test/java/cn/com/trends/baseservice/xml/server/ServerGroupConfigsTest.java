package cn.com.trends.baseservice.xml.server;

import cn.com.fanyev5.baseservice.base.config.xml.server.ServerGroupConfigs;
import cn.com.fanyev5.basecommons.xml.JAXBUtil;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * ServerGroupConfigs Test
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-26
 */
public class ServerGroupConfigsTest {

    @Test
    public void testConfig() {
        ServerGroupConfigs serverGroupConfigs = JAXBUtil.unmarshal(ServerGroupConfigs.class, "servergroup-config.xml");
        TestCase.assertNotNull(serverGroupConfigs);
    }
}
