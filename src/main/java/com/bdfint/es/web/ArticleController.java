package com.bdfint.es.web;

import com.bdfint.es.bean.Article;
import com.bdfint.es.common.BaseParam;
import com.bdfint.es.common.Result;
import com.bdfint.es.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public Result init() throws Exception {
        return articleService.initData();
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result save(@Valid Article article) throws Exception {
        return articleService.save(article);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Result search(@Valid BaseParam baseParam) throws Exception {
        List<Article> articleList = articleService.search(baseParam.getPageNo(), baseParam.getPageSize(),
                baseParam.getSearchContent());
        return Result.of(articleList);
    }
}
