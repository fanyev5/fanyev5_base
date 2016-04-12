package cn.com.fanyev5.baseservice.es.model.search;

import cn.com.fanyev5.baseservice.es.constant.enums.SearchTypeEnum;

/**
 * 过滤抽象信息
 *
 * @author fanqi427@gmail.com
 * @since 2013-12-18
 */
public abstract class AbstractFilterInfo {

    protected SearchTypeEnum filterType;

    protected AbstractFilterInfo(SearchTypeEnum filterType) {
        this.filterType = filterType;
    }

    public SearchTypeEnum getFilterType() {
        return filterType;
    }
}
