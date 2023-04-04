package com.zk.blogapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zk.blogapi.entity.Comment;
import com.zk.blogapi.entity.SysUser;
import com.zk.blogapi.mapper.CommentMapper;
import com.zk.blogapi.service.CommentService;
import com.zk.blogapi.service.SysUserService;
import com.zk.blogapi.utils.UserThreadLocal;
import com.zk.blogapi.vo.CommentVo;
import com.zk.blogapi.vo.UserVo;
import com.zk.blogapi.vo.param.CommentParam;
import com.zk.common.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zk
 * @since 2023-04-02
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    private SysUserService sysUserService;
    @Transactional
    @Override
    public Result change(CommentParam commentParam) {
        Long articleId = commentParam.getArticleId();
        String content = commentParam.getContent();
        Long parent = commentParam.getParent();
        Long toUserid = commentParam.getToUserid();

        SysUser account = UserThreadLocal.getAccount();
        Comment comment = new Comment();
        comment.setAuthorId(account.getId());
        comment.setContent(content);
        comment.setArticleId(Math.toIntExact(articleId));
        comment.setCreateDate(System.currentTimeMillis());
//        校验参数
        if (parent==null||parent==0){
            comment.setLevel(String.valueOf(1));
        }else {
            comment.setLevel(String.valueOf(2));
        }
        comment.setParentId(parent==null?0:parent);
        comment.setToUid(toUserid==null?0:toUserid);
        baseMapper.insert(comment);
        return Result.success();
    }

    @Override
    public Result commentByArticleId(Long id) {
//        根据文章id查询，level为1的评论
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getArticleId,id).eq(Comment::getLevel,1);
        List<Comment> comments = baseMapper.selectList(wrapper);
//        把一级评论数据转换为vo数据
        List<CommentVo> commentVos = copyList(comments);
        return Result.success(commentVos);
    }
//  List<Comment> 转换List<CommentVo>
    private List<CommentVo> copyList(List<Comment> comments) {
        ArrayList<CommentVo> commentVos = new ArrayList<>();
        comments.forEach(comment -> commentVos.add(copy(comment)));
        return commentVos;
    }
    /**
     * author: zk
     * date: 2023/4/2
     * description: 把comment转换为commentVo
     * param comment:
     * return: com.zk.blogapi.vo.CommentVo
     */
    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);
//        作者信息
        Long authorId = comment.getAuthorId();
        SysUser user = sysUserService.getById(authorId);
        UserVo userVo = new UserVo();
        userVo.setNickname(user.getNickname());
        userVo.setAvatar(user.getAvatar());
        userVo.setId(user.getId().toString());
        commentVo.setAuthor(userVo);
//        子评论
        if (comment.getLevel().equals("1") ){
//            根据id，查询此id下的评论，即id为子评论的ParentId
            Long id = comment.getId();
            LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Comment::getParentId,id);
            List<Comment> comments = baseMapper.selectList(wrapper);
            List<CommentVo> commentVos = copyList(comments);
            commentVo.setChildrens(commentVos);
        }
        Long toUid = comment.getToUid();
//        判断评论是否有回复对象
        if (toUid!=0){
            SysUser user1 = sysUserService.getById(toUid);
            UserVo userVo1 = new UserVo();
            userVo1.setNickname(user1.getNickname());
            userVo1.setAvatar(user1.getAvatar());
            userVo1.setId(user1.getId().toString());
            commentVo.setToUser(userVo1);
        }
        return commentVo;
    }
}
