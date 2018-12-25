package com.yunweb.controller;

import com.yun.domain.User;
import com.yun.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.SchedulingException;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author wangyunlong
 * @date 2018/12/4 19:59
 */
@Component
public class MyDynamicTask implements SchedulingConfigurer {
    private static Logger log = LoggerFactory.getLogger(MyDynamicTask.class);

    @Resource
    private RedisService redisService;

    private ScheduledTaskRegistrar taskRegistrar;
    private Map<String, ScheduledFuture<?>> taskFutures = new ConcurrentHashMap<>();
    private int taskSchedulerCorePoolSize = 10;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler());
        this.taskRegistrar = taskRegistrar;
        try {
            // 等待任务调度初始化完成
            while (!this.inited()) {
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("任务调度初始化完成，添加任务");
        List<User> users = redisService.findAll();
        users.forEach(user -> {
            this.addTriggerTask(user);
        });
    }

    private ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(taskSchedulerCorePoolSize);
        /**需要实例化线程*/
        threadPoolTaskScheduler.initialize();
        return threadPoolTaskScheduler;
    }


    private Runnable doTask(User user) {
        return new Runnable() {
            @Override
            public void run() {
                // 业务逻辑
                log.info(Thread.currentThread().getName()+"执行了MyDynamicTask:" + user.toString());
            }
        };
    }

    private Trigger getTrigger(User user) {
        return new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                // 触发器
                CronTrigger trigger = new CronTrigger(getCron(user.getStart()));
                return trigger.nextExecutionTime(triggerContext);
            }
        };
    }

    public String getCron(Date time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ss mm HH dd MM ?");
        String cron = "";
        if (Objects.nonNull(time)) {
            cron = dateFormat.format(time);
        }
        return cron;
    }

    /**
     * 添加任务
     *
     * @param user
     */
    public void addTriggerTask(User user)
    {
        String taskId = user.getId().toString();
        if (taskFutures.containsKey(taskId)) {
            throw new SchedulingException("定时任务 taskId[" + taskId + "] 已添加 ...");
        }
        TaskScheduler scheduler = taskRegistrar.getScheduler();
        ScheduledFuture<?> future = scheduler.schedule(doTask(user), getTrigger(user));
        taskFutures.put(taskId, future);
    }

    /**
     * 取消任务
     *
     * @param taskId
     */
    public void cancelTriggerTask(String taskId)
    {
        ScheduledFuture<?> future = taskFutures.get(taskId);
        if (future != null)
        {
            future.cancel(true);
        }
        taskFutures.remove(taskId);
    }

    /**
     * 重置任务
     *
     * @param taskId
     * @param triggerTask
     */
    /*public void resetTriggerTask(String taskId, TriggerTask triggerTask)
    {
        cancelTriggerTask(taskId);
        addTriggerTask(taskId, triggerTask);
    }*/

    /**
     * 任务编号
     *
     * @return
     */
    public Set<String> taskIds()
    {
        return taskFutures.keySet();
    }

    /**
     * 是否存在任务
     *
     * @param taskId
     * @return
     */
    public boolean hasTask(String taskId)
    {
        return this.taskFutures.containsKey(taskId);
    }

    /**
     * 任务调度是否已经初始化完成
     *
     * @return
     */
    private boolean inited()
    {
        return this.taskRegistrar != null && this.taskRegistrar.getScheduler() != null;
    }

}

