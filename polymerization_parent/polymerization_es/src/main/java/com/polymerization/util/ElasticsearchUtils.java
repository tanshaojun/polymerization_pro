package com.polymerization.util;

import com.alibaba.fastjson.JSONObject;
import com.polymerization.controller.EsPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequest;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * name
 * Date: 2018/8/1
 * Time: 下午9:35
 */
@Slf4j
@Component
public class ElasticsearchUtils {

    @Autowired
    private TransportClient transportClient;

    private static TransportClient client;

    @PostConstruct
    public void init() {
        client = this.transportClient;
    }

    public static void testParticiple(String index) {
        //standard 标准分词
        //ik_max_word ik分词
        //ik_smart ik分词
        AnalyzeRequest analyzeRequest = new AnalyzeRequest(index)
                .text("中华人民共和国国歌")
                .analyzer("ik_smart");
        List<AnalyzeResponse.AnalyzeToken> tokens = client.admin().indices()
                .analyze(analyzeRequest)
                .actionGet()
                .getTokens();
        for (AnalyzeResponse.AnalyzeToken token : tokens) {
            System.out.println(token.getTerm());
        }
    }

    /**
     * 创建索引
     *
     * @param index
     * @return
     */
    public static boolean createIndex(String index, String type) throws IOException {
        if (!isIndexExist(index)) {
            log.info("Index is not exits!");
        }
        CreateIndexResponse indexresponse = client.admin().indices().prepareCreate(index).execute().actionGet();
        log.info("执行建立成功？{}", indexresponse.isAcknowledged());
        boolean b = addMapper(index, type);
        log.info("执行添加分词成功？{}", b);
        return indexresponse.isAcknowledged();
    }

    private static boolean addMapper(String index, String type) throws IOException {
        log.info("添加分词执行中.........");
        XContentBuilder mapping = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("properties")
                .startObject("title").field("type", "text").field("analyzer", "ik_max_word").endObject()
                .endObject()
                .endObject();

        PutMappingRequest mapping1 = Requests.putMappingRequest(index).type(type).source(mapping);
        AcknowledgedResponse acknowledgedResponse = client.admin().indices().putMapping(mapping1).actionGet();
        log.info("添加分词成功？{}", acknowledgedResponse.isAcknowledged());
        return acknowledgedResponse.isAcknowledged();
    }

    /**
     * 删除索引
     *
     * @param index
     * @return
     */
    public static boolean deleteIndex(String index) {
        if (!isIndexExist(index)) {
            log.info("Index is not exits!");
        }
        AcknowledgedResponse acknowledgedResponse = client.admin().indices().prepareDelete(index).execute().actionGet();
        if (acknowledgedResponse.isAcknowledged()) {
            log.info("delete index {} successfully!", index);
        } else {
            log.info("Fail to delete index {}", index);
        }
        return acknowledgedResponse.isAcknowledged();
    }

    /**
     * 判断索引是否存在
     *
     * @param index
     * @return
     */
    public static boolean isIndexExist(String index) {
        IndicesExistsResponse inExistsResponse = client.admin().indices().exists(new IndicesExistsRequest(index))
                .actionGet();
        if (inExistsResponse.isExists()) {
            log.info("Index [{}] is exist!", index);
        } else {
            log.info("Index [] is not exist!", index);
        }
        return inExistsResponse.isExists();
    }

    /**
     * 数据添加，正定ID
     *
     * @param jsonObject 要增加的数据
     * @param index      索引，类似数据库
     * @param type       类型，类似表
     * @param id         数据ID
     * @return
     */
    public static String addData(JSONObject jsonObject, String index, String type, String id) {
        IndexResponse response = client.prepareIndex(index, type, id).setSource(jsonObject).get();
        log.info("addData response status:{},id:{}", response.status().getStatus(), response.getId());
        return response.getId();
    }

    /**
     * 通过ID删除数据
     *
     * @param index 索引，类似数据库
     * @param type  类型，类似表
     * @param id    数据ID
     */
    public static void deleteDataById(String index, String type, String id) {
        DeleteResponse response = client.prepareDelete(index, type, id).execute().actionGet();
        log.info("deleteDataById response status:{},id:{}", response.status().getStatus(), response.getId());
    }

    /**
     * 通过ID 更新数据
     *
     * @param jsonObject 要增加的数据
     * @param index      索引，类似数据库
     * @param type       类型，类似表
     * @param id         数据ID
     * @return
     */
    public static void updateDataById(JSONObject jsonObject, String index, String type, String id) {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(index).type(type).id(id).doc(jsonObject);
        UpdateResponse updateResponse = client.update(updateRequest).actionGet();
        log.debug("更新数据状态信息: {}", updateResponse.status().getStatus());

    }

    /**
     * 通过ID获取数据
     *
     * @param index  索引，类似数据库
     * @param type   类型，类似表
     * @param id     数据ID
     * @param fields 需要显示的字段，逗号分隔（缺省为全部字段）
     * @return
     */
    public static Map<String, Object> searchDataById(String index, String type, String id, String fields) {
        GetRequestBuilder getRequestBuilder = client.prepareGet(index, type, id);
        if (StringUtils.isNotEmpty(fields)) {
            getRequestBuilder.setFetchSource(fields.split(","), null);
        }
        GetResponse getResponse = getRequestBuilder.execute().actionGet();
        return getResponse.getSource();
    }

    /**
     * 使用分词查询
     *
     * @param index     索引名称
     * @param type      类型名称,可传入多个type逗号分隔
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param size      文档大小限制
     * @param matchStr  过滤条件（k=v）
     * @return
     */
    public static List<Map<String, Object>> searchListData(String index, String type, long startTime, long endTime,
                                                           Integer size, String matchStr) {
        return searchListData(index, type, startTime, endTime, size, null, null, false, null, matchStr);
    }

    /**
     * 使用分词查询
     *
     * @param index    索引名称
     * @param type     类型名称,可传入多个type逗号分隔
     * @param size     文档大小限制
     * @param fields   需要显示的字段，逗号分隔（缺省为全部字段）
     * @param matchStr 过滤条件（k=v）
     * @return
     */
    public static List<Map<String, Object>> searchListData(String index, String type, Integer size, String fields,
                                                           String matchStr) {
        return searchListData(index, type, 0, 0, size, fields, null, false, null, matchStr);
    }

    /**
     * 使用分词查询
     *
     * @param index       索引名称
     * @param type        类型名称,可传入多个type逗号分隔
     * @param size        文档大小限制
     * @param fields      需要显示的字段，逗号分隔（缺省为全部字段）
     * @param sortField   排序字段
     * @param matchPhrase true 使用，短语精准匹配
     * @param matchStr    过滤条件（k=v）
     * @return
     */
    public static List<Map<String, Object>> searchListData(String index, String type, Integer size, String fields,
                                                           String sortField, boolean matchPhrase, String matchStr) {
        return searchListData(index, type, 0, 0, size, fields, sortField, matchPhrase, null, matchStr);
    }


    /**
     * 使用分词查询
     *
     * @param index          索引名称
     * @param type           类型名称,可传入多个type逗号分隔
     * @param size           文档大小限制
     * @param fields         需要显示的字段，逗号分隔（缺省为全部字段）
     * @param sortField      排序字段
     * @param matchPhrase    true 使用，短语精准匹配
     * @param highlightField 高亮字段
     * @param matchStr       过滤条件（k=v）
     * @return
     */
    public static List<Map<String, Object>> searchListData(String index, String type, Integer size, String fields,
                                                           String sortField, boolean matchPhrase, String
                                                                   highlightField, String matchStr) {
        return searchListData(index, type, 0, 0, size, fields, sortField, matchPhrase, highlightField, matchStr);
    }

    /**
     * 使用分词查询
     *
     * @param index          索引名称
     * @param type           类型名称,可传入多个type逗号分隔
     * @param startTime      开始时间
     * @param endTime        结束时间
     * @param size           文档大小限制
     * @param fields         需要显示的字段，逗号分隔（缺省为全部字段）
     * @param sortField      排序字段
     * @param matchPhrase    true 精准匹配
     * @param highlightField 高亮字段(高亮查询，过滤条件必传)
     * @param matchStr       过滤条件（k=v）
     * @return
     */
    public static List<Map<String, Object>> searchListData(String index, String type, long startTime, long endTime,
                                                           Integer size, String fields, String sortField, boolean
                                                                   matchPhrase, String highlightField, String
                                                                   matchStr) {
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        if (StringUtils.isNotEmpty(type)) {
            searchRequestBuilder.setTypes(type.split(","));
        }
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (startTime > 0 && endTime > 0) {
            boolQuery.must(QueryBuilders.rangeQuery("timestamp")
                    .format("epoch_millis")
                    .from(startTime)
                    .to(endTime)
                    .includeLower(true)
                    .includeUpper(true));
        }
        if (StringUtils.isNotEmpty(matchStr)) {
            searchField(matchPhrase, matchStr, boolQuery);
        }
        if (StringUtils.isNotEmpty(highlightField)) {
            highlightField(highlightField, searchRequestBuilder);
        }
        searchRequestBuilder.setQuery(boolQuery);
        if (StringUtils.isNotEmpty(fields)) {
            searchRequestBuilder.setFetchSource(fields.split(","), null);
        }
        searchRequestBuilder.setFetchSource(true);
        if (StringUtils.isNotEmpty(sortField)) {
            searchRequestBuilder.addSort(sortField, SortOrder.DESC);
        }
        if (size != null && size > 0) {
            searchRequestBuilder.setSize(size);
        }
        log.info("{}", searchRequestBuilder);
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        long totalHits = searchResponse.getHits().totalHits;
        long length = searchResponse.getHits().getHits().length;
        log.info("共查询到[{}]条数据,处理数据条数[{}]", totalHits, length);
        if (searchResponse.status().getStatus() == 200) {
            // 解析对象
            return setSearchResponse(searchResponse, highlightField);
        }
        return null;

    }


