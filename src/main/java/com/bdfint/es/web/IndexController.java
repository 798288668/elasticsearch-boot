package com.bdfint.es.web;

import com.bdfint.es.bean.Article;
import com.bdfint.es.common.BaseParam;
import com.bdfint.es.common.Result;
import com.bdfint.es.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
