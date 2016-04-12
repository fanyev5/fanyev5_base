package cn.com.fanyev5.baseservice.es.util;

import cn.com.fanyev5.baseservice.base.config.xml.es.ESConfigs;
import cn.com.fanyev5.baseservice.es.constant.enums.SearchTypeEnum;
import cn.com.fanyev5.baseservice.es.constant.enums.SortOrderEnum;
import cn.com.fanyev5.baseservice.es.exception.ESException;
import cn.com.fanyev5.baseservice.es.exception.ParameterParseException;
import cn.com.fanyev5.baseservice.es.model.search.*;
import cn.com.fanyev5.basecommons.codec.CodecUtil;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.AndFilterBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.facet.Facet;
import org.elasticsearch.search.facet.FacetBuilder;
import org.elasticsearch.search.facet.FacetBuilders;
import org.elasticsearch.search.facet.Facets;
import org.elasticsearch.search.facet.range.RangeFacet;
import org.elasticsearch.search.facet.range.RangeFacetBuilder;
import org.elasticsearch.search.facet.terms.TermsFacet;
import org.elasticsearch.search.sort.SortOrder;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * ES工具
 *
 * @author fanqi427@gmail.com
 * @since 2013-12-17
 */
public final class ESUtil {

	private static final DecimalFormat doubleToStrFormat = new DecimalFormat("#.#");

	private ESUtil() {
	}

	/**
	 * 构建ES Client
	 *
	 * @param esElement
	 * @return
	 */
	public static Client buildClient(ESConfigs.ESElement esElement) {
		// 创建ES Setting
		ImmutableSettings.Builder builder = ImmutableSettings.settingsBuilder();
		for (ESConfigs.ConfigEntryElement configEntryElement : esElement.getConfigs()) {
			builder.put(configEntryElement.getName(), configEntryElement.getValue());
		}
		Settings settings = builder.build();
		// 创建ES Client
		TransportClient client = new TransportClient(settings);
		for (ESConfigs.ServerElement serverElement : esElement.getServers()) {
			client.addTransportAddress(
					new InetSocketTransportAddress(serverElement.getHost(), serverElement.getPort()));
		}
		return client;
	}

