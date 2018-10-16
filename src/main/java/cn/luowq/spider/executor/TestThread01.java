package cn.luowq.spider.executor;

/**
 * @Auther: rowan
 * @Date: 2018/9/29 16:09
 * @Description:
 */
public class TestThread01 {
    static Thread thread1 = new Thread(new Runnable() {
        public void run() {
            System.out.println("thread1");
        }
    });
    static Thread thread2 = new Thread(new Runnable() {
        public void run() {
            System.out.println("thread2");
        }
    });
    static Thread thread3 = new Thread(new Runnable() {
        public void run() {
            System.out.println("thread3");
        }
    });

    public static void main(String[] args) throws InterruptedException {
        //通过join方法，保证线程的顺序性
        //让主线程等待子线程执行完成之后，再继续向下执行
        thread1.start();
        thread1.join();
        thread2.start();
        thread2.join();
        thread3.start();
        thread3.join();
    }
}