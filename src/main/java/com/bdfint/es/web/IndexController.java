package com.bdfint.es.web;

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
    public String index(ModelMap map) {
        map.addAttribute("host", "http://blog.didispace.com");
        return "index";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(@Valid BaseParam baseParam, Model model) throws Exception {
        Map<String, Object> search = articleService.search(baseParam.getPageNo(), baseParam.getPageSize(),
                baseParam.getSearchContent());
        model.addAttribute("result", search);
        return "index";
    }
}
