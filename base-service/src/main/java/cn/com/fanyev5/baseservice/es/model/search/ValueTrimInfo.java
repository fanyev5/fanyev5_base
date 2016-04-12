package cn.com.fanyev5.baseservice.es.model.search;

/**
 * 属性值长度控制
 *
 * @author fanqi427@gmail.com
 * @since 2013-12-18
 */
public class ValueTrimInfo {

    private String fieldName;

    private int length = 10;

    public ValueTrimInfo(String fieldName, int length) {
        this.fieldName = fieldName;
        this.length = length;
    }

    public String getFieldName() {
        return fieldName;
    }

    public int getLength() {
        return length;
    }
}
