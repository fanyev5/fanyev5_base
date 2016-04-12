package cn.com.fanyev5.baseservice.es.model.search;

import java.util.List;

import cn.com.fanyev5.baseservice.es.constant.enums.SearchTypeEnum;

/**
 * Facet range 信息
 *
 * @author fanqi427@gmail.com
 * @since 2013-12-18
 */
public class FacetRangeInfo extends AbstractFacetInfo {

    private String fieldName;

    private List<Entry> entries;

    public FacetRangeInfo(String fieldName, List<Entry> entries) {
        super(SearchTypeEnum.RANGE);
        this.fieldName = fieldName;
        this.entries = entries;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public static class Entry {
        private double from = 0;

        private double to = 0;

        public Entry(double from, double to) {
            this.from = from;
            this.to = to;
        }

        public double getFrom() {
            return from;
        }

        public double getTo() {
            return to;
        }
    }
}
