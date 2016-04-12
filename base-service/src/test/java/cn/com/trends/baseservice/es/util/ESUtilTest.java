package cn.com.trends.baseservice.es.util;

import cn.com.fanyev5.baseservice.es.model.search.*;
import cn.com.fanyev5.baseservice.es.util.ESUtil;
import cn.com.fanyev5.basecommons.codec.JSONUtil;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.List;

/**
 * ESUtil Test
 *
 * @author fanqi427@gmail.com
 * @since 2013-12-19
 */
public class ESUtilTest {

    @Test
    public void testParseSearchKeys() {
        List<SearchKeyInfo> list = ESUtil.parseSearchKeys("tle,刘德华;tle,时尚;");
        TestCase.assertNotNull(list);
        System.out.println(JSONUtil.obj2Json(list));
    }

    @Test
    public void testParseSorts() {
        List<SortInfo> list = ESUtil.parseSorts("desc,timed;asc,cid");
        TestCase.assertNotNull(list);
        System.out.println(JSONUtil.obj2Json(list));
    }

    @Test
    public void testParseFacets() {
        List<AbstractFacetInfo> list = ESUtil.parseFacets("terms,cid,1000;range,timed,1386826041:1387430841,1384838841:1387430841,1379568441:1387430841,1355894841:1387430841,0:1387430841;");
        TestCase.assertNotNull(list);
        System.out.println(JSONUtil.obj2Json(list));
    }

    @Test
    public void testParseFilters() {
        List<AbstractFilterInfo> list = ESUtil.parseFilters("terms,cid,314,611;range,id,0:100000;range,timed,0:200000;");
        TestCase.assertNotNull(list);
        System.out.println(JSONUtil.obj2Json(list));
    }

    @Test
    public void testParseValueTrims() {
        List<ValueTrimInfo> list = ESUtil.parseValueTrims("cnt,30;");
        TestCase.assertNotNull(list);
        System.out.println(JSONUtil.obj2Json(list));
    }
}
