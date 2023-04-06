package com.zk.blogapi.aspect;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zk.blogapi.annotation.LogAnnotation;
import com.zk.blogapi.entity.SysLog;
import com.zk.blogapi.entity.SysUser;
import com.zk.blogapi.service.SysLogService;
import com.zk.blogapi.utils.IpUtil;
import com.zk.blogapi.utils.UserThreadLocal;
import com.zk.common.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * author: zk
 * date: 2023/4/6
 * description: 日志切面
 */
@Aspect
@Configuration
public class LogAspect {
    /**
     * 获取注解的日志
     */
    private static final Logger logger = LoggerFactory.getLogger(LogAnnotation.class);
    @Autowired
    private SysLogService sysLogService;
    /**
     * author: zk
     * date: 2023/4/6
     * description: 定义切入点
     * return: void
     */
    @Pointcut("@annotation(com.zk.blogapi.annotation.LogAnnotation)")
    public void logPointcut() {}
    /**
     * author: zk
     * date: 2023/4/6
     * description: 环绕通知
     * param joinPoint:
     * @param joinPoint:
     * return: java.lang.Object
     */
    @Around("logPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {

        //        记录开始时间
        long start = System.currentTimeMillis();
        //先执行业务
        Object result = joinPoint.proceed();
//        执行时长
        long timeMillis = System.currentTimeMillis()-start;
        try {
            // 日志处理
            handle(joinPoint,(Result) result,timeMillis);

        } catch (Exception e) {
            logger.error("日志记录出错!", e);
        }

        return result;
    }
    /**
     * author: zk
     * date: 2023/4/6
     * description: 日志处理
     * param joinPoint:
     *
     * @param result     :
     *                   return: void
     * @param timeMillis
     */
    @Async  //此处耗时操作使用线程池开启子线程进行操作
    void handle(ProceedingJoinPoint joinPoint, Result result, long timeMillis) {
        String params = null;
        String nickName = null;
        Long userId = null;
        
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);

        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature)joinPoint.getSignature();
            //获取切入点所在的方法
            Method method = signature.getMethod();
            //获取日志注解
            LogAnnotation annotation = method.getAnnotation(LogAnnotation.class);
            logger.info("======================log start==========================");
            String model = annotation.model();
            logger.info("module:{}", model);
            String desc = annotation.desc();
            logger.info("description:{}", desc);
            ObjectMapper objectMapper = new ObjectMapper();
            String logType = objectMapper.writeValueAsString(annotation.logType());
            logger.info("logType:{}", logType);
            boolean save = annotation.save();
            logger.info("save:{}", save);

            //获取日志注解所在的类名
            String className = joinPoint.getTarget().getClass().getName();
            //获取日志注解所在的方法名
            String name = signature.getName();
            logger.info("request method:{}",className+"."+name+"()");

            //获取日志注解标注所在方法的参数
            Object[] args = joinPoint.getArgs();
            if (args.length!=0){
                params = JSON.toJSONString(args[0]);
                logger.info("params:{}",params);
            }
            //执行时间
            logger.info("execute time:{}ms",timeMillis);
            // 当前请求的ip地址
            String ipAddr = IpUtil.getIpAddr(request);
            logger.info("ip:{}",ipAddr);
            logger.info("======================log end==========================");
            if (!save)return;
            SysUser account = UserThreadLocal.getAccount();
            if (account!=null){
                 nickName = account.getNickname();
                 userId = account.getId();
            }
            //    添加到数据库表
            SysLog sysLog = SysLog.builder().ip(ipAddr).module(model).time(timeMillis).createDate(System.currentTimeMillis())
                    .operation(annotation.logType()).method(name).params(params).userid(userId)
                    .nickname(nickName).description(annotation.desc())
                    .build();
            sysLogService.save(sysLog);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
