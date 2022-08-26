import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import java.util.Random;
/*
* 适用于网站https://www.xyushu5.com/的小说爬虫*/
public class BookPageProcessor2 implements PageProcessor {
    public static int ChapterNum = 1;
    Random ran = new Random();

    private Site site = Site.me()
            .setCharset("GBK")//编码
            .setSleepTime(ran.nextInt(5)*100+1000)//抓取间隔时间
            .setTimeOut(1000*10)//超时时间
            .setRetrySleepTime(5000)//重试时间
            .setRetryTimes(8)//重试次数
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.62 Safari/537.36");
    @Override
    public void process(Page page) {
        page.putField("content",page.getHtml().xpath("//div[@id=main]/div/div/p/html()")
                .replace("&nbsp;"," ")
                .replace("<br>","\r\n").all());
        page.putField("title",page.getHtml().xpath("//h1/text()").all());
        String nexturl = page.getHtml().
                xpath("div[@class=papgbutton]/a[@class=nextpage]/@href").toString();
        page.addTargetRequest(nexturl);

    }

    @Override
    public Site getSite() {
        //若不return site，则会报错
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new BookPageProcessor2())
                .addUrl("https://www.xyushu5.com/read/35753/17092029/")
                .addPipeline(new ConsolePipeline())
                .addPipeline(new BookMypipeline("F:/javatest/book2.txt",2))
                .run();
    }
}
