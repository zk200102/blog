package com.zk.blogapi.mapper;

import com.zk.blogapi.entity.Tag;
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
public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 根据文章id查询标签数据(要查关联表和标签表，mybatis无法处理多表查询，所以需要手动编写sql)
     * param articleId
     * return
     */
    List<Tag> findTagsByArticleId(Long articleId);

    /**
     * 查询最热标签
     * return
     */
    List<Long> getHotTags(int limit);
}
