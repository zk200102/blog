package com.zk.blogapi.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import lombok.Data;

import java.util.List;

/**
 * author: zk
 * date: 2023/4/2
 * description:
 */
@Data
public class CommentVo {
    //手动添加Id序列化，防止前端接受long型数据出现精度损失
//    @JsonSerialize(using = StringSerializer.class)    //默认jdk的序列化注解
    @JSONField(serializeUsing = StringSerializer.class)   //fastjson的序列化注解
    private Long id;
    private UserVo userVo;
    private String content;
    private List<CommentVo> childrens;
    private String createDate;
    private Integer level;
    private UserVo toUser;
    private UserVo author;
}
