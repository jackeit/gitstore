import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;
import java.util.List;
import java.util.Random;
/*
*
* 适用于https://www.xinyushuwu.com的小说爬虫
* */
public class BookPageProcessor1 implements PageProcessor {
    public static int ChapterNum = 1;
    Random ran = new Random();

    private Site site = Site.me()
            .setCharset("GBK")//编码
            .setSleepTime(ran.nextInt(2)*100+300)//抓取间隔时间
            .setTimeOut(1000*10)//超时时间
            .setRetrySleepTime(5000)//重试时间
            .setRetryTimes(8)//重试次数
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.62 Safari/537.36");

    @Override
    public void process(Page page) {
        page.putField("title",page.getHtml().xpath("//div[@class=nr_title]/h3/text()").all());
        page.putField("content",page.getHtml().xpath("//p[@id=articlecontent]/html()")
                .replace("&nbsp;"," ").replace("<br>","\r\n").all());
        List<Selectable> l = page.getHtml().xpath("//div[@class=nr_page]/a").nodes();
        String baseurl = "https://www.xinyushuwu.com";
        String extendurl = "";
        System.out.println(l.get(0).xpath("//a/text()").toString());
        System.out.println(l.get(0).xpath("//a//@href").toString());
        for(Selectable i :l){
            if(i.xpath("//a/text()").toString().equals("下一章")){
                extendurl = i.xpath("//a//@href").toString();
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
        Spider.create(new BookPageProcessor1())
                .addUrl("https://www.xinyushuwu.com/33/33810/1802160.html")
                .addPipeline(new ConsolePipeline())
                .addPipeline(new BookMypipeline("f:/javatest/book2.txt",1))
                .run();
        System.out.println("end");
    }
}
