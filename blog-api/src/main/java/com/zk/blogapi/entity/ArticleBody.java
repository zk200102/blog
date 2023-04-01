package com.zk.blogapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * author zk
 * @since 2023-04-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ms_article_body")
@ApiModel(value="ArticleBody对象", description="")
public class ArticleBody implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String content;

    private String contentHtml;

    private Long articleId;


}
