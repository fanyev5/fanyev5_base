package cn.com.trends.baseservice.xml.es;

import cn.com.fanyev5.baseservice.base.config.xml.es.ESConfigs;
import cn.com.fanyev5.basecommons.xml.JAXBUtil;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * ESConfigs Test
 *
 * @author fanqi427@gmail.com
 * @since 2013-12-17
 */
public class ESConfigsTest {

    @Test
    public void test() {
        ESConfigs esConf = JAXBUtil.unmarshal(ESConfigs.class, "es.conf.xml");
        //System.out.println(esConf.getESMap().size());
        TestCase.assertNotNull(esConf);
    }
}
