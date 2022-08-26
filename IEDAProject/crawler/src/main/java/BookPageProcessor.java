import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.monitor.SpiderMonitor;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import java.util.List;
import java.util.Random;
/*
 * 适用于http://www.minjianxiaoshuo.com网站爬虫，待完善*/
public class BookPageProcessor implements PageProcessor {
    public static int ChapterNum = 1;
    Random ran = new Random();

    private Site site = Site.me()
            .setCharset("UTF-8")//编码
        .setSleepTime(ran.nextInt(5)+10)//抓取间隔时间
        .setTimeOut(1000*10)//超时时间
        .setRetrySleepTime(5000)//重试时间
        .setRetryTimes(8)//重试次数
        .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.62 Safari/537.36");
    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {
        page.putField("content", page.getHtml().xpath("//div[@class=contentinfo]//p/html()")
                .replace("&nbsp;"," ").replace("<br>","").all());
        page.putField("title",page.getHtml().xpath("//div[@class=container]/h1[@class=page-title]/text()").all());
        String baseURL ="http://www.minjianxiaoshuo.com/67/67079/";

//        page.putField("author", page.getHtml().xpath("//div[@class=contentinfo]//p/text()").all());
//        List<Selectable> j = page.getHtml().xpath("//div[@class=contentinfo]//p/html()").nodes();
//        List<String> k = page.getHtml().xpath("//div[@class=contentinfo]//p/html()").all();
        List<String> l = page.getHtml().xpath("//div[@class='mod page-control']/div[@class=bd]" +
                "/a[@class=next]/@href").all();
        page.addTargetRequest(baseURL+l.get(0));
        System.out.println("进行中:"+l.get(0));

    }

    public static void main(String[] args)  throws Exception{

        Spider bookspider = Spider.create(new BookPageProcessor())
                .addUrl("http://www.minjianxiaoshuo.com/67/67079/7540351.html")
                .addPipeline(new ConsolePipeline())
                .addPipeline(new BookMypipeline("F:/javatest/倒霉的林以【高H、NP】.txt",1));
        SpiderMonitor.instance().register(bookspider);
        bookspider.run();
        System.out.println("end");
    }

//8158727
}
