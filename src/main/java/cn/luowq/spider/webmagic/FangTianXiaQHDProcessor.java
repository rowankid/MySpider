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
public class FangTianXiaQHDProcessor implements PageProcessor {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(30).setSleepTime(1000);

    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        //翻页清单
        List<String> targetPageRequestList = new ArrayList<String>();
        // 部分二：定义如何抽取页面信息，并保存下来
        List<Selectable> nodes1 = page.getHtml().xpath("//li[@class='fr']").nodes();
        for (Selectable liNode:nodes1
             ) {
            List<Selectable> targetList = liNode.xpath("//a[@href]/@href").nodes();
            for (Selectable node: targetList
            ) {
                String url = node.toString();
                if(url!=null&&!url.contains("http")&&url.length()>3&&!url.contains("javascript")){
                    String targetPageUrl = "http://"+this.getSite().getDomain()+url;
                    targetPageRequestList.add(targetPageUrl);
                }
            }
        }
        page.addTargetRequests(targetPageRequestList);

        List<String> targetDetailRequestList = new ArrayList<String>();
        //每页的楼盘进入选项
            List<Selectable> nodes = page.getHtml().xpath("//div[@class='house_value clearfix']/div/a").nodes();
            for (Selectable node: nodes
                 ) {
                String target = node.links().toString();
                if(target!=null&&!target.contains("dianping")){
                    targetDetailRequestList.add(target);
//                    System.out.println("target: " + target);
                }
            }

        Selectable xpath = page.getHtml().xpath("//a[@id='xfptxq_B03_08']/@href");
        Selectable xpath2 = page.getHtml().xpath("//a[@id='xfdsxq_B03_08']/@href");
            //xfdsxq_B03_08
            //xfdsxq_B03_08
        List<String> detailUrl = new ArrayList<String>();
        if (xpath!=null||xpath2!=null){
//            System.out.println("详细页地址："+xpath);
//            System.out.println("详细页地址："+xpath2);
            detailUrl.add(xpath.toString());
            detailUrl.add(xpath2.toString());
            page.addTargetRequests(detailUrl);
        }
        if (page.getUrl().toString().endsWith("housedetail.htm")){
            //楼盘名称
            System.out.println("进入详细页："+ page.getUrl().toString());
            page.putField("\"name\"", "\""+page.getHtml().xpath("//a[@class='ts_linear']/text()").toString()+"\"");
            page.putField("\"price\"", "\""+page.getHtml().xpath("//em/text()").toString()+"\"");
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
                    page.putField("\""+li.xpath("//div[@class='list-left']/text()").toString()+"\"","\""+value+"\"");
                }
            }
            String replace = page.getResultItems().toString().replace("ResultItems{fields=", "");
            logger.error(replace.substring(0,replace.indexOf("request=Request")-1).replace("=",":"));
        }

        // 部分三：从页面发现后续的url地址来抓取
        page.addTargetRequests(targetDetailRequestList);
    }

    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        Spider.create(new FangTianXiaQHDProcessor())
                //从"https://github.com/code4craft"开始抓
                .addUrl("http://qhd.newhouse.fang.com/house/s/")
//                .addUrl("http://xiangyilanwan0335.fang.com/")
                .addPipeline(new JsonFilePipeline("D:\\webmagic\\"))
                //开启5个线程抓取
                .thread(10)
                //启动爬虫
                .run();
    }
}