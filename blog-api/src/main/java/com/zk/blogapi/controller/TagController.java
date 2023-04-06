package com.zk.blogapi.controller;


import com.zk.blogapi.annotation.LogAnnotation;
import com.zk.blogapi.entity.Tag;
import com.zk.blogapi.service.TagService;
import com.zk.blogapi.utils.enums.LogTypeEnum;
import com.zk.common.Result;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * author zk
 * @since 2023-03-26
 */
@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TagController {

    private final TagService tagService;

    @ApiOperation("获取最热标签")
    @GetMapping("hot")
    @LogAnnotation(model = "标签管理",logType = LogTypeEnum.QUERY,desc="获取最热标签")
    public Result hot(){
        List<Tag> list = tagService.listHot(3);
        return Result.success(list);
    }
    @ApiOperation("获取所有标签")
    @GetMapping
    @LogAnnotation(model = "标签管理",logType = LogTypeEnum.QUERY,desc="获取所有标签")
    public Result findAll(){
        return tagService.findAll();
    }

}

