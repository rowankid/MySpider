package cn.luowq.spider.webmagic;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: rowan
 * @Date: 2018/10/12 11:22
 * @Description:
 */
public class XiangyilanwanPageProcessor implements PageProcessor {

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        // 部分二：定义如何抽取页面信息，并保存下来
        //楼盘名称
        page.putField("name", page.getHtml().xpath("//a[@class='ts_linear']/text()").toString());
        page.putField("price", page.getHtml().xpath("//em/text()").toString());
        List<Selectable> clearfix = page.getHtml().xpath("//ul[@class='list clearfix']").nodes();
        clearfix.addAll(page.getHtml().xpath("//ul[@class='clearfix list']").nodes());
        for (Selectable node:clearfix) {
            List<Selectable> liList = node.xpath("//li").nodes();

            Map detail = new HashMap();

            for (Selectable li:liList) {
                String value = null;
                value=li.xpath("//div[@class='list-right']/text()").toString();
                if (value==null||"".equals(value.replaceAll(" ",""))){
                    value = li.xpath("//div[@class='list-right-text']/text()").toString();
                    if (value==null||"".equals(value.replaceAll(" ",""))){
                        value = li.xpath("//div[@class='list-right-text']/a/text()").toString();
                    }
                }
                page.putField(li.xpath("//div[@class='list-left']/text()").toString(),value);
            }
        }


        // 部分三：从页面发现后续的url地址来抓取
//        page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/[\\w\\-]+/[\\w\\-]+)").all());
    }

    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        Spider.create(new XiangyilanwanPageProcessor())
                //从"https://github.com/code4craft"开始抓
                .addUrl("http://xiangyilanwan0335.fang.com/house/1317758951/housedetail.htm")
                //开启5个线程抓取
                .thread(1)
                //启动爬虫
                .run();
    }
}