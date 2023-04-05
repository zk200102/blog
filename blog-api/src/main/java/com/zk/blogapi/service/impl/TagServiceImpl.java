package com.zk.blogapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zk.blogapi.entity.Tag;
import com.zk.blogapi.mapper.TagMapper;
import com.zk.blogapi.service.TagService;
import com.zk.blogapi.vo.TagVo;
import com.zk.common.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * author zk
 * @since 2023-03-26
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public List<TagVo> findTagsByArticleId(Long articleId) {
        List<Tag> tags = baseMapper.findTagsByArticleId(articleId);
        return copyList(tags);
    }
    private List<TagVo> copyList(List<Tag> tags) {
        ArrayList<TagVo> tagVos = new ArrayList<>();
        tags.forEach(tag -> tagVos.add(copy(tag)));
        return tagVos;
    }
    private TagVo copy(Tag tag) {
        TagVo tagVo = new TagVo();
        tagVo.setId(tag.getId().toString());
        BeanUtils.copyProperties(tag,tagVo);
        return tagVo;
    }

    @Override
    public List<Tag> listHot(int limit) {
        //查询出热门标签id
        List<Long> tags = baseMapper.getHotTags(limit);
        //如果查询为空，返回空集合
        if (CollectionUtils.isEmpty(tags)){
            return Collections.emptyList();
        }
        //根据id查询出标签数据
        List<Tag> list = baseMapper.selectBatchIds(tags);
        //转换为tagvo返回
//        List<TagVo> collect = list.stream().map(tag -> {
//            TagVo tagVo = new TagVo();
//            BeanUtils.copyProperties(tag, tagVo);
//            return tagVo;
//        }).collect(Collectors.toList());

        return list;
    }

    @Override
    public Result findAll() {
        List<Tag> list = baseMapper.selectList(null);
        List<TagVo> tagVos = list.stream().map(tag -> {
            TagVo tagVo = new TagVo();
            tagVo.setId(String.valueOf(tag.getId()));
            BeanUtils.copyProperties(tag, tagVo);
            return tagVo;
        }).collect(Collectors.toList());
        return Result.success(tagVos);
    }
}
