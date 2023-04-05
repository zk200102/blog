package com.zk.blogapi.vo.param;

import com.zk.blogapi.vo.ArticleBodyVo;
import com.zk.blogapi.vo.CategoryVo;
import com.zk.blogapi.vo.TagVo;
import lombok.Data;

import java.util.List;

/**
 * author: zk
 * date: 2023/4/5
 * description:
 */
@Data
public class ArticleParam {
    private Long id;
    private ArticleBodyParam body;
    private CategoryVo category;
    private String summary;
    private List<TagVo> tags;
    private String title;

}
