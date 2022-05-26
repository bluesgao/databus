package com.sjb.gyl.databus.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author bei.wu
 */
@Slf4j
@EnableAsync
@Configuration
public class ExecutorConfig {
    @Bean
    public ThreadPoolTaskExecutor handlerExecutor() {
        log.info("start handlerExecutor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        //配置最大线程数 Runtime.getRuntime().availableProcessors() * 2 + 1
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 2 + 1);
        //配置队列大小
        executor.setQueueCapacity(1000);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("handler-executor-");

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }

//    @Bean
//    public ThreadPoolTaskExecutor binlogExecutor() {
//        log.info("start handlerExecutor");
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        //配置核心线程数
//        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
//        //配置最大线程数 Runtime.getRuntime().availableProcessors() * 2 + 1
//        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 1 + 1);
//        //配置队列大小
//        executor.setQueueCapacity(1000);
//        // 设置线程活跃时间（秒）
//        executor.setKeepAliveSeconds(60);
//        //配置线程池中的线程的名称前缀
//        executor.setThreadNamePrefix("handler-executor-");
//
//        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
//        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//        //执行初始化
//        executor.initialize();
//        return executor;
//    }
}
