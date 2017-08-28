package com.bdfint.es.service;

import com.alibaba.fastjson.JSON;
import com.bdfint.es.bean.Article;
import com.bdfint.es.dao.ArticleRepository;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public void initData() {
        List<Article> articleList = new ArrayList<>();
        articleList.add(new Article("1", "那天", "我还清晰地记得那天我们坐在江边聊天的情境", "关键词",0L, new Date()));
        articleList.add(new Article("2", "那天", "我记得我们聊天的情境", "关键词",0L, new Date()));
        articleList.add(new Article("2", "那天", "我们聊天的情境", "关键词",0L, new Date()));
        articleRepository.saveAll(articleList);
    }

    public String search(String param) {
        QueryBuilder queryBuilde = QueryBuilders.multiMatchQuery(param, "name", "content");
        Iterable<Article> search = articleRepository.search(queryBuilde);
        return JSON.toJSONString(search);
    }
}