	/**
	 * 通用检索
	 *
	 * @param client
	 *            ES client
	 * @param indexName
	 *            索引Name
	 * @param type
	 *            类型
	 * @param searchKeyInfos
	 *            检索信息
	 * @param sortInfos
	 *            排序集
	 * @param facetInfos
	 *            Facet集
	 * @param pageIndex
	 *            页码
	 * @param pageSize
	 *            每页记录条数
	 * @return 结果集
	 */
	public static ResultInfo query(final Client client, String indexName, String type,
			List<SearchKeyInfo> searchKeyInfos, List<SortInfo> sortInfos, List<AbstractFacetInfo> facetInfos,
			List<AbstractFilterInfo> filterInfos, int pageIndex, int pageSize) {
		try {
			// 构建Facet
			List<FacetBuilder> facetBuilders = null;
			if (facetInfos != null && !facetInfos.isEmpty()) {
				facetBuilders = Lists.newArrayListWithCapacity(facetInfos.size());
				for (AbstractFacetInfo absFacetInfo : facetInfos) {
					SearchTypeEnum facetType = absFacetInfo.getFacetType();
					if (SearchTypeEnum.RANGE.equals(facetType)) {
						FacetRangeInfo facetInfo = (FacetRangeInfo) absFacetInfo;
						RangeFacetBuilder builder = FacetBuilders.rangeFacet(facetInfo.getFieldName())
								.field(facetInfo.getFieldName());
						for (FacetRangeInfo.Entry entry : facetInfo.getEntries()) {
							builder.addRange(entry.getFrom(), entry.getTo());
						}
						facetBuilders.add(builder);
					} else if (SearchTypeEnum.TERMS.equals(facetType)) {
						FacetTermsInfo facetInfo = (FacetTermsInfo) absFacetInfo;
						facetBuilders.add(FacetBuilders.termsFacet(facetInfo.getFieldName())
								.field(facetInfo.getFieldName()).size(facetInfo.getSize()));
					}
				}
			}

			// 构建Filter
			AndFilterBuilder filterBuilders = null;
			if (filterInfos != null && !filterInfos.isEmpty()) {
				filterBuilders = FilterBuilders.andFilter();
				for (AbstractFilterInfo absFilterInfo : filterInfos) {
					SearchTypeEnum facetType = absFilterInfo.getFilterType();
					if (SearchTypeEnum.RANGE.equals(facetType)) {
						FilterRangeInfo filterInfo = (FilterRangeInfo) absFilterInfo;
						filterBuilders.add(FilterBuilders.rangeFilter(filterInfo.getFieldName())
								.from(filterInfo.getEntry().getFrom()).to(filterInfo.getEntry().getTo()));
					} else if (SearchTypeEnum.TERMS.equals(facetType)) {
						FilterTermsInfo filterInfo = (FilterTermsInfo) absFilterInfo;
						filterBuilders.add(FilterBuilders.inFilter(filterInfo.getFieldName(), filterInfo.getTerms()));
					}
				}
			}

			// 查询
			SearchRequestBuilder searchRequestBuilder = client.prepareSearch(indexName).setTypes(type);
			if (searchKeyInfos != null && !searchKeyInfos.isEmpty()) {
				BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
				for (SearchKeyInfo searchKeyInfo : searchKeyInfos) {
					queryBuilder.must(
							QueryBuilders.fieldQuery(searchKeyInfo.getFieldName(), searchKeyInfo.getFieldValue()));
				}
				searchRequestBuilder.setQuery(queryBuilder);
			}

			// 加载Filter
			if (filterBuilders != null) {
				searchRequestBuilder.setFilter(filterBuilders);
			}

			// 加载Facets
			if (facetBuilders != null && !facetBuilders.isEmpty()) {
				for (FacetBuilder facetBuilder : facetBuilders) {
					if (filterBuilders != null) {
						// 加载Filter
						facetBuilder.facetFilter(filterBuilders);
					}
					searchRequestBuilder.addFacet(facetBuilder);
				}
			}
			// 排序
			if (sortInfos != null && !sortInfos.isEmpty()) {
				for (SortInfo sortInfo : sortInfos) {
					searchRequestBuilder.addSort(sortInfo.getFieldName(),
							(sortInfo.getSortOrder() == SortOrderEnum.ASC ? SortOrder.ASC : SortOrder.DESC));
				}
			}

			// 分页
			int from = (pageIndex - 1) * pageSize;
			searchRequestBuilder.setFrom(from).setSize(pageSize);
			// 执行查询
			SearchResponse response = searchRequestBuilder.execute().actionGet();
			// 结果封装
			ResultInfo resultInfo = new ResultInfo();
			// 排序
			resultInfo.setSorts(sortInfos);

			// Facets封装
			{
				Map<String, Map<String, Long>> facetsMap = Maps.newHashMap();
				Facets facets = response.getFacets();
				if (facets != null) {
					for (Map.Entry<String, Facet> facetEntry : facets.getFacets().entrySet()) {
						Map<String, Long> facetMap = Maps.newHashMap();
						if (facetEntry.getValue() instanceof TermsFacet) {
							for (TermsFacet.Entry entry : ((TermsFacet) facetEntry.getValue()).getEntries()) {
								facetMap.put(entry.getTerm().string(), entry.getCount() * 1L);
							}
						} else if (facetEntry.getValue() instanceof RangeFacet) {
							for (RangeFacet.Entry entry : ((RangeFacet) facetEntry.getValue()).getEntries()) {
								facetMap.put(String.format("%s-%s", doubleToStrFormat.format(entry.getFrom()),
										doubleToStrFormat.format(entry.getTo())), entry.getCount());
							}
						}
						facetsMap.put(facetEntry.getKey(), facetMap);
					}
				}
				resultInfo.setFacets(facetsMap);
			}
			// 检索耗时
			resultInfo.setTime(response.getTookInMillis());
			// 查询结果数据封装
			SearchHits searchHits = response.getHits();
			resultInfo.setPage(new ResultInfo.Page(searchHits.getTotalHits(), pageIndex, pageSize));
			List<Map<String, Object>> dataList = Lists.newArrayList();
			for (SearchHit hit : searchHits.hits()) {
				dataList.add(hit.getSource());
			}
			resultInfo.setItems(dataList);
			return resultInfo;
		} catch (Exception e) {
			throw new ESException(e);
		}
	}

	/**
	 * 解析搜索信息信息 格式:"tle,刘德华;tle,时尚;";
	 *
	 * @param searchKeys
	 */
	public static List<SearchKeyInfo> parseSearchKeys(String searchKeys) {
		if (StringUtils.isBlank(searchKeys)) {
			return null;
		}
		List<SearchKeyInfo> searchKeyInfos = Lists.newArrayList();
		for (String itemStr : StringUtils.split(searchKeys, ';')) {
			String[] itemArr = StringUtils.split(itemStr, ',');
			if (itemArr.length < 2 || StringUtils.isBlank(itemArr[0]) || StringUtils.isBlank(itemArr[1])) {
				throw new ParameterParseException("Parse search key exception. #1");
			}
			searchKeyInfos.add(new SearchKeyInfo(itemArr[0], itemArr[1]));
		}
		return searchKeyInfos;
	}

