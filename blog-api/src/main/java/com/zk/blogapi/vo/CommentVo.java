package com.zk.blogapi.vo;

import lombok.Data;

import java.util.List;

/**
 * author: zk
 * date: 2023/4/2
 * description:
 */
@Data
public class CommentVo {
    private Long id;
    private UserVo userVo;
    private String content;
    private List<CommentVo> childrens;
    private String createDate;
    private Integer level;
    private UserVo toUser;
    private UserVo author;
}
