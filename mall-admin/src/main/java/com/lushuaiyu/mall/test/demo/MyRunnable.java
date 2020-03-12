package com.lushuaiyu.mall.test.demo;

import springfox.documentation.service.ApiListing;

import java.time.LocalDateTime;

/**
 * Created by jones on 10/3/2020
 *
 * @author lushuaiyu
 */
public class MyRunnable implements Runnable {

    private String command;


    public MyRunnable(String command) {
        this.command = command;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " Start.Time = " + LocalDateTime.now());
        processCommand();
        System.out.println(Thread.currentThread().getName() + " End.Time = " + LocalDateTime.now());

    }

    private void processCommand() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "MyRunnable{" +
                "command='" + command + '\'' +
                '}';
    }
}
