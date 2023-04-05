package com.zk.blogapi.service.impl;

import com.zk.blogapi.entity.Category;
import com.zk.blogapi.mapper.CategoryMapper;
import com.zk.blogapi.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zk.blogapi.vo.CategoryVo;
import com.zk.common.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Override
    public Result findAll() {
        List<Category> categories = baseMapper.selectList(null);
        List<CategoryVo> categoryVos = categories.stream().map(category -> {
            CategoryVo categoryVo = new CategoryVo();
//            由于category的id是long型，而categoryVo的id是string类型，直接使用BeanUtils复制将无法复制类型不同的值，所以提前赋值id给categoryVo
            categoryVo.setId(String.valueOf(category.getId()));
            BeanUtils.copyProperties(category, categoryVo);
            return categoryVo;
        }).collect(Collectors.toList());
        return Result.success(categoryVos);
    }
}
