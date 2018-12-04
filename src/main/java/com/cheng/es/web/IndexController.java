package com.cheng.es.web;

import com.cheng.es.bean.Article;
import com.cheng.es.common.BaseParam;
import com.cheng.es.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

/**
 * @author fengcheng
 * @version 2017/8/30
 */
@Controller
public class IndexController {

    private final ArticleService articleService;

    @Autowired
    public IndexController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(@Valid BaseParam baseParam, Model model) throws Exception {
        List<Article> articleList = articleService.search(baseParam.getPageNo(), baseParam.getPageSize(),
                baseParam.getSearchContent());
        model.addAttribute("searchContent", baseParam.getSearchContent());
        model.addAttribute("result", articleList);
        return "index";
    }
}
