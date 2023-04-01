package com.zk.blogapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zk.blogapi.entity.Article;
import com.zk.blogapi.mapper.ArticleMapper;
import com.zk.blogapi.service.ThreadService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author zk
 * @date 2023/4/1
 * @description:
 */
@Service
public class ThreadServiceImpl implements ThreadService {
    @Override
    @Async
    public void updateArticleViewCount(ArticleMapper baseMapper, Article article) {
        int viewCounts = article.getViewCounts();
        Article articleUpdate = new Article();
        articleUpdate.setViewCounts(viewCounts+1);
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getId,article.getId()).eq(Article::getViewCounts,viewCounts);
        baseMapper.update(articleUpdate,wrapper);
    }
}
