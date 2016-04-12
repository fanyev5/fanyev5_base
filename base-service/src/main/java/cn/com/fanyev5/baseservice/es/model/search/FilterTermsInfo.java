package cn.com.fanyev5.baseservice.es.model.search;

import cn.com.fanyev5.baseservice.es.constant.enums.SearchTypeEnum;

/**
 * Filter terms 信息
 *
 * @author fanqi427@gmail.com
 * @since 2013-12-18
 */
public class FilterTermsInfo extends AbstractFilterInfo {

    private String fieldName;

    private Object[] terms;

    public FilterTermsInfo(String fieldName, Object[] terms) {
        super(SearchTypeEnum.TERMS);
        this.fieldName = fieldName;
        this.terms = terms;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object[] getTerms() {
        return terms;
    }
}
