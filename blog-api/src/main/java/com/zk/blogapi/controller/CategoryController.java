package com.zk.blogapi.controller;


import com.zk.blogapi.service.CategoryService;
import com.zk.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * author zk
 * @since 2023-04-01
 */
@RestController
@RequestMapping("categorys")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @ApiOperation("查询所有分类")
    @GetMapping
    public Result categorys(){
        return categoryService.findAll();
    }
}

