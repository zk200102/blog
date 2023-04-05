package com.zk.blogapi.service;

import com.zk.blogapi.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zk.common.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * author zk
 * @since 2023-04-01
 */
public interface CategoryService extends IService<Category> {
    /**
     * author: zk
     * date: 2023/4/5
     * description: 查询所有分类
     * return: com.zk.common.Result
     */
    Result findAll();
}
