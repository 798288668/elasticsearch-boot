package com.bdfint.es.web;

import com.bdfint.es.bean.Article;
import com.bdfint.es.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public String init() throws Exception {
        articleService.initData();
        return "init success!";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Article article) throws Exception {
        articleService.save(article);
        return "success!";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(Integer pageNo, Integer pageSize, String searchContent) throws Exception {
        return articleService.search(pageNo, pageSize, searchContent);
    }
}