	/**
	 * 解析排序信息 格式:"desc,timed;asc,cid";
	 *
	 * @param sortStrs
	 */
	public static List<SortInfo> parseSorts(String sortStrs) {
		if (StringUtils.isBlank(sortStrs)) {
			return null;
		}
		List<SortInfo> sortInfos = Lists.newArrayList();
		for (String itemStr : StringUtils.split(sortStrs, ';')) {
			String[] itemArr = StringUtils.split(itemStr, ',');
			SortOrderEnum sortOrder = SortOrderEnum.valueOf(StringUtils.upperCase(itemArr[0]));
			if (sortOrder == null || itemArr.length < 2 || StringUtils.isBlank(itemArr[1])) {
				throw new ParameterParseException("Parse sorts exception.");
			}
			sortInfos.add(new SortInfo(itemArr[1], sortOrder));
		}
		return sortInfos;
	}

	/**
	 * 解析Facet信息 格式:
	 * "terms,cid,1000;range,timed,1386826041:1387430841,1384838841:1387430841,1379568441:1387430841,1355894841:1387430841,0:1387430841;";
	 *
	 * @param facetStrs
	 */
	public static List<AbstractFacetInfo> parseFacets(String facetStrs) {
		if (StringUtils.isBlank(facetStrs)) {
			return null;
		}
		List<AbstractFacetInfo> facetInfos = Lists.newArrayList();
		for (String itemStr : StringUtils.split(facetStrs, ';')) {
			String[] itemArr = StringUtils.split(itemStr, ',');
			SearchTypeEnum type = SearchTypeEnum.valueOf(StringUtils.upperCase(itemArr[0]));
			if (type == null || itemArr.length < 3 || StringUtils.isBlank(itemArr[1])
					|| StringUtils.isBlank(itemArr[2])) {
				throw new ParameterParseException("Parse facets exception. #1");
			}
			if (SearchTypeEnum.TERMS.equals(type)) {
				facetInfos.add(new FacetTermsInfo(itemArr[1], Integer.parseInt(itemArr[2])));
			} else if (SearchTypeEnum.RANGE.equals(type)) {
				List<FacetRangeInfo.Entry> entries = Lists.newArrayList();
				for (int i = 2; i < itemArr.length; i++) {
					for (String info : StringUtils.split(itemArr[i], ',')) {
						String[] infoArr = StringUtils.split(info, ':');
						if (StringUtils.isBlank(infoArr[0]) || StringUtils.isBlank(infoArr[1])) {
							throw new ParameterParseException("Parse facets exception. #3");
						}
						entries.add(new FacetRangeInfo.Entry(Double.parseDouble(infoArr[0]),
								Double.parseDouble(infoArr[1])));
					}
				}
				facetInfos.add(new FacetRangeInfo(itemArr[1], entries));
			}
		}
		return facetInfos;
	}

	/**
	 * 解析过滤器信息
	 * 格式:"terms,cid,314,611;range,id,0:100000;range,timed,0:1259051280;";
	 * timStrs = "cnt,30;";
	 *
	 * @param filterStrs
	 */
	public static List<AbstractFilterInfo> parseFilters(String filterStrs) {
		if (StringUtils.isBlank(filterStrs)) {
			return null;
		}
		List<AbstractFilterInfo> filterInfos = Lists.newArrayList();
		for (String itemStr : StringUtils.split(filterStrs, ';')) {
			String[] itemArr = StringUtils.split(itemStr, ',');
			SearchTypeEnum type = SearchTypeEnum.valueOf(StringUtils.upperCase(itemArr[0]));
			if (type == null || itemArr.length < 3 || StringUtils.isBlank(itemArr[1])
					|| StringUtils.isBlank(itemArr[2])) {
				throw new ParameterParseException("Parse filter exception. #1");
			}
			if (SearchTypeEnum.TERMS.equals(type)) {
				List<String> terms = Lists.newArrayList();
				for (int i = 2; i < itemArr.length; i++) {
					if (StringUtils.isBlank(itemArr[i])) {
						throw new ParameterParseException("Parse filter exception. #2");
					}
					terms.add(itemArr[i]);
				}
				filterInfos.add(new FilterTermsInfo(itemArr[1], terms.toArray()));
			} else if (SearchTypeEnum.RANGE.equals(type)) {
				if (StringUtils.isBlank(itemArr[2])) {
					throw new ParameterParseException("Parse filter exception. #3");
				}
				String[] infoArr = StringUtils.split(itemArr[2], ':');
				if (infoArr.length < 2 || StringUtils.isBlank(infoArr[0]) || StringUtils.isBlank(infoArr[1])) {
					throw new ParameterParseException("Parse filter exception. #4");
				}
				filterInfos.add(new FilterRangeInfo(itemArr[1], Double.parseDouble(infoArr[0]),
						Double.parseDouble(infoArr[1])));
			}
		}
		return filterInfos;
	}

