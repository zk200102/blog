package com.zk.blogapi.service;

import com.zk.blogapi.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zk.blogapi.vo.param.CommentParam;
import com.zk.common.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zk
 * @since 2023-04-02
 */
public interface CommentService extends IService<Comment> {

    /**
     * author: zk
     * date: 2023/4/2
     * description: 获取评论信息
     * param id: 文章id
     * return: com.zk.common.Result
     */
    Result commentByArticleId(Long id);
    /**
     * author: zk
     * date: 2023/4/4
     * description: 保存评论信息
     * param commentParam:
     * return: com.zk.common.Result
     */
    Result change(CommentParam commentParam);
}
