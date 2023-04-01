package com.zk.blogapi.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author zk
 * @since 2023-03-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ms_article")
@ApiModel(value="Article对象", description="")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "评论数量")
    private Integer commentCounts;

    @ApiModelProperty(value = "创建时间")
    private Long createDate;

    @ApiModelProperty(value = "简介")
    private String summary;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "浏览数量")
    private Integer viewCounts;

    @ApiModelProperty(value = "是否置顶")
    private Integer weight;

    @ApiModelProperty(value = "作者id")
    private Long authorId;

    @ApiModelProperty(value = "内容id")
    private Long bodyId;

    @ApiModelProperty(value = "类别id")
    private Integer categoryId;


}
