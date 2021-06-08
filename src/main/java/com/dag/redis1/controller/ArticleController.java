package com.dag.redis1.controller;

import com.dag.redis1.model.req.ArticleReq;
import com.dag.redis1.service.ArticleService;
import com.dag.redis1.util.AjaxResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author: donganguo
 * @date: 2021/6/8 2:08 下午
 * @Description:
 */
@RestController
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @PostMapping("/article")
    public AjaxResult post_article(ArticleReq articleReq) {

        articleService.post_article(articleReq);

        return new AjaxResult("发布成功", true);
    }

    @GetMapping("/article")
    public List<Map<String, Object>> getArticles(int page, String order) {
        return articleService.getArticles(page, order);
    }

}
