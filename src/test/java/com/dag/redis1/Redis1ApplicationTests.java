package com.dag.redis1;

import com.dag.redis1.model.req.ArticleReq;
import com.dag.redis1.service.ArticleService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;


public class Redis1ApplicationTests extends TestBase{

	@Autowired
	private ArticleService articleService;

	@Autowired
	private HashOperations<String, String, Object> hashOperations;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;


	@Test
	public void test1(){
		ArticleReq articleReq = new ArticleReq();
		articleReq.setUser("anguo");
		articleReq.setTitle("6.08");
		articleReq.setLink("www.anguo.com");
		articleService.post_article(articleReq);
	}
}
