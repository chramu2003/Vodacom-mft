package za.co.vodacom.vodacommft.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author ncubeh on 2020/08/03
 * @package za.co.vodacom.vodacomMFT.config
 */

@Configuration
@EnableAsync
public class AsynchronousConfig {

    @Autowired
    private PropertiesFileSysConfig propertiesFileSysConfig;

    @Bean(value = "DelThreadTaskExecutor")
    public Executor deliveryThreadTaskExecutor(){
        ThreadPoolTaskExecutor del_tPoolTaskExecutor = new ThreadPoolTaskExecutor();
        del_tPoolTaskExecutor.setCorePoolSize(Integer.MAX_VALUE);
        del_tPoolTaskExecutor.setMaxPoolSize(Integer.MAX_VALUE);
        del_tPoolTaskExecutor.setQueueCapacity(Integer.MAX_VALUE);

        del_tPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); /*When maximum pool and queue is full The thread invokes itself on rejected pool.
        You will not lose the thread. This policy like increasing queue capacity.*/
        del_tPoolTaskExecutor.setThreadNamePrefix("File_Thread_");
        del_tPoolTaskExecutor.initialize();
        return del_tPoolTaskExecutor;
    }
    /*@Bean(value = "CollThreadTaskExecutor")
    public Executor collectionThreadTaskExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(propertiesFileSysConfig.getColCorePoolSize());
        threadPoolTaskExecutor.setMaxPoolSize(propertiesFileSysConfig.getColMaxPoolSize());
        threadPoolTaskExecutor.setQueueCapacity(propertiesFileSysConfig.getColqueueCapacity());
        threadPoolTaskExecutor.setThreadNamePrefix("File_Thread_");
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }*/
    @Bean(value = "TaskExecutor")
    public TaskExecutor taskExecutor(){
        SimpleAsyncTaskExecutor asyncTaskExecutor=new SimpleAsyncTaskExecutor("File_Thread_");
        asyncTaskExecutor.setConcurrencyLimit(propertiesFileSysConfig.getConcurrencyLimit());
        return asyncTaskExecutor;
    }

    @Bean(name = "ConcurrentTaskExecutor")
    public TaskExecutor taskExecutor2 () {
        return new ConcurrentTaskExecutor(
                Executors.newFixedThreadPool(propertiesFileSysConfig.getConcurrentNumberOfThreadPool()));
    }

    @Bean(value = "DelScheduleThreadTaskExecutor")
    public Executor deliveryScheduleThreadTaskExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(50);
        threadPoolTaskExecutor.setMaxPoolSize(300);
        threadPoolTaskExecutor.setQueueCapacity(300);

        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); /*When maximum pool and queue is full The thread invokes itself on rejected pool.
        You will not lose the thread. This policy like increasing queue capacity.*/
        threadPoolTaskExecutor.setThreadNamePrefix("Sch_le_Thread_");
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}
