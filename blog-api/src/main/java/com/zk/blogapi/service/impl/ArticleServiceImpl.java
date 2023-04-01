package com.zk.blogapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zk.blogapi.dos.Archive;
import com.zk.blogapi.entity.Article;
import com.zk.blogapi.entity.ArticleBody;
import com.zk.blogapi.entity.Category;
import com.zk.blogapi.mapper.ArticleMapper;
import com.zk.blogapi.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zk.blogapi.vo.ArticleBodyVo;
import com.zk.blogapi.vo.ArticleVo;
import com.zk.blogapi.vo.CategoryVo;
import com.zk.blogapi.vo.UserVo;
import com.zk.blogapi.vo.param.PageParams;
import com.zk.common.Result;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * author zk
 * @since 2023-03-26
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private TagService tagService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ArticleBodyService articleBodyService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ThreadService threadService;
    @Override
    public List<ArticleVo> listArticle(PageParams pageParams) {
        LambdaQueryWrapper<Article> qw = new LambdaQueryWrapper<>();
        qw.orderByDesc(Article::getWeight,Article::getCreateDate);
        Page<Article> articlePage = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        baseMapper.selectPage(articlePage,qw);
        List<Article> records = articlePage.getRecords();
        return copyList(records,true,true);
    }

    @Override
    public List<Map<String, Object>> hotArticles(int i) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Article::getId,Article::getTitle).orderByDesc(Article::getViewCounts).last("limit "+i);
        List<Article> articles = baseMapper.selectList(wrapper);

        ArrayList<Map<String, Object>> list = new ArrayList<>();
        articles.forEach(article -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id",article.getId());
            map.put("title",article.getTitle());
            list.add(map);
        });
        return list;
    }

    @Override
    public List<ArticleVo> getNewArticles(int i) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Article::getId,Article::getTitle,Article::getAuthorId).orderByDesc(Article::getCreateDate).last("limit "+i);
        List<Article> articles = baseMapper.selectList(wrapper);
        return copyList(articles, true, true);
    }

    @Override
    public List<Archive> getListArchives() {
        return baseMapper.getListArchives();
    }

    @Override
    public Result findArticleById(Long id) {
        Article article = baseMapper.selectById(id);
        ArticleVo articleVo = copy(article, true, true, true, true);
//        更新阅读数，开启子线程去执行，为了防止增加阅读数业务异常导致当前查询文章内容业务异常
        threadService.updateArticleViewCount(baseMapper,article);
        return Result.success(articleVo);
    }
    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        ArrayList<ArticleVo> list = new ArrayList<>();
        records.forEach(article -> list.add(copy(article,isTag,isAuthor,false,false)));
        return list;
    }
    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor,boolean isBody,boolean isCategory) {
        ArrayList<ArticleVo> list = new ArrayList<>();
        records.forEach(article -> list.add(copy(article,isTag,isAuthor,isBody,isCategory)));
        return list;
    }
    private ArticleVo copy(Article article,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory){
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(article.getId().toString());
        BeanUtils.copyProperties(article,articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //添加标签作者，判断是否存在
        if (isTag){
            articleVo.setTags(tagService.findTagsByArticleId(article.getId()));
        }
        if (isAuthor){
            UserVo userVo = new UserVo();
            Long authorId = article.getAuthorId();
            BeanUtils.copyProperties(sysUserService.getById(authorId),userVo);
            articleVo.setAuthor(userVo);
        }
        if (isBody){
            Long bodyId = article.getBodyId();
            ArticleBody articleBody = articleBodyService.getById(bodyId);
            ArticleBodyVo articleBodyVo = new ArticleBodyVo();
            articleBodyVo.setContent(articleBody.getContent());
            articleVo.setBody(articleBodyVo);
        }
        if (isCategory){
            Integer categoryId = article.getCategoryId();
            Category category = categoryService.getById(categoryId);
            CategoryVo categoryVo = new CategoryVo();
            categoryVo.setId(category.getId().toString());
            categoryVo.setCategoryName(category.getCategoryName());
            categoryVo.setAvatar(category.getAvatar());
            categoryVo.setDescription(category.getDescription());
            articleVo.setCategory(categoryVo);
        }
        return articleVo;
    }
}
