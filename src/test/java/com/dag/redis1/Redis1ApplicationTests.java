package com.dag.redis1;

import com.dag.redis1.entity.Chapter1;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.Serializable;
import java.util.Map;

@SpringBootTest
class Redis1ApplicationTests {
	@Autowired
	private Chapter1 chapter1;
	@Autowired
	private HashOperations<String, String, Object> hashOperations;
	@Test
	void contextLoads() {
	}

	@Test
	public void test(){
		String articleId = chapter1.post_article("username", "A title", "http://www.google.com");
		chapter1.article_vote("xiaolu", "article:8");
		System.out.println("We posted a new article with id: " + articleId);
		System.out.println("Its HASH looks like:");
		Map<String, Object> articleData = hashOperations.entries("article:" + articleId);
		for (Map.Entry<String,Object> entry : articleData.entrySet()){
			System.out.println("  " + entry.getKey() + ": " + entry.getValue());
		}
	}
}
