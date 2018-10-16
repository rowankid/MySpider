package cn.luowq.spider.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @Auther: rowan
 * @Date: 2018/9/28 19:50
 * @Description:
 */
public class TestProxyIP {

    public static void main(String[] args){
        //创建httpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://central.maven.org/maven2/com");
//        HttpHost proxy = new HttpHost("");
        RequestConfig config = RequestConfig.custom()
                //设置连接超时时间
                .setConnectTimeout(10*1000)
                //设置读取超时时间
                .setSocketTimeout(10*1000)
//                .setProxy(proxy)
                .build() ;
        httpGet.setConfig(config);
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
            httpResponse.close();
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}