package com.dag.redis1.service;

import com.dag.redis1.model.req.ArticleReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: dag
 * @Description:
 * @Date: 2021/1/1 15:51
 */
@Service
public class ArticleService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SetOperations<String, Object> setOperations;
    @Autowired
    private ZSetOperations<String, Object> zSetOperations;
    @Autowired
    private HashOperations<String, String, Object> hashOperations;


    private static final int ONE_WEEK_IN_SECOND = 7 * 24 * 3600;
    private static final int VOTE_SOCRE = 432;
    private static final int ARTICLES_PER_PAGE = 25;

    public String post_article(ArticleReq articleReq) {
        String article_id = Objects.requireNonNull(stringRedisTemplate.opsForValue().increment("article:")).toString();
        String voted = "voted:" + article_id;
        setOperations.add("voted:", articleReq.getUser());
        redisTemplate.expire(voted, ONE_WEEK_IN_SECOND, TimeUnit.SECONDS);

        long now = System.currentTimeMillis() / 1000;
        String article = "article:" + article_id;
        HashMap<String, String> articleData  = new HashMap<>();
        articleData.put("title", articleReq.getTitle());
        articleData.put("link", articleReq.getLink());
        articleData.put("user", articleReq.getUser());
        articleData.put("now", String.valueOf(now));
        articleData.put("votes", "1");
        hashOperations.putAll(article, articleData);

        zSetOperations.add("score:", article, now + VOTE_SOCRE);
        zSetOperations.add("time:", article, now);

        return article_id;
    }

    public void article_vote(String user, String article) {
        long cutoff = System.currentTimeMillis() / 1000 - ONE_WEEK_IN_SECOND;
        if (zSetOperations.score("time:", article) < cutoff) {
            return;
        }
        String article_id = article.substring(article.indexOf(":"+1));
        if (setOperations.add(("voted:"+article_id), user) > 0) {
            zSetOperations.incrementScore("score:", article, VOTE_SOCRE);
            hashOperations.increment(article, "votes", 1);
        }
    }

    public List<Map<String, Object>> getArticles(int page, String order) {
        int start = (page - 1) * ARTICLES_PER_PAGE;
        int end = start + ARTICLES_PER_PAGE - 1;

        Set<Object> ids = zSetOperations.range(order, start, end);
        ArrayList<Map<String, Object>> articles = new ArrayList<>();
        for (Object id : ids) {
            Map<String, Object> articleData = hashOperations.entries((String) id);
            articleData.put("id", id);
            articles.add(articleData);
        }
        return articles;
    }

    public void add_remove_groups(String article_id, String[] to_add, String[] to_remove) {
        String article = "article:" + article_id;
        for (String group_id : to_add) {
            setOperations.add("group:"+group_id, article);
        }
        for (String group_id : to_remove) {
            setOperations.remove("group:"+group_id, article);
        }
    }

    public List<Map<String,Object>> getGroupArticles(String group_name, int page, String order) {
        String key = order + group_name;
        String group = "group:" + group_name;
        ArrayList<String> list = new ArrayList<String>();
        list.add(group);
        if (!redisTemplate.hasKey(key)) {
            zSetOperations.intersectAndStore(order, list, key, RedisZSetCommands.Aggregate.MAX);
            redisTemplate.expire(key, 60, TimeUnit.SECONDS);
        }
        return getArticles(page, key);
    }
}
