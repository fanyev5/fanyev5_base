package cn.com.fanyev5.baseservice.base.config.xml.server;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.com.fanyev5.baseservice.base.config.xml.common.XmlPropertyElement;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 通用服务组配置
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-26
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "root")
public class ServerGroupConfigs {
    @XmlJavaTypeAdapter(ServerMapAdapter.class)
    private Map<String, Server> servers = Maps.newHashMap();

    @XmlJavaTypeAdapter(GroupMapAdapter.class)
    private Map<String, Group> groups = Maps.newHashMap();

    /**
     * 取得服务器列表
     *
     * @return
     */
    public Map<String, Server> getServers() {
        return servers;
    }

    public void setServers(Map<String, Server> servers) {
        this.servers = servers;
    }

    /**
     * 获取服务组列表
     *
     * @return
     */
    public Map<String, Group> getGroups() {
        return groups;
    }

    public void setGroups(Map<String, Group> groups) {
        this.groups = groups;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "server")
    public static class Server {
        @XmlAttribute(name = "name", required = true)
        private String name;
        @XmlAttribute(name = "port", required = true)
        private String port;
        @XmlAttribute(name = "host", required = true)
        private String host;
        @XmlAttribute(name = "password", required = false)
        private String password = "";
        @XmlAttribute(name = "passport", required = false)
        private String passport = "";

        @XmlJavaTypeAdapter(XmlPropertyElement.MapAdapter.class)
        private Map<String, String> properties = Maps.newHashMap();

        public Map<String, String> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, String> properties) {
            this.properties = properties;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPassport() {
            return passport;
        }

        public void setPassport(String passport) {
            this.passport = passport;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "groups")
    public static class Group {
        @XmlAttribute(name = "name", required = true)
        private String name;

        @XmlElementWrapper(name = "servers", required = true)
        @XmlElement(name = "server", required = true)
        private List<String> servers;

        @XmlJavaTypeAdapter(XmlPropertyElement.MapAdapter.class)
        private Map<String, String> properties = Maps.newHashMap();

        public List<String> getServers() {
            return servers;
        }

        public void setServers(List<String> servers) {
            this.servers = servers;
        }

        public Map<String, String> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, String> properties) {
            this.properties = properties;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private static final class ServerMapAdapter extends XmlAdapter<ServerConfigType, Map<String, Server>> {
        public ServerMapAdapter() {
        }

        @Override
        public Map<String, Server> unmarshal(ServerConfigType v) {
            Map<String, Server> map = Maps.newHashMap();
            for (Server p : v.getServers()) {
                Preconditions.checkArgument(!map.containsKey(p.getName()), "Duplicate key %s", p.getName());
                map.put(p.getName(), p);
            }
            return map;
        }

        @Override
        public ServerConfigType marshal(Map<String, Server> v) {
            List<Server> list = Lists.newArrayList();
            Set<Map.Entry<String, Server>> entries = v.entrySet();
            for (Map.Entry<String, Server> entry : entries) {
                list.add(entry.getValue());
            }
            ServerConfigType serverConfigType = new ServerConfigType();
            serverConfigType.setServers(list);
            return serverConfigType;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    private static class ServerConfigType {
        @XmlElement(name = "server")
        private List<Server> servers;

        public List<Server> getServers() {
            return servers;
        }

        public void setServers(List<Server> servers) {
            this.servers = servers;
        }
    }

    private static final class GroupMapAdapter extends XmlAdapter<GroupConfigType, Map<String, Group>> {
        public GroupMapAdapter() {
        }

        @Override
        public Map<String, Group> unmarshal(GroupConfigType v) {
            Map<String, Group> map = Maps.newHashMap();
            for (Group p : v.getGroups()) {
                Preconditions.checkArgument(!map.containsKey(p.getName()), "Duplicate key %s", p.getName());
                map.put(p.getName(), p);
            }
            return map;
        }

        @Override
        public GroupConfigType marshal(Map<String, Group> v) {
            List<Group> list = Lists.newArrayList();
            Set<Map.Entry<String, Group>> entries = v.entrySet();
            for (Map.Entry<String, Group> entry : entries) {
                list.add(entry.getValue());
            }
            GroupConfigType groupConfigType = new GroupConfigType();
            groupConfigType.setGroups(list);
            return groupConfigType;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    private static class GroupConfigType {
        @XmlElement(name = "group")
        private List<Group> groups;

        public List<Group> getGroups() {
            return groups;
        }

        public void setGroups(List<Group> groups) {
            this.groups = groups;
        }
    }

}