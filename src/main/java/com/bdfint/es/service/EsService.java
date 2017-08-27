package com.bdfint.es.service;

import com.bdfint.es.bean.SearchReturn;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author fengcheng
 * @version 2017/8/26
 */
@Service
public class EsService {

    @Autowired
    private TestDao dao;

    String index = "laohu";
    String type = "test";

    //将数据库数据初始化一键导入的方法
    public List<Map> init() {
        Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "elasticsearch").build();
        TransportClient client = new TransportClient(settings);
        client.addTransportAddress(new InetSocketTransportAddress("192.168.35.225", 9300));

        List<Map> list = dao.list();
        for (Map item : list) {
            IndexResponse response = client.prepareIndex(index, type, item.get("id").toString()).setSource(item).execute().actionGet();
        }
        return list;
    }


    //根据关键字索引
    public SearchReturn scSearch(String keyWord) {
        //以下代码可以抽取出来
        Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "elasticsearch").build();
        TransportClient client = new TransportClient(settings);
        client.addTransportAddress(new InetSocketTransportAddress("192.168.35.225", 9300));


        ArrayList<Map> list = new ArrayList<Map>();
        SearchReturn searchReturn = new SearchReturn();

        SearchResponse response = client
                //指定索引库
                .prepareSearch(index)
                //指定类型
                .setTypes(type)
                //指定查询类型
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                //指定要查询的关键字
                .setQuery(QueryBuilders.matchQuery("name", keyWord))
                .addHighlightedField("name")
                .setHighlighterPreTags("<font color='red'>")
                .setHighlighterPostTags("</font>")
                //相当于实现分页
                .setFrom(0)
                .setSize(10)
                //执行查询
                .execute().actionGet();
        //可以获取查询的所有内容
        SearchHits hits = response.getHits();
        //获取查询的数据总数
        long totalHits = hits.getTotalHits();
        searchReturn.setTotal(totalHits);

        SearchHit[] hits2 = hits.getHits();

        ArrayList<String> searchText = new ArrayList<String>();
        ArrayList<String> highLighting = new ArrayList<String>();

        for (SearchHit searchHit : hits2) {
            //获取所有高亮内容
            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
            //根据设置的高亮字段获取高亮内容
            HighlightField highlightField = highlightFields.get("name");
            Text[] fragments = highlightField.getFragments();

            for (Text text : fragments) {
                highLighting.add(text.toString());
            }
            searchText.add(searchHit.getSourceAsString());
        }
        searchReturn.setSearchText(searchText);
        searchReturn.setHighLighting(highLighting);

        return searchReturn;
    }

}
