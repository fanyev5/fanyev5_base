package cn.com.fanyev5.baseservice.es.model.search;

import cn.com.fanyev5.baseservice.es.constant.enums.SearchTypeEnum;

/**
 * Filter range 信息
 *
 * @author fanqi427@gmail.com
 * @since 2013-12-18
 */
public class FilterRangeInfo extends AbstractFilterInfo {

    private String fieldName;

    private Entry entry;

    public FilterRangeInfo(String fieldName, double from, double to) {
        super(SearchTypeEnum.RANGE);
        this.fieldName = fieldName;
        this.entry = new Entry(from, to);
    }

    public String getFieldName() {
        return fieldName;
    }

    public Entry getEntry() {
        return entry;
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
