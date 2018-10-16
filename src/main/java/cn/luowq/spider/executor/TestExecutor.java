package cn.luowq.spider.executor;

import java.util.concurrent.*;

/**
 * @Auther: rowan
 * @Date: 2018/9/29 14:23
 * @Description:
 */
public class TestExecutor {

    private static volatile Integer count = 1;

    //执行标识
    private static Boolean exeFlag = true;

    public static void main(String[] args) {
        //创建ExecutorService 连接池大小默认10个
        //TODO ThreadPoolExecutor
        ExecutorService executorService = Executors.newFixedThreadPool(10);
            while(exeFlag){
                executorService.execute(new Runnable() {
                    synchronized public void run() {
                        if(count<=100){
                            System.out.println("计数："+count);
                            count++;
                        }else{
                            exeFlag=false;
                        }
                    }
                });
            }
                //没有活动线程
                if (((ThreadPoolExecutor)executorService).getActiveCount()==0){
                    executorService.shutdown();

                    System.out.println("结束:"+count);
                }

//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

    }

}