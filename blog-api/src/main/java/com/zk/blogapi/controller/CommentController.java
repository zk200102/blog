package com.zk.blogapi.controller;


import com.zk.blogapi.service.CommentService;
import com.zk.blogapi.vo.param.CommentParam;
import com.zk.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zk
 * @since 2023-04-02
 */
@RestController
@RequestMapping("comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @ApiOperation("获取评论信息")
    @GetMapping("article/{id}")
    public Result comments(@PathVariable Long id){
        return commentService.commentByArticleId(id);
    }

    @ApiOperation("添加评论信息")
    @PostMapping ("create/change")
    public Result change(@RequestBody CommentParam commentParam){
        return commentService.change(commentParam);
    }
}

