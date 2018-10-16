package cn.luowq.spider.httpclient;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Auther: rowan
 * @Date: 2018/9/28 19:40
 * @Description:
 */
public class TestGetPicture {
    public static void main(String[] args){
        //创建httpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://www.java1234.com/uploads/allimg/170116/1-1F116101333221.JPG");
        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
        CloseableHttpResponse httpResponse = null;
        try {
            //执行http请求
            httpResponse = httpClient.execute(httpGet);
            Integer status = httpResponse.getStatusLine().getStatusCode();
            System.out.println(status);
            //获取返回实体
            HttpEntity entity = httpResponse.getEntity();
            //獲取請求返回內容類型
            String value = entity.getContentType().getValue();
            System.out.println(value);
            if (entity!=null){
                System.out.println(entity.getContentType().getValue());
                InputStream inputStream = entity.getContent();
                FileUtils.copyToFile(inputStream,new File("d://aaa.jpg"));
            }
            //获取网页内容
            String entityString = EntityUtils.toString(entity,"utf-8");//utf-8
            httpResponse.close();
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}