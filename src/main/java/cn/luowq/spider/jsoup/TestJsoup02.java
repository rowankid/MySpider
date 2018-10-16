package cn.luowq.spider.jsoup;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * @Auther: rowan
 * @Date: 2018/9/29 10:33
 * @Description:
 */
public class TestJsoup02 {


    public static void main(String[] args){
        //创建httpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://xiangyilanwan0335.fang.com/house/1317758951/housedetail.htm");
        CloseableHttpResponse httpResponse = null;
        String content = null;
        try {
            //执行http请求
            httpResponse = httpClient.execute(httpGet);
            //获取返回实体
            HttpEntity entity = httpResponse.getEntity();
            //获取网页内容
            content = EntityUtils.toString(entity,"utf-8");//utf-8
//            System.out.println(content);
            httpResponse.close();
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document document = Jsoup.parse(content);
        //选择器
        Elements postItems = document.getElementsByClass("ts_linear");
        for (Element e:postItems
             ) {
//            System.out.println(e.html());
        }
        //通过选择器查找所有博客dom
        //id选择器 #id
        Elements elements = document.select(".post_item .post_item_body h3 a");
        for (Element e:postItems
        ) {
            System.out.println(e.text());
            //获取dom元素属性值
            System.out.println(e.attr("href"));
            System.out.println(e.attr("terget"));
        }
        //带有href属性的a标签
        document.select("a[href]");
        //dom元素   e.toString

        //img标签  src 属性  png结尾
        document.select("img[src$=.png]");




    }
}