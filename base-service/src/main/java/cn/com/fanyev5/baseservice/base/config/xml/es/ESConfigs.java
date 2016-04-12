package cn.com.fanyev5.baseservice.base.config.xml.es;

import cn.com.fanyev5.basecommons.search.SearchConfig;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.Map;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "root")
public class ESConfigs implements SearchConfig {
    @XmlElementWrapper(name = "ess")
    @XmlElement(name = "es", required = true)
    private List<ESElement> ess = Lists.newArrayList();
    @XmlElementWrapper(name = "indexes")
    @XmlElement(name = "index", required = true)
    private List<IndexElement> indexes = Lists.newArrayList();

    public Map<String, IndexElement> getIndexMap() {
        Map<String, IndexElement> elementMap = Maps.newLinkedHashMap();
        if (this.indexes != null) {
            List<IndexElement> indexes = this.indexes;
            for (IndexElement element : indexes) {
                elementMap.put(element.getName(), element);
            }
        }
        return elementMap;
    }

    public Map<String, ESElement> getESMap() {
        Map<String, ESElement> elementMap = Maps.newLinkedHashMap();
        if (this.ess != null) {
            List<ESElement> ess = this.ess;
            for (ESElement element : ess) {
                elementMap.put(element.getName(), element);
            }
        }
        return elementMap;
    }

    public List<ESElement> getEss() {
        return ess;
    }

    public List<IndexElement> getIndexes() {
        return indexes;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "es")
    public static class ESElement {
        @XmlAttribute(name = "name", required = true)
        private String name;
        @XmlElementWrapper(name = "servers")
        @XmlElement(name = "server", required = true)
        private List<ServerElement> servers = Lists.newArrayList();
        @XmlElementWrapper(name = "configs")
        @XmlElement(name = "entry", required = true)
        private List<ConfigEntryElement> configs = Lists.newArrayList();

        public String getName() {
            return name;
        }

        public List<ServerElement> getServers() {
            return servers;
        }

        public List<ConfigEntryElement> getConfigs() {
            return configs;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "index")
    public static class IndexElement {
        @XmlAttribute(name = "name", required = true)
        private String name;
        @XmlElement(name = "es", required = true)
        private String es = null;
        @XmlAttribute(name = "indexName", required = true)
        private String indexName;
        @XmlAttribute(name = "indexType", required = true)
        private String indexType;
        @XmlElement(name = "dataSource", required = true)
        private DataSource dataSource = null;
        @XmlElementWrapper(name = "fields")
        @XmlElement(name = "field", required = true)
        private List<FieldElement> fields = Lists.newArrayList();

        public String getName() {
            return name;
        }

        public String getEs() {
            return es;
        }

        public String getIndexName() {
            return indexName;
        }

        public String getIndexType() {
            return indexType;
        }

        public DataSource getDataSource() {
            return dataSource;
        }

        public List<FieldElement> getFields() {
            return fields;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "dataSource")
    public static class DataSource {
        @XmlElement(name = "type", required = true)
        private String type;
        @XmlElement(name = "jdbcDriver", required = true)
        private String jdbcDriver;
        @XmlElement(name = "jdbcUrl", required = true)
        private String jdbcUrl;
        @XmlElement(name = "jdbcUser", required = true)
        private String jdbcUser;
        @XmlElement(name = "jdbcPwd", required = true)
        private String jdbcPwd;
        @XmlElement(name = "jdbcQueryLoad", required = true)
        private String jdbcQueryLoad;
        @XmlElement(name = "jdbcQueryOne", required = true)
        private String jdbcQueryOne;

        public String getType() {
            return type;
        }

        public String getJdbcDriver() {
            return jdbcDriver;
        }

        public String getJdbcUrl() {
            return jdbcUrl;
        }

        public String getJdbcUser() {
            return jdbcUser;
        }

        public String getJdbcPwd() {
            return jdbcPwd;
        }

        public String getJdbcQueryLoad() {
            return jdbcQueryLoad;
        }

        public String getJdbcQueryOne() {
            return jdbcQueryOne;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "fields")
    public static class FieldElement {
        @XmlAttribute(name = "name", required = true)
        private String name;
        @XmlValue
        private String value;
        @XmlAttribute(name = "type")
        private String type;
        @XmlAttribute(name = "isId", required = true)
        private boolean isId;

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public boolean isId() {
            return isId;
        }
        
        public String getType() {
            return type;
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
    @XmlRootElement(name = "server")
    public static class ServerElement {
        @XmlAttribute(name = "host", required = true)
        private String host;
        @XmlAttribute(name = "port", required = true)
        private int port;

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }
    }
}
