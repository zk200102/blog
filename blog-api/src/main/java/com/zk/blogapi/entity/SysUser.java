package com.zk.blogapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zk.blogapi.utils.enums.AdminEnum;
import com.zk.blogapi.utils.enums.DeleteEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * author zk
 * @since 2023-03-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ms_sys_user")
@ApiModel(value="SysUser对象", description="")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "是否管理员")
    private AdminEnum admin;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "注册时间")
    private Long createDate;

    @ApiModelProperty(value = "是否删除")
    private DeleteEnum deleted;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "最后登录时间")
    private Long lastLogin;

    @ApiModelProperty(value = "手机号")
    private String mobilePhoneNumber;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "加密盐")
    private String salt;

    @ApiModelProperty(value = "状态")
    private String status;


}
