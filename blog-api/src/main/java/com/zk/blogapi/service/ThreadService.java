package com.zk.blogapi.service;

import com.zk.blogapi.entity.Article;
import com.zk.blogapi.mapper.ArticleMapper;

/**
 * @author zk
 * @date 2023/4/1
 * @description: 线程服务接口
 */

public interface ThreadService {
    /**
     * @author: zk
     * @date: 2023/4/1
     * @description: 子线程完成对阅读数的更改
     * @param baseMapper:
     * @param article:文章
     * @return: void
     */
    void updateArticleViewCount(ArticleMapper baseMapper, Article article);
}
