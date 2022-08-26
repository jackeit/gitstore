import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;
import java.util.Random;
/*
* 适用于http://www.huanxiangji.com的网络小说爬虫
* */
public class BookPageProcessor4 implements PageProcessor {
    public static int ChapterNum = 1;
    Random ran = new Random();

    private Site site = Site.me()
            .setCharset("GBK")//编码
            .setSleepTime(ran.nextInt(3)*100+200)//抓取间隔时间
            .setTimeOut(1000*10)//超时时间
            .setRetrySleepTime(5000)//重试时间
            .setRetryTimes(8)//重试次数
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.62 Safari/537.36");
    @Override
    public void process(Page page) {
        page.putField("content",page.getHtml().xpath("//div[@id=content]/html()")
                .replace("&nbsp;"," ")
                .replace("<br>","").all());
//        \r\n
        page.putField("title",page.getHtml().xpath("//h1[@class=title]/text()")
                .replace(".*分卷阅读.*","").all());

        String baseurl = "http://www.huanxiangji.com/book/1471/";

        String extendurl = "";
        List<Selectable> list1 = page.getHtml().xpath("//div[@class='section-opt m-bottom-opt']/a").nodes();
        for(Selectable i:list1){
            if(i.xpath("//a/text()").toString().equals("下一章")){
                extendurl = i.xpath("//a/@href").toString();
                break;
            }
        }
        page.addTargetRequest(baseurl+extendurl);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new BookPageProcessor4())
                .addUrl("http://www.huanxiangji.com/book/1471/571794.html")
                .addPipeline(new ConsolePipeline())
                .addPipeline(new BookMypipeline("F:/javatest/book4.txt",25))
                .run();
        System.out.println("end");
    }
}
