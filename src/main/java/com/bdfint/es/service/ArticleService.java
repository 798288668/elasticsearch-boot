package com.bdfint.es.service;

import com.bdfint.es.bean.Article;
import com.bdfint.es.common.Result;
import com.bdfint.es.dao.ArticleRepository;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ArticleService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleService.class);

    private final ArticleRepository articleRepository;
    private final ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, ElasticsearchTemplate elasticsearchTemplate) {
        this.articleRepository = articleRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    public Result initData() {
        List<Article> articleList = new ArrayList<>();
        articleList.add(new Article("1", "我们", "我还清晰地记得那天我们坐在江边聊天的情境", "关键词", 0L, new Date()));
        articleList.add(new Article("2", "我们", "我记得我们聊天的情境", "关键词", 0L, new Date()));
        articleList.add(new Article("3", "我们", "我们聊天的情境", "关键词", 0L, new Date()));
        articleRepository.saveAll(articleList);
        return Result.of("init success!");
    }

    public Result save(Article article) {
        articleRepository.save(article);
        return Result.of("save success!");
    }

    public Map<String, Object> search(Integer pageNo, Integer pageSize, String searchContent) {

        //方案一：
        SearchRequestBuilder builder = elasticsearchTemplate.getClient().prepareSearch("article_index");
        builder.setTypes("article");
        builder.setFrom(pageNo);
        builder.setSize(pageSize);
        //设置查询类型：1.SearchType.DFS_QUERY_THEN_FETCH 精确查询； 2.SearchType.SCAN 扫描查询,无序
        builder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        //设置查询关键词
        if (StringUtils.isNotEmpty(searchContent)) {
            builder.setQuery(QueryBuilders.multiMatchQuery(searchContent, "title", "content"));
        }
        //设置是否按查询匹配度排序
        builder.setExplain(true);
        //设置高亮显示
        HighlightBuilder highlightBuilder = new HighlightBuilder().field("*").requireFieldMatch(false);
        highlightBuilder.preTags("<span style=\"color:red\">");
        highlightBuilder.postTags("</span>");
        builder.highlighter(highlightBuilder);

        //执行搜索,返回搜索响应信息
        SearchResponse searchResponse = builder.get();
        SearchHits searchHits = searchResponse.getHits();

        //总命中数
        long total = searchHits.getTotalHits();
        Map<String, Object> map = new HashMap<>();
        SearchHit[] hits = searchHits.getHits();
        map.put("count", total);
        List<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : hits) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            //title高亮
            HighlightField titleField = highlightFields.get("title");
            Map<String, Object> source = hit.getSource();
            if (titleField != null) {
                Text[] fragments = titleField.fragments();
                StringBuilder title = new StringBuilder();
                for (Text text : fragments) {
                    title.append(text);
                }
                source.put("title", title.toString());
            }

            //content高亮
            HighlightField contentField = highlightFields.get("content");
            if (contentField != null) {
                Text[] fragments = contentField.fragments();
                StringBuilder content = new StringBuilder();
                for (Text text : fragments) {
                    content.append(text);
                }
                source.put("content", content.toString());
            }
            list.add(source);
        }
        map.put("dataList", list);

//        // 分页参数
//        Pageable pageable = new PageRequest(pageNo, pageSize);
//
//        // 短语匹配到的搜索词，求和模式累加权重分
//        // 权重分查询 https://www.elastic.co/guide/cn/elasticsearch/guide/current/function-score-query.html
//        //   - 短语匹配 https://www.elastic.co/guide/cn/elasticsearch/guide/current/phrase-matching.html
//        //   - 字段对应权重分设置，可以优化成 enum
//        //   - 由于无相关性的分值默认为 1 ，设置权重分最小值为 10
//        // Function Score Query
//
//        QueryBuilder queryBuilde = QueryBuilders.multiMatchQuery(searchContent, "name", "content");
//        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(queryBuilde);
//        // 创建搜索 DSL 查询
//        SearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withPageable(pageable)
//                .withQuery(functionScoreQueryBuilder)
//                .build();
//
//        logger.info("\n searchCity(): searchContent [" + searchContent + "] \n DSL  = \n " + searchQuery.getQuery().toString());
//
//        Page<Article> result = articleRepository.search(searchQuery);


        return map;
    }


}
