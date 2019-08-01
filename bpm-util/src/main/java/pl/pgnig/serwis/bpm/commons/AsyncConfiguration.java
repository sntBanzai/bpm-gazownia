/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.bpm.commons;

import java.util.concurrent.Executor;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 *
 * @author jerzy.malyszko
 */
@Configuration
@EnableAsync
@ComponentScan(useDefaultFilters = false, value = "pl.pgnig.serwis.bpm", includeFilters = {
    @ComponentScan.Filter(Aspect.class)})
public class AsyncConfiguration implements AsyncConfigurer {

    public static final String EXECUTOR_ID = "fs-db-sync";

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setBeanName(EXECUTOR_ID);
        executor.setMaxPoolSize(42);
        executor.setQueueCapacity(11);
        executor.setThreadNamePrefix(EXECUTOR_ID + '-');
        executor.initialize();
        return executor;
    }

}
