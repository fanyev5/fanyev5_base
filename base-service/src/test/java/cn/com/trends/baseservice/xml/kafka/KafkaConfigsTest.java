package cn.com.trends.baseservice.xml.kafka;

import cn.com.fanyev5.baseservice.base.config.xml.kafka.KafkaConfigs;
import cn.com.fanyev5.baseservice.kafka.constants.KafkaConstants;
import cn.com.fanyev5.basecommons.xml.JAXBUtil;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * KafkaConfigs Test
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-23
 */
public class KafkaConfigsTest {

    @Test
    public void testConfig() {
        KafkaConfigs kafkaConfigs = JAXBUtil.unmarshal(KafkaConfigs.class, KafkaConstants.KAFKA_CONFIG_FILE_NAME);
        TestCase.assertNotNull(kafkaConfigs);
    }
}
