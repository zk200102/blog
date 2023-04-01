package com.zk.blogapi.service;

import com.zk.blogapi.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zk.blogapi.vo.TagVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zk
 * @since 2023-03-26
 */
public interface TagService extends IService<Tag> {
    /**
     * @author: zk
     * @date: 2023/3/30
     * @description: 根据文章id查找标签
     * @param articleId: 文章id
     * @return: java.util.List<com.zk.blogapi.vo.TagVo>
     */
    List<TagVo> findTagsByArticleId(Long articleId);
    /**
     * @author: zk
     * @date: 2023/3/30
     * @description: 获取最热标签
     * @param limit:
     * @return: java.util.List<com.zk.blogapi.entity.Tag>
     */
    List<Tag> listHot(int limit);
}
