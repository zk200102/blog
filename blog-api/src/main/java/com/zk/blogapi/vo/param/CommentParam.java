package com.zk.blogapi.vo.param;

import lombok.Data;

/**
 * author: zk
 * date: 2023/4/4
 * description: 评论参数实体类
 */
@Data
public class CommentParam {
    private Long articleId;
    private String content;
    private Long parent;
    private Long toUserid;
}
