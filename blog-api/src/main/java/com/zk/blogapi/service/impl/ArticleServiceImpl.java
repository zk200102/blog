package com.zk.blogapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zk.blogapi.dos.Archive;
import com.zk.blogapi.entity.*;
import com.zk.blogapi.mapper.ArticleBodyMapper;
import com.zk.blogapi.mapper.ArticleMapper;
import com.zk.blogapi.mapper.ArticleTagMapper;
import com.zk.blogapi.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zk.blogapi.utils.UserThreadLocal;
import com.zk.blogapi.vo.*;
import com.zk.blogapi.vo.param.ArticleParam;
import com.zk.blogapi.vo.param.PageParams;
import com.zk.common.Result;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private ArticleTagMapper articleTagMapper;

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
    @Transactional
    @Override
    public Result publish(ArticleParam articleParam) {
//        获取threadlocal里的用户信息（前提是用户要登陆，即配置拦截器拦截此接口）
        SysUser account = UserThreadLocal.getAccount();
        Article article = new Article();
        article.setAuthorId(account.getId());

        article.setCommentCounts(0);
        article.setCreateDate(System.currentTimeMillis());
        article.setSummary(articleParam.getSummary());
        article.setTitle(articleParam.getTitle());
        article.setViewCounts(0);
        article.setWeight(Article.Article_Common);
        article.setCategoryId(Integer.valueOf(articleParam.getCategory().getId()));
//        先执行插入，会自动生成主键id，后续就可以拿到id
        baseMapper.insert(article);
//        标签保存到关联表
        List<TagVo> tags = articleParam.getTags();
        if (tags !=null){
            tags.forEach(tagVo -> {
                Long id = article.getId();
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(Long.valueOf(tagVo.getId()));
                articleTag.setArticleId(id);
                articleTagMapper.insert(articleTag);
            });
        }
//        文章内容存储
        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBody.setArticleId(article.getId());
        articleBodyService.save(articleBody);
//
        article.setBodyId(articleBody.getId());
        baseMapper.updateById(article);

        HashMap<String, String> map = new HashMap<>();
        map.put("id",article.getId().toString());
        return Result.success(map);
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