	/**
	 * 解析属性值长度信息 格式:"cnt,30;";
	 *
	 * @param trimStrs
	 */
	public static List<ValueTrimInfo> parseValueTrims(String trimStrs) {
		if (StringUtils.isBlank(trimStrs)) {
			return null;
		}
		List<ValueTrimInfo> trimInfos = Lists.newArrayList();
		for (String itemStr : StringUtils.split(trimStrs, ';')) {
			String[] itemArr = StringUtils.split(itemStr, ',');
			if (itemArr.length < 2 || StringUtils.isBlank(itemArr[0]) || StringUtils.isBlank(itemArr[1])) {
				throw new ParameterParseException("Parse value trim exception. #1");
			}
			trimInfos.add(new ValueTrimInfo(itemArr[0], Integer.parseInt(itemArr[1])));
		}
		return trimInfos;
	}

	/**
	 * 格式化请求参数
	 *
	 * @param requestParameter
	 * @return
	 */
	public static String formatRUI(SearchRequestParameter requestParameter) {
		StringBuilder uriStrbui = new StringBuilder();
		uriStrbui.append("key=").append(requestParameter.getIndexName());
		uriStrbui.append("&p=").append(requestParameter.getPageIndex());
		uriStrbui.append("&n=").append(requestParameter.getPageSize());
		if (requestParameter.getSearchKeyInfos() != null) {
			StringBuilder strbui = new StringBuilder();
			for (SearchKeyInfo info : requestParameter.getSearchKeyInfos()) {
				strbui.append(info.getFieldName()).append(',').append(info.getFieldValue()).append(';');
			}
			uriStrbui.append("&q=").append(CodecUtil.encodeBase64(strbui.toString()));
		}
		if (requestParameter.getSortInfos() != null) {
			StringBuilder strbui = new StringBuilder();
			for (SortInfo info : requestParameter.getSortInfos()) {
				strbui.append(info.getSortOrder().name()).append(',').append(info.getFieldName()).append(';');
			}
			uriStrbui.append("&srts=").append(CodecUtil.encodeBase64(strbui.toString()));
		}
		if (requestParameter.getTrimInfos() != null) {
			StringBuilder strbui = new StringBuilder();
			for (ValueTrimInfo info : requestParameter.getTrimInfos()) {
				strbui.append(info.getFieldName()).append(',').append(info.getLength()).append(';');
			}
			uriStrbui.append("&tims=").append(CodecUtil.encodeBase64(strbui.toString()));
		}
		if (requestParameter.getFacetInfos() != null) {
			StringBuilder strbui = new StringBuilder();
			for (AbstractFacetInfo info : requestParameter.getFacetInfos()) {
				if (info instanceof FacetTermsInfo) {
					FacetTermsInfo termsInfo = (FacetTermsInfo) info;
					strbui.append(termsInfo.getFacetType().name()).append(',').append(termsInfo.getFieldName())
							.append(',').append(termsInfo.getSize()).append(';');
				} else if (info instanceof FacetRangeInfo) {
					FacetRangeInfo rangeInfo = (FacetRangeInfo) info;
					strbui.append(rangeInfo.getFacetType().name()).append(',').append(rangeInfo.getFieldName())
							.append(',');
					for (FacetRangeInfo.Entry entry : rangeInfo.getEntries()) {
						strbui.append(doubleToStrFormat.format(entry.getFrom())).append(':')
								.append(doubleToStrFormat.format(entry.getTo())).append(',');
					}
					strbui.append(';');
				}
			}
			uriStrbui.append("&fets=").append(CodecUtil.encodeBase64(strbui.toString()));
		}
		if (requestParameter.getFilterInfos() != null) {
			StringBuilder strbui = new StringBuilder();
			for (AbstractFilterInfo info : requestParameter.getFilterInfos()) {
				if (info instanceof FilterTermsInfo) {
					FilterTermsInfo termsInfo = (FilterTermsInfo) info;
					strbui.append(termsInfo.getFilterType().name()).append(',').append(termsInfo.getFieldName())
							.append(',').append(Joiner.on(',').join(termsInfo.getTerms())).append(';');
				} else if (info instanceof FilterRangeInfo) {
					FilterRangeInfo rangeInfo = (FilterRangeInfo) info;
					strbui.append(rangeInfo.getFilterType().name()).append(',').append(rangeInfo.getFieldName())
							.append(',');
					strbui.append(doubleToStrFormat.format(rangeInfo.getEntry().getFrom())).append(':')
							.append(doubleToStrFormat.format(rangeInfo.getEntry().getTo()));
					strbui.append(';');
				}
			}
			uriStrbui.append("&fers=").append(CodecUtil.encodeBase64(strbui.toString()));
		}
		return uriStrbui.toString();
	}
}
