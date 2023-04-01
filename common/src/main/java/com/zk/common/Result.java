package com.zk.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author zk
 * @date 2023/3/26 15:44
 * @desciption: 返回结果类
 */
@Data
@AllArgsConstructor
public class Result {
    @ApiModelProperty(value = "是否成功")
    private Boolean success;

    @ApiModelProperty(value = "返回码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private Object data;

    public static Result success(Object data){
        return new Result(true,200,"success", data);
    }
    public static Result fail(int code,String message){
        return new Result(false,code,message,null);
    }

}
