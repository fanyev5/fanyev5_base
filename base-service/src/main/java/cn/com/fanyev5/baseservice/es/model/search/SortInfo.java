package cn.com.fanyev5.baseservice.es.model.search;

import cn.com.fanyev5.baseservice.es.constant.enums.SortOrderEnum;

/**
 * 排序信息
 *
 * @author fanqi427@gmail.com
 * @since 2013-12-18
 */
public class SortInfo {

    private String fieldName;

    private SortOrderEnum sortOrder;

    public SortInfo() {
    }

    public SortInfo(String fieldName, SortOrderEnum sortOrder) {
        this.fieldName = fieldName;
        this.sortOrder = sortOrder;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public SortOrderEnum getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(SortOrderEnum sortOrder) {
        this.sortOrder = sortOrder;
    }
}
