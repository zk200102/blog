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

import java.io.Serializable;
import java.lang.reflect.Method;
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
//        return copyList(records,true,true);
        return copyListOther(records,true,true);
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
        return copyListOther(articles, true, true);
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
        ArticleVo articleVo = copyOther(article, true, true, true, true);
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
    /**
     * author: zk
     * date: 2023/4/6
     * description: 测试泛型方法
     * param kList:
     * @param isTag:
     * @param isAuthor:
     * return: java.util.List<T>
     */
    private <T,K> List<T> copyListOther(List<K> kList,boolean isTag, boolean isAuthor){
        ArrayList<T> ts = new ArrayList<>();
        kList.forEach(k -> ts.add(copyOther(k,isTag,isAuthor,false,false)));
        return ts;
    }

    private <T, K> T copyOther(K k,boolean isTag, boolean isAuthor,boolean isBody,boolean isCategory) {
        try {
//            反射获取泛型T的实例
            Class<?> articleVo = Class.forName("com.zk.blogapi.vo.ArticleVo");
            T t = (T) articleVo.newInstance();
//            获取泛型T的setId方法
            Method setId = t.getClass().getMethod("setId", String.class);
//            执行泛型k的getId方法获取id
            String id = k.getClass().getMethod("getId").invoke(k).toString();
//            执行泛型T的setId方法设置k的id
            setId.invoke(t,id);
//            把k的其他属性赋值给t
            BeanUtils.copyProperties(k,t);
//            获取t的setCreateDate方法
            Method setCreateDate = t.getClass().getMethod("setCreateDate", String.class);
//            获取k的getCreateDate方法
            Method getCreateDate = k.getClass().getMethod("getCreateDate");
//            执行t的setCreateDate方法，设置k的getCreateDate得到的值
            setCreateDate.invoke(t,new DateTime(getCreateDate.invoke(k)).toString("yyyy-MM-dd HH:mm"));
            //添加标签作者，判断是否存在
            if (isTag){
                Method setTags = t.getClass().getMethod("setTags",List.class);
                setTags.invoke(t,tagService.findTagsByArticleId(Long.valueOf(id)));
            }
            if (isAuthor){
                UserVo userVo = new UserVo();
                Method getAuthorId = k.getClass().getMethod("getAuthorId");
                BeanUtils.copyProperties(sysUserService.getById((Serializable) getAuthorId.invoke(k)),userVo);
                Method setAuthor = t.getClass().getMethod("setAuthor", UserVo.class);
                setAuthor.invoke(t,userVo);
            }
            if (isBody){
                Method getBodyId = k.getClass().getMethod("getBodyId");
                Long bodyId = (Long) getBodyId.invoke(k);
                ArticleBody articleBody = articleBodyService.getById(bodyId);
                ArticleBodyVo articleBodyVo = new ArticleBodyVo();
                articleBodyVo.setContent(articleBody.getContent());
                Method setBody = t.getClass().getMethod("setBody", ArticleBodyVo.class);
                setBody.invoke(t,articleBodyVo);
            }
            if (isCategory){
                Method getCategoryId = k.getClass().getMethod("getCategoryId");
                Integer categoryId = (Integer) getCategoryId.invoke(k);
                Category category = categoryService.getById(categoryId);
                CategoryVo categoryVo = new CategoryVo();
                categoryVo.setId(category.getId().toString());
                categoryVo.setCategoryName(category.getCategoryName());
                categoryVo.setAvatar(category.getAvatar());
                categoryVo.setDescription(category.getDescription());
                Method setCategory = t.getClass().getMethod("setCategory", CategoryVo.class);
                setCategory.invoke(t,categoryVo);
            }
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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
