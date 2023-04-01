package com.zk.blogapi.vo.param;

import lombok.Data;

/**
 * author zk
 * date 2023/3/26 16:01
 * description: 分页参数实体类
 */
@Data
public class PageParams {
    private int page = 1;
    private int pageSize = 10;
}
