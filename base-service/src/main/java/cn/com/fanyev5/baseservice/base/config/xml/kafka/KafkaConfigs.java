package cn.com.fanyev5.baseservice.base.config.xml.kafka;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * Kafka配置文件
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-23
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "root")
public class KafkaConfigs {

    @XmlElement(name = "kafka", required = true)
    private KafkaElement kafka = null;

    public Map<String, ConsumerElement> getConsumers() {
        Map<String, ConsumerElement> elementMap = Maps.newLinkedHashMap();
        if (this.kafka != null) {
            List<ConsumerElement> consumers = this.kafka.getConsumers();
            for (ConsumerElement element : consumers) {
                elementMap.put(element.getName(), element);
            }
        }
        return elementMap;
    }

    public Map<String, ProducerElement> getProducers() {
        Map<String, ProducerElement> elementMap = Maps.newLinkedHashMap();
        if (this.kafka != null) {
            List<ProducerElement> producers = this.kafka.getProducers();
            for (ProducerElement element : producers) {
                elementMap.put(element.getName(), element);
            }
        }
        return elementMap;
    }

    public Map<String, ZookeeperElement> getZookeepers() {
        Map<String, ZookeeperElement> elementMap = Maps.newLinkedHashMap();
        if (this.kafka != null) {
            List<ZookeeperElement> zookeepers = this.kafka.getZookeepers();
            for (ZookeeperElement element : zookeepers) {
                elementMap.put(element.getName(), element);
            }
        }
        return elementMap;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "kafka")
    public static class KafkaElement {

        @XmlElementWrapper(name = "zookeepers", required = true)
        @XmlElement(name = "zookeeper", required = true)
        private List<ZookeeperElement> zookeepers = Lists.newArrayList();

        @XmlElementWrapper(name = "consumers")
        @XmlElement(name = "consumer", required = true)
        private List<ConsumerElement> consumers = Lists.newArrayList();

        @XmlElementWrapper(name = "producers")
        @XmlElement(name = "producer", required = true)
        private List<ProducerElement> producers = Lists.newArrayList();

        public List<ZookeeperElement> getZookeepers() {
            return zookeepers;
        }

        public List<ConsumerElement> getConsumers() {
            return consumers;
        }

        public List<ProducerElement> getProducers() {
            return producers;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "zookeepers")
    public static class ZookeeperElement {

        @XmlAttribute(name = "name", required = true)
        private String name;

        @XmlElement(name = "chroot", required = false)
        private String chroot;

        @XmlElementWrapper(name = "configs")
        @XmlElement(name = "entry", required = true)
        private List<ConfigEntryElement> configs = Lists.newArrayList();

        public String getName() {
            return name;
        }

        public Map<String, String> getConfigs() {
            return KafkaConfigs.getConfigs(this.configs);
        }

        public String getChroot() {
            return chroot;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "consumers")
    public static class ConsumerElement {

        @XmlAttribute(name = "name", required = true)
        private String name;
        @XmlElement(name = "zookeeper", required = true)

        private String zookeeper;
        @XmlElement(name = "serializer", required = true)
        private String serializer;

        @XmlElementWrapper(name = "topics", required = true)
        @XmlElement(name = "topic")
        private List<TopicElement> topics = Lists.newArrayList();

        @XmlElementWrapper(name = "configs")
        @XmlElement(name = "entry")
        private List<ConfigEntryElement> configs = Lists.newArrayList();

        public String getName() {
            return name;
        }

        public String getZookeeper() {
            return zookeeper;
        }

        public List<TopicElement> getTopics() {
            return topics;
        }

        public Map<String, String> getConfigs() {
            return KafkaConfigs.getConfigs(this.configs);
        }

        public String getSerializer() {
            return serializer;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "producers")
    public static class ProducerElement {

        @XmlAttribute(name = "name", required = true)
        private String name;

        @XmlElement(name = "zookeeper")
        private String zookeeper;

        @XmlElement(name = "serializer", required = true)
        private String serializer;

        @XmlElementWrapper(name = "configs")
        @XmlElement(name = "entry")
        private List<ConfigEntryElement> configs = Lists.newArrayList();

        public String getName() {
            return name;
        }

        public String getZookeeper() {
            return zookeeper;
        }

        public String getSerializer() {
            return serializer;
        }

        public Map<String, String> getConfigs() {
            return KafkaConfigs.getConfigs(this.configs);
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "configs")
    public static class ConfigEntryElement {

        @XmlAttribute(name = "name", required = true)
        private String name;

        @XmlValue
        private String value;

        @XmlAttribute(name = "value", required = false)
        private String valueAttr;

        public String getName() {
            return name;
        }

        public String getValue() {
            if (valueAttr != null) {
                return valueAttr;
            }
            return value;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "topics")
    public static class TopicElement {

        @XmlElement(name = "name", required = true)
        private String name;

        @XmlElement(name = "streamcount", required = true)
        private int streamcount;

        @XmlElement(name = "processor", required = true)
        private ProcessorElement processor;

        public String getName() {
            return name;
        }

        public int getStreamcount() {
            return streamcount;
        }

        public ProcessorElement getProcessor() {
            return processor;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ProcessorElement {

        @XmlAttribute(name = "model", required = true)
        private String model;

        @XmlValue
        private String className;

        public String getModel() {
            return model;
        }

        public String getClassName() {
            return className;
        }
    }

    private static Map<String, String> getConfigs(final List<ConfigEntryElement> configs) {
        Map<String, String> elementMap = Maps.newLinkedHashMap();
        if (configs != null) {
            for (ConfigEntryElement element : configs) {
                elementMap.put(element.getName(), element.getValue());
            }
        }
        return elementMap;
    }

}
