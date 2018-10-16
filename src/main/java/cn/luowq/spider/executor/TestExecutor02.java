package cn.luowq.spider.executor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @Auther: rowan
 * @Date: 2018/9/29 14:23
 * @Description:
 */
public class TestExecutor02 {

    private static volatile Integer count = 1;

    //执行标识
    private static volatile Boolean exeFlag = true;

    public static void main(String[] args) {
        //创建ExecutorService 连接池大小默认10个
        //TODO ThreadPoolExecutor
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("thread-call-runner-%d").build();
        int size = 10;
        ExecutorService executorService =
                new ThreadPoolExecutor(size, size,
                        0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>(), namedThreadFactory);
        synchronized (TestExecutor02.class){
        while (exeFlag) {
            executorService.execute(new Runnable() {
                public void run() {
                    add();
                    System.out.println(Thread.currentThread().getName() + ",计数：" + count);
                }
            });
            if (((ThreadPoolExecutor) executorService).getActiveCount() == 0) {
                executorService.shutdown();
                System.out.println("结束1:" + count);
            }
        }
        }
        System.out.println("结束:" + count);
    }

    private static synchronized void add() {
        if (count <= 100) {
            count++;
        } else {
            exeFlag = false;
        }
    }

}