package com.yunweb.controller;

import com.github.javaparser.utils.Log;
import com.yun.domain.User;
import com.yun.service.RedisService;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

/**
 * @author wangyunlong
 * @date 2018/11/30 11:04
 */
@RestController
public class TimerController {

    @Resource
    private MyDynamicTask myDynamicTask;

    @Scheduled(cron = "10 12 13 * * *")
    public void cancelTask() {
        myDynamicTask.cancelTriggerTask("3");
        myDynamicTask.cancelTriggerTask("5");
    }

    @Resource
    private RedisService redisService;

    private Timer timer = new Timer();
    private ScheduledFuture<?> future;
    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());

    @Scheduled(cron = "0 21 11 * * *")
    public void setTimer() {
        Log.info("开始创建定时任务 ...");
        List<User> users = redisService.findAll();
        users.forEach(user -> createSchedule(user));
    }

    @Scheduled(cron = "31 21 11 * * *")
    public void deleteTimer() {
        Log.info("撤销定时任务 ...");
        //timer.cancel();
        future.cancel(false);
    }



    private void createSchedule(User user) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " Timer打印出: " + user.toString());
            }
        }, user.getStart());

        future = executorService.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName() + " ScheduledExecutor打印出: " + user.toString());
                } catch (Exception e) {
                    Log.info(e.getMessage());
                }
            }
        }, user.getStart().getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);

    }

    private void deleteSchedule(User user) {

    }
}
