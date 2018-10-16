package cn.luowq.spider.webmagic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: rowan
 * @Date: 2018/10/12 11:22
 * @Description:
 */
public class QHDXiaoShouXuKeProcessor implements PageProcessor {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(30).setSleepTime(1000);

    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        //翻页清单
        List<String> targetPageRequestList = new ArrayList<String>();
        // 部分二：定义如何抽取页面信息，并保存下来
        List<Selectable> nodes1 = page.getHtml().xpath("//div[@class='pages']").nodes();
//        System.out.println(nodes1);
        for (Selectable liNode:nodes1
             ) {
            List<Selectable> targetList = liNode.xpath("//a[@href]/@href").nodes();
//            System.out.println(targetList);
            for (Selectable node: targetList
            ) {
                String url = node.toString();
                    targetPageRequestList.add(url);
            }
        }
        page.addTargetRequests(targetPageRequestList);

        List<String> targetDetailRequestList = new ArrayList<String>();
        //每页的楼盘进入选项
            List<Selectable> nodes = page.getHtml().xpath("//ul[@class='global_tx_list4']/li/a").nodes();
            for (Selectable node: nodes
                 ) {
                String target = node.links().toString();
                if(target!=null&&!target.contains("dianping")){
                    targetDetailRequestList.add(target);
                    System.out.println("target: " + target);
                }
            }
        page.addTargetRequests(targetDetailRequestList);

        if (page.getUrl().toString().contains("itemid")){
            //楼盘名称
            System.out.println("进入详细页："+ page.getUrl().toString());
            if (page.getHtml().xpath("//h1/text()").toString().contains("秦皇岛市新建商品房预售资金监管工作政策提示")){
                List<Selectable> spans = page.getHtml().xpath("//div[@id='article_body']/p/span").nodes();
//                System.out.println(spans);
                StringBuilder text = new StringBuilder();
                for (Selectable span: spans
                     ) {
//                    System.out.println(span);
                    text.append(span.xpath("tidyText()"));
                }
                if (text.length() > 10) {
                    page.putField("URL",page.getUrl());
                    page.putField("text", text);
                }
                logger.error(page.getResultItems().toString());
            }

        }

        // 部分三：从页面发现后续的url地址来抓取
    }

    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        Spider.create(new QHDXiaoShouXuKeProcessor())
                //从"https://github.com/code4craft"开始抓
                .addUrl("http://www.qhdfgj.gov.cn/qhdfgj/?action-category-catid-41")
//                .addUrl("http://www.qhdfgj.gov.cn/qhdfgj/?action-viewnews-itemid-8103")
//                .addUrl("http://xiangyilanwan0335.fang.com/")
                .addPipeline(new JsonFilePipeline("D:\\webmagic\\2018101601\\"))
                //开启5个线程抓取
                .thread(10)
                //启动爬虫
                .run();
    }
}