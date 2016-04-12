package cn.com.fanyev5.baseservice.es.model.search;

import cn.com.fanyev5.baseservice.es.constant.enums.SearchTypeEnum;

/**
 * Facet terms 信息
 *
 * @author fanqi427@gmail.com
 * @since 2013-12-18
 */
public class FacetTermsInfo extends AbstractFacetInfo {

    private String fieldName;

    private int size = 10;

    public FacetTermsInfo(String fieldName, int size) {
        super(SearchTypeEnum.TERMS);
        this.fieldName = fieldName;
        this.size = size;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
