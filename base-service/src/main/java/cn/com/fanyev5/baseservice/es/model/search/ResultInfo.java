package cn.com.fanyev5.baseservice.es.model.search;

import java.util.List;
import java.util.Map;

/**
 * 结果信息
 *
 * @author fanqi427@gmail.com
 * @since 2013-12-18
 */
public class ResultInfo {

    /**
     * 分页页码
     */
    private Page page;

    /**
     * 排序字段,有序
     */
    private List<SortInfo> sorts;

    /**
     * Facets集
     */
    private Map<String, Map<String, Long>> facets;

    /**
     * 结果集
     */
    private List<Map<String, Object>> items;

    /**
     * 耗时, 单位:毫秒
     */
    private long time;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<SortInfo> getSorts() {
        return sorts;
    }

    public void setSorts(List<SortInfo> sorts) {
        this.sorts = sorts;
    }

    public Map<String, Map<String, Long>> getFacets() {
        return facets;
    }

    public void setFacets(Map<String, Map<String, Long>> facets) {
        this.facets = facets;
    }

    public List<Map<String, Object>> getItems() {
        return items;
    }

    public void setItems(List<Map<String, Object>> items) {
        this.items = items;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public static class Page {
        private long total;
        private int index;
        private int size;

        public Page() {
        }

        public Page(long total, int index, int size) {
            this.total = total;
            this.index = index;
            this.size = size;
        }

        public long getTotal() {
            return total;
        }

        public void setTotal(long total) {
            this.total = total;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }
}
