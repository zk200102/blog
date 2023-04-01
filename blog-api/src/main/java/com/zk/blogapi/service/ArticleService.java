package com.zk.blogapi.service;

import com.zk.blogapi.dos.Archive;
import com.zk.blogapi.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zk.blogapi.vo.ArticleVo;
import com.zk.blogapi.vo.param.PageParams;
import com.zk.common.Result;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * author zk
 * @since 2023-03-26
 */
public interface ArticleService extends IService<Article> {
    /**
     * author: zk
     * date: 2023/4/1
     * description: TODO
     * param pageParams: 
     * return: java.util.List<com.zk.blogapi.vo.ArticleVo>
     */
    List<ArticleVo> listArticle(PageParams pageParams);
    /**
     * author: zk
     * date: 2023/3/30
     * description: 查询最热文章
     * param i: 查询条数
     * return: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    List<Map<String, Object>> hotArticles(int i);
    /**
     * author: zk
     * date: 2023/3/30
     * description: 查询最新文章
     * param i: 查询条数
     * return: java.util.List<com.zk.blogapi.vo.ArticleVo>
     */
    List<ArticleVo> getNewArticles(int i);
    /**
     * author: zk
     * date: 2023/3/30
     * description: 获取年份时间的文章数据
     * return: java.util.List<com.zk.blogapi.dos.Archive>
     */
    List<Archive> getListArchives();
    /**
     * author: zk
     * date: 2023/4/1
     * description: 查询文章详情
     * param id: 文章id
     * return: com.zk.common.Result
     */
    Result findArticleById(Long id);
}
