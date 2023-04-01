package com.zk.blogapi.dos;

import lombok.Data;

/**
 * @author zk
 * @date 2023/3/28 15:55
 * @desciption: 文章归档实体类
 */
@Data
public class Archive {
    private Integer year;
    private Integer month;
    private Long count;
}