    /**
     * 使用分词查询,并分页
     *
     * @param index          索引名称
     * @param type           类型名称,可传入多个type逗号分隔
     * @param currentPage    当前页
     * @param pageSize       每页显示条数
     * @param startTime      开始时间
     * @param endTime        结束时间
     * @param fields         需要显示的字段，逗号分隔（缺省为全部字段）
     * @param sortField      排序字段
     * @param matchPhrase    true 使用，短语精准匹配
     * @param highlightField 高亮字段
     * @param matchStr       过滤条件（k=v）
     * @return
     */
    public static EsPage searchDataPage(String index, String type, int currentPage, int pageSize, long startTime,
                                        long endTime, String fields, String sortField, boolean matchPhrase, String
                                                highlightField, String matchStr) {
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        if (StringUtils.isNotEmpty(type)) {
            searchRequestBuilder.setTypes(type.split(","));
        }
        searchRequestBuilder.setSearchType(SearchType.QUERY_THEN_FETCH);
        // 需要显示的字段，逗号分隔（缺省为全部字段）
        if (StringUtils.isNotEmpty(fields)) {
            searchRequestBuilder.setFetchSource(fields.split(","), null);
        }
        //排序字段
        if (StringUtils.isNotEmpty(sortField)) {
            searchRequestBuilder.addSort(sortField, SortOrder.DESC);
        }
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (startTime > 0 && endTime > 0) {
            boolQuery.must(QueryBuilders.rangeQuery("timestamp")
                    .format("epoch_millis")
                    .from(startTime)
                    .to(endTime)
                    .includeLower(true)
                    .includeUpper(true));
        }
        if (StringUtils.isNotEmpty(matchStr)) {
            searchField(matchPhrase, matchStr, boolQuery);
        }
        if (StringUtils.isNotEmpty(highlightField)) {
            highlightField(highlightField, searchRequestBuilder);
        }
        searchRequestBuilder.setQuery(QueryBuilders.matchAllQuery());
        searchRequestBuilder.setQuery(boolQuery);
        // 分页应用
        searchRequestBuilder.setFrom(currentPage).setSize(pageSize);
        // 设置是否按查询匹配度排序
        searchRequestBuilder.setExplain(true);
        log.info("{}" + searchRequestBuilder);
        // 执行搜索,返回搜索响应信息
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        long totalHits = searchResponse.getHits().totalHits;
        long length = searchResponse.getHits().getHits().length;
        log.debug("共查询到[{}]条数据,处理数据条数[{}]", totalHits, length);
        if (searchResponse.status().getStatus() == 200) {
            // 解析对象
            List<Map<String, Object>> sourceList = setSearchResponse(searchResponse, highlightField);
            return new EsPage(currentPage, pageSize, (int) totalHits, sourceList);
        }
        return null;

    }


    /**
     * 高亮结果集 特殊处理
     *
     * @param searchResponse
     * @param highlightField
     */
    private static List<Map<String, Object>> setSearchResponse(SearchResponse searchResponse, String highlightField) {
        List<Map<String, Object>> sourceList = new ArrayList<Map<String, Object>>(16);
        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            searchHit.getSourceAsMap().put("id", searchHit.getId());
            if (StringUtils.isNotEmpty(highlightField)) {
                log.info("遍历 高亮结果集，覆盖 正常结果集{}", searchHit.getSourceAsMap());
                Text[] text = searchHit.getHighlightFields().get(highlightField).getFragments();
                if (text != null) {
                    StringBuffer stringBuffer = new StringBuffer();
                    for (Text str : text) {
                        stringBuffer.append(str.string());
                    }
                    //遍历 高亮结果集，覆盖 正常结果集
                    searchHit.getSourceAsMap().put(highlightField, stringBuffer.toString());
                }
            }
            sourceList.add(searchHit.getSourceAsMap());
        }
        return sourceList;
    }


    /**
     * 搜索字段
     *
     * @param matchPhrase
     * @param matchStr
     * @param boolQuery
     */
    private static void searchField(boolean matchPhrase, String matchStr, BoolQueryBuilder boolQuery) {
        for (String s : matchStr.split(",")) {
            String[] split = s.split("=");
            if (split.length > 1) {
                if (matchPhrase) {
                    boolQuery.must(QueryBuilders.matchPhraseQuery(split[0], split[1]));
                } else {
                    boolQuery.must(QueryBuilders.wildcardQuery(split[0], split[1]));
                }
            }
        }
    }

    /**
     * 高亮字段
     *
     * @param highlightField
     * @param searchRequestBuilder
     */
    private static void highlightField(String highlightField, SearchRequestBuilder searchRequestBuilder) {
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        //设置前缀
        highlightBuilder.preTags("<span style='color:red' >");
        //设置后缀
        highlightBuilder.postTags("</span>");
        // 设置高亮字段
        highlightBuilder.field(highlightField);
        searchRequestBuilder.highlighter(highlightBuilder);
    }


}
