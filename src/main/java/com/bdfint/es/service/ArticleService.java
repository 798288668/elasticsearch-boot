package com.bdfint.es.service;

import com.alibaba.fastjson.JSON;
import com.bdfint.es.bean.Article;
import com.bdfint.es.dao.ArticleRepository;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ArticleService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleService.class);

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public void initData() {
        List<Article> articleList = new ArrayList<>();
        articleList.add(new Article("1", "我们", "我还清晰地记得那天我们坐在江边聊天的情境", "关键词", 0L, new Date()));
        articleList.add(new Article("2", "我们", "我记得我们聊天的情境", "关键词", 0L, new Date()));
        articleList.add(new Article("2", "我们", "我们聊天的情境", "关键词", 0L, new Date()));
        articleRepository.saveAll(articleList);
    }

    public void save(Article article) {
        articleRepository.save(article);
    }

    public String search(Integer pageNo, Integer pageSize, String searchContent) {
        QueryBuilder queryBuilde = QueryBuilders.multiMatchQuery(searchContent, "name", "content");


        Iterable<Article> result = articleRepository.search(queryBuilde);

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


        return JSON.toJSONString(result);
    }


}
