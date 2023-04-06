package com.zk.blogapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zk.blogapi.mapper.SysUserMapper;
import com.zk.blogapi.service.SysUserService;
import com.zk.blogapi.utils.enums.AdminEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.zk.blogapi.utils.enums.AdminEnum.ADMIN_TRUE;

@SpringBootTest
class BlogApiApplicationTests {

    @Test
    void contextLoads() throws JsonProcessingException {
        AdminEnum adminTrue = ADMIN_TRUE;
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(adminTrue);
        System.out.println(s);
    }
}