package cn.luowq.spider.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @Auther: rowan
 * @Date: 2018/9/28 18:53
 * @Description:
 */
public class TestHelloWorld {
    //未添加浏览器模拟
    public static void main(String[] args){
        //创建httpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://www.jb51.net");
        CloseableHttpResponse httpResponse = null;
        try {
            //执行http请求
            httpResponse = httpClient.execute(httpGet);
            //获取返回实体
            HttpEntity entity = httpResponse.getEntity();
            //获取网页内容
            String entityString = EntityUtils.toString(entity,"gb2312");//utf-8
            System.out.println(entityString);
            httpResponse.close();
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}