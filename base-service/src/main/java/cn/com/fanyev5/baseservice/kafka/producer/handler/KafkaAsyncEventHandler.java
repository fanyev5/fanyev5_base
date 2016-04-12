package cn.com.fanyev5.baseservice.kafka.producer.handler;

import kafka.producer.ProducerConfig;
import kafka.producer.SyncProducer;
import kafka.producer.async.CallbackHandler;
import kafka.producer.async.EventHandler;
import kafka.producer.async.QueueItem;
import kafka.serializer.Encoder;
import kafka.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.fanyev5.baseservice.kafka.util.KafkaUtil;
import scala.collection.Seq;

import java.util.Map;
import java.util.Properties;

/**
 * Kafka handler, 在Producer async时,发送失败记录本地日志处理
 *
 * @author fanqi427@gmail.com
 * @since 2013-08-21
 */
public class KafkaAsyncEventHandler<T> implements EventHandler<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaAsyncEventHandler.class);
    private static final Logger LOGGER_RECOVER = LoggerFactory.getLogger("recover");
    private kafka.producer.async.EventHandler eventHandler;

    @Override
    public void init(Properties props) {
        Properties processedProps = new Properties();
        for (Map.Entry<Object, Object> entry : props.entrySet()) {
            String name = entry.getKey().toString();
            // 对于存在,的value值,在加入配置时会把它换成<#@#>,所以这里要得到原来的值
            String value = KafkaUtil.decodeConfigValue(entry.getValue().toString());
            processedProps.put(name, value);
        }
        ProducerConfig config = new ProducerConfig(processedProps);
        CallbackHandler cbkHandler = Utils.getObject(config.cbkHandler());
        if (cbkHandler != null) {
            cbkHandler.init(config.cbkHandlerProps());
        }
        eventHandler = new kafka.producer.async.DefaultEventHandler<T>(config, cbkHandler);
    }

    @Override
    public void handle(Seq<QueueItem<T>> events, SyncProducer producer, Encoder<T> encoder) {
        try {
            eventHandler.handle(events, producer, encoder);
        } catch (Exception e) {
            LOGGER.error("Handle error,try to log recover data.", e);
            scala.collection.Iterator<QueueItem<T>> iterator = events.iterator();
            while (iterator.hasNext()) {
                try {
                    QueueItem<T> event = iterator.next();
                    if (event == null) {
                        continue;
                    }
                    String data = KafkaUtil.toBase64(event.getData());
                    LOGGER_RECOVER.warn(
                            "Recover.partition:{},topic:{},data:{}",
                            new Object[]{
                                    event.getPartition(),
                                    event.getTopic(),
                                    data
                            });
                } catch (Exception re) {
                    LOGGER.error("Log recover fail.", re);
                }
            }
        }
    }

    @Override
    public void close() {
    }
}
