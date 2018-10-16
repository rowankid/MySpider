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
 * @Date: 2018/9/29 10:13
 * @Description:
 */
public class TestJsoup {

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
            System.out.println(content);
            httpResponse.close();
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document document = Jsoup.parse(content);
        Elements titles = document.getElementsByTag("title");
        Element title = titles.first();//titles.get(0);
        String titleText = title.text();//title.html();
        document.getElementById("ic_title");
        //根据样式
        Elements elementsByClass = document.getElementsByClass("");

        for (Element e: elementsByClass ) {
            System.out.println(e.html());
        }
        //根据属性名
        document.getElementsByAttribute("");
        //根据属性名和属性值
        document.getElementsByAttributeValue("","");


    }
}