package cn.com.fanyev5.baseservice.es.model.search;

/**
 * 搜索信息
 *
 * @author fanqi427@gmail.com
 * @since 2013-12-18
 */
public class SearchKeyInfo {

    private String fieldName;

    private String fieldValue;

    public SearchKeyInfo(String fieldName, String fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }
}
