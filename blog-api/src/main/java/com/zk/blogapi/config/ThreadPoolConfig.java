package com.zk.blogapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * author zk
 * date 2023/4/1
 * description: 线程池配置
 */

@Configuration
@EnableAsync
public class ThreadPoolConfig {
    @Value("${mythreadpool.maxPoolSize}")
    private Integer maxPoolSize;

    @Value("${mythreadpool.corePoolSize}")
    private Integer corePoolSize;

    @Value("${mythreadpool.queueCapacity}")
    private Integer queueCapacity;

    @Value("${mythreadpool.keepAliveSeconds}")
    private Integer keepAliveSeconds;


    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        /*
corePoolSize: 核心线程数，当向线程池提交一个任务时池里的线程数小于核心线程数，那么它会创建一个线程来执行这个任务，一直直到池内的线程数等于核心线程数。
maxPoolSize: 最大线程数，线程池中允许的最大线程数量。关于这两个数量的区别，核心线程数会一直存在，而最大线程数只有在缓冲队列满了之后才会创建新的线程。
queueCapacity: 缓冲队列大小，用来保存阻塞的任务队列（注意这里的队列放的是任务而不是线程）。
         */
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix("taskExecutor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
