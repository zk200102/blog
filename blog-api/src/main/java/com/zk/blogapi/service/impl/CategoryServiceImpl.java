package com.zk.blogapi.service.impl;

import com.zk.blogapi.entity.Category;
import com.zk.blogapi.mapper.CategoryMapper;
import com.zk.blogapi.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * author zk
 * @since 2023-04-01
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

}
