package cn.com.fanyev5.baseservice.es.model.search;

import java.util.List;

/**
 * 搜索请求参数封装
 *
 * @author fanqi427@gmail.com
 * @since 2014-01-03
 */
public class SearchRequestParameter {
    /**
     * 索引名称
     */
    private String indexName;
    /**
     * 页码
     */
    private int pageIndex;
    /**
     * 每页记录条数
     */
    private int pageSize;
    /**
     * 搜索词集
     */
    private List<SearchKeyInfo> searchKeyInfos;
    /**
     * 排序属性集
     */
    private List<SortInfo> sortInfos;
    /**
     * Facet集
     */
    private List<AbstractFacetInfo> facetInfos;
    /**
     * 过滤属性集
     */
    private List<AbstractFilterInfo> filterInfos;
    /**
     * 属性内容长度限制集
     */
    private List<ValueTrimInfo> trimInfos;

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<SearchKeyInfo> getSearchKeyInfos() {
        return searchKeyInfos;
    }

    public void setSearchKeyInfos(List<SearchKeyInfo> searchKeyInfos) {
        this.searchKeyInfos = searchKeyInfos;
    }

    public List<SortInfo> getSortInfos() {
        return sortInfos;
    }

    public void setSortInfos(List<SortInfo> sortInfos) {
        this.sortInfos = sortInfos;
    }

    public List<AbstractFacetInfo> getFacetInfos() {
        return facetInfos;
    }

    public void setFacetInfos(List<AbstractFacetInfo> facetInfos) {
        this.facetInfos = facetInfos;
    }

    public List<AbstractFilterInfo> getFilterInfos() {
        return filterInfos;
    }

    public void setFilterInfos(List<AbstractFilterInfo> filterInfos) {
        this.filterInfos = filterInfos;
    }

    public List<ValueTrimInfo> getTrimInfos() {
        return trimInfos;
    }

    public void setTrimInfos(List<ValueTrimInfo> trimInfos) {
        this.trimInfos = trimInfos;
    }
}
