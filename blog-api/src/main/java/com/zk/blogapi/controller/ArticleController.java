package com.zk.blogapi.controller;


import com.zk.blogapi.annotation.LogAnnotation;
import com.zk.blogapi.dos.Archive;
import com.zk.blogapi.service.ArticleService;
import com.zk.blogapi.utils.enums.LogTypeEnum;
import com.zk.blogapi.vo.ArticleVo;
import com.zk.blogapi.vo.param.ArticleParam;
import com.zk.blogapi.vo.param.PageParams;
import com.zk.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * author zk
 * @since 2023-03-26
 */
@RestController
@RequestMapping("articles")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags = "文章管理")
public class ArticleController {
    private final ArticleService articleService;

    @ApiOperation(value = "查询文章列表",httpMethod = "POST",response = Result.class,notes = "查询文章列表")
    @PostMapping
    @LogAnnotation(model = "文章管理",logType = LogTypeEnum.QUERY,desc = "查询文章列表")
    public Result listArticle(PageParams pageParams){
        List<ArticleVo> list = articleService.listArticle(pageParams);
        return Result.success(list);
    }

    @ApiOperation(value = "查询最热文章",httpMethod = "POST",response = Result.class,notes = "查询最热文章")
    @PostMapping("hot")
    @LogAnnotation(model = "文章管理",logType = LogTypeEnum.QUERY,desc = "查询最热文章")
    public Result hotArticles(){
        List<Map<String,Object>> list = articleService.hotArticles(5);
        return Result.success(list);
    }

    @ApiOperation(value = "查询最新文章",httpMethod = "POST",response = Result.class,notes = "查询最新文章")
    @PostMapping("new")
    @LogAnnotation(model="文章管理",logType = LogTypeEnum.QUERY,desc = "查询最新文章")
    public Result newArticles(){
        List<ArticleVo> list = articleService.getNewArticles(5);
        return Result.success(list);
    }

    @ApiOperation(value = "查询文章归档",httpMethod = "POST",response = Result.class,notes = "查询文章归档")
    @PostMapping("listArchives")
    @LogAnnotation(model="文章管理",logType = LogTypeEnum.QUERY,desc = "查询文章归档")
    public Result listArchives(){
        List<Archive> list = articleService.getListArchives();
        return Result.success(list);
    }

    @ApiOperation(value = "查询文章详情",httpMethod = "POST",response = Result.class,notes = "查询文章详情")
    @PostMapping("view/{id}")
    @LogAnnotation(model="文章管理",logType = LogTypeEnum.QUERY,desc = "查询文章详情")
    public Result findArticleById(@PathVariable Long id){
        return articleService.findArticleById(id);
    }

    @ApiOperation(value = "发布文章",httpMethod = "POST",response = Result.class,notes = "发布文章")
    @PostMapping("publish")
    @LogAnnotation(model="文章管理",logType = LogTypeEnum.QUERY,desc = "发布文章")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }
}

