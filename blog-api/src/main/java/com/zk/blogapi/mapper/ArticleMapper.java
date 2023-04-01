package com.zk.blogapi.mapper;

import com.zk.blogapi.dos.Archive;
import com.zk.blogapi.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * author zk
 * @since 2023-03-26
 */
public interface ArticleMapper extends BaseMapper<Article> {

    List<Archive> getListArchives();
}
