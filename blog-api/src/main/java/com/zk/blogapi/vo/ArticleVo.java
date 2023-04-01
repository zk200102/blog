package com.zk.blogapi.vo;

import lombok.Data;

import java.util.List;

/**
 * @author zk
 * @date 2023/3/26 16:20
 * @desciption: 文章实体类
 */
@Data
public class ArticleVo {
    private String id;

    private String title;

    private String summary;

    private Integer commentCounts;

    private Integer viewCounts;

    private Integer weight;
    /**
     * 创建时间
     */
    private String createDate;

    private UserVo author;

    private ArticleBodyVo body;

    private List<TagVo> tags;

    private CategoryVo category;
}
