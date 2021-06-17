package com.dag.redis.blog.model.req;

import lombok.Data;

/**
 * @author: donganguo
 * @date: 2021/6/8 2:03 下午
 * @Description:
 */
@Data
public class ArticleReq {

    String user;

    String title;

    String link;

}
