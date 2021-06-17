package com.dag.redis.blog.util;

import lombok.Data;

/**
 * @author: donganguo
 * @date: 2021/6/8 2:11 下午
 * @Description:
 */
@Data
public class AjaxResult {
    private String msg;
    private boolean success = false;

    public AjaxResult(String msg) {
        this.msg = msg;
    }

    public AjaxResult(String msg, boolean success) {
        this.msg = msg;
        this.success = success;
    }
}
