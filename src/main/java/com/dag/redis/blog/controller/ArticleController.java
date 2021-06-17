package com.dag.redis.blog.controller;

import com.dag.redis.blog.model.req.ArticleReq;
import com.dag.redis.blog.service.ArticleService;
import com.dag.redis.blog.util.AjaxResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
