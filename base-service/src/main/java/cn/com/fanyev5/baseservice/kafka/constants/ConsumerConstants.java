package cn.com.fanyev5.baseservice.kafka.constants;

/**
 * Kafka consumer常量
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-23
 */
public final class ConsumerConstants {

    private ConsumerConstants() {
    }

    /**
     * group和topic的名字的format
     */
    public static final String GROUP_TOPIC_FORMAT = "%s" + ConsumerConstants.GROUP_TOPIC_SEQ + "%s";

    /**
     * group与topic的分隔符
     */
    public static final String GROUP_TOPIC_SEQ = "<|@.@|>";

    /**
     * 默认的参数
     */
    public static final String[][] DEFAULT_CONFIG = {
            {"zk.sessiontimeout.ms", "400"},
            {"zk.connectiontimeout.ms", "6000"},
            {"zk.synctime.ms", "200"},

            {"socket.timeout.ms", "30000"},
            {"socket.buffersize", String.valueOf(64 * 1024)},
            {"fetch.size", String.valueOf(300 * 1024)},
            {"queuedchunks.max", "100"},
            {"autocommit.enable", "true"},
            {"autocommit.interval.ms", "10000"},
            {"autooffset.reset", "smallest"},
            {"rebalance.retries.max", "4"}
    };
    /**
     * 固定的参数
     */
    public static final String[][] FIXED_CONFIG = {
            {"consumer.timeout.ms", "-1"}
    };
}
