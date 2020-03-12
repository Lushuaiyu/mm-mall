package com.lushuaiyu.mall;
import java.time.LocalDateTime;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.crypto.digest.BCrypt;
import com.lushuaiyu.mall.entity.UmsAdmin;
import com.lushuaiyu.mall.service.RedisService;
import com.lushuaiyu.mall.test.demo.MyRunnable;
import com.lushuaiyu.mall.vo.UmsAdminVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jones on 8/3/2020
 *
 * @author lushuaiyu
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class Test1 {

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;
    private static final int QUEUE_CAPACITY = 100;
    private static final Long KEEP_ALIVE_TIME = 1L;

        @Resource
        private RedisService redis;

        @Test
        public void test11 () {
            UmsAdmin umsAdmin = new UmsAdmin();
            umsAdmin.setPassword(BCrypt.hashpw("123456", BCrypt.gensalt()));
            System.out.println(umsAdmin.getPassword());

        }

        @Test
        public void test2 () {
            UmsAdmin umsAdmin = new UmsAdmin();
            umsAdmin.setId(0L);
            umsAdmin.setUsername("fffg");
            umsAdmin.setPassword("edsf");
            umsAdmin.setIcon("asdf");
            umsAdmin.setEmail("redcvbgtre");
            umsAdmin.setNickName("fwszxc");
            UmsAdminVo umsAdminVo = new UmsAdminVo();
            BeanUtil.copyProperties(umsAdmin, umsAdminVo, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
            System.out.println(umsAdminVo);
        }

        @Test
        public void test3 () {
            redis.set("aa", "gtr", 60);
        }

        @Test
        public void test4 () {


            ThreadPoolExecutor executor = new ThreadPoolExecutor(
                    CORE_POOL_SIZE,
                    MAX_POOL_SIZE,
                    KEEP_ALIVE_TIME,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(QUEUE_CAPACITY),
                    new ThreadPoolExecutor.CallerRunsPolicy()
            );

            for (int i = 0; i < 10; i++){
                //创建 WorkerThread 对象 (WorkThread 类实现了 Runnable 接口)
                MyRunnable runnable = new MyRunnable("" + i);
                executor.execute(runnable);
            }
            executor.shutdown();

            while (!executor.isTerminated()){
            }
            System.out.println("Finished all threads");

            AtomicInteger integer = new AtomicInteger();



        }

        @Test
        public void test5(){
            UmsAdminVo umsAdmin = new UmsAdminVo();
            umsAdmin.setEmail("gfd");
            System.out.println(umsAdmin);


        }
    }

