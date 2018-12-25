package com.yunweb.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;

/**
 * @author wangyunlong
 * @date 2018/9/6 16:45
 */
@RestController
public class CountDownLatchTest {

    private static final Integer COUNT = 5;

    private static volatile Integer count = 1;

    public Integer getTotalNumber(){
        CountDownLatch countDownLatch = new CountDownLatch(COUNT);
        for (int i =1;i<=COUNT;i++){
            new Thread(() -> {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                add();
                System.out.println(count+" add 1");
                countDownLatch.countDown();
            }).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        System.out.println("finish!!!"+ count);
        return count;
    }

    private Integer add(){
        count+=count;
        return count;
    }

    public static void main(String[] args) {
        CountDownLatchTest countDownLatchTest = new CountDownLatchTest();
        countDownLatchTest.getTotalNumber();
    }
}
