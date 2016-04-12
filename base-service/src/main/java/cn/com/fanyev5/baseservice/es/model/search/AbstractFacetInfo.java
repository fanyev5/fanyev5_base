package cn.com.fanyev5.baseservice.es.model.search;

import cn.com.fanyev5.baseservice.es.constant.enums.SearchTypeEnum;

/**
 * Facet抽象信息
 *
 * @author fanqi427@gmail.com
 * @since 2013-12-18
 */
public abstract class AbstractFacetInfo {

    protected SearchTypeEnum facetType;

    protected AbstractFacetInfo(SearchTypeEnum facetType) {
        this.facetType = facetType;
    }

    public SearchTypeEnum getFacetType() {
        return facetType;
    }
}
