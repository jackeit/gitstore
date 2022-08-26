import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;
import java.util.Random;

public class BookPageProcessor5 implements PageProcessor {
    public static int ChapterNum = 1;
    Random ran = new Random();

    private Site site = Site.me()
            .setCharset("GBK")//编码
            .setSleepTime(ran.nextInt(2)*100+100)//抓取间隔时间
            .setTimeOut(1000*10)//超时时间
            .setRetrySleepTime(5000)//重试时间
            .setRetryTimes(8)//重试次数
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.62 Safari/537.36");
    @Override
    public void process(Page page) {
        page.putField("content",page.getHtml().xpath("//div[@class=readerCon]/html()")
                .replace("<p style[\\S\\s]*?</p>","")
                .replace("<div style[\\S\\s]*?</div>","")
                .replace("<p [\\S\\s]*</p>","")
                .replace("<script>read_app(\"弃坑提督揭棺而起\");</script>","")
                .replace("&nbsp;"," ")
                .replace("<br>","\r\n")
                .all()
        );
        //
        page.putField("title",page.getHtml().xpath("//h2/text()")
                .replace(".*分卷阅读.*","").all());
        List<Selectable> list1 = page.getHtml().xpath("//div[@class=readPage]/a").nodes();
        //
        list1.remove(1);

        for(Selectable i: list1){
            if(i.xpath("//a/span/text()").toString().equals("下一章")){
                page.addTargetRequest(i.xpath("//a/@href").toString());
                break;
            }
        }
        System.out.println(ChapterNum);



    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new BookPageProcessor5())
                .addUrl("https://www.wanben.org/24880/16270849.html")
                .addPipeline(new ConsolePipeline())
                .addPipeline(new BookMypipeline("F:/javatest/book5.txt",2))
                .run();
    }
}
