import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;
import java.util.Random;

public class HonghuayuanBookPageProcessor implements PageProcessor {
    public static String starturl = "https://houhuayuan.vip/%e7%83%ad%e5%85%b5%e5%99%a8%e7%9a%84%e5%85%bb%e6%88%90-%e7%ac%ac%e4%b8%80%e8%87%b3%e5%8d%81%e7%ab%a0";
    public static int ChapterNum = 1;
    public static boolean hasread = false;
    Random ran = new Random();

    private Site site = Site.me()
            .setCharset("UTF-8")//编码
            .setSleepTime(ran.nextInt(5)*100+200)//抓取间隔时间
            .setTimeOut(1000*10)//超时时间
            .setRetrySleepTime(6000)//重试时间
            .setRetryTimes(20)//重试次数
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.62 Safari/537.36");
    @Override
    public Site getSite() {
        return site;
    }
    @Override
    public void process(Page page) {
        page.putField("title",page.getHtml().xpath("//h1/text()"));
        page.putField("content",page.getHtml().xpath("//div[@class=entry-content]")
                .replace("<div[\\s\\S]*</div>","").all());
        List<String> nexturls = page.getHtml()
                .xpath("//div[@class=seriesbox]/ul[@class=serieslist-ul]").all();
        System.out.println("crawlering...");
    }

    public static void main(String[] args) {
        Spider.create(new HonghuayuanBookPageProcessor())
                .addPipeline(new ConsolePipeline())
                .addUrl(starturl)
                .run();
    }
}
