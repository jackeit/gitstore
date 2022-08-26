import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;
import java.util.List;
import java.util.Random;
/*
* 本程序适用于https://www.rouwennp.club/的网络小说爬虫*/
public class RouwennpBookPageProcessor3 implements PageProcessor {
    public static int ChapterNum = 1;
    public static String starturl = "https://www.rouwennp.club/book/25307/5132958.html";
    Random ran = new Random();

    private Site site = Site.me()
            .setCharset("UTF-8")//编码
            .setSleepTime(200+ran.nextInt(3)*100)//抓取间隔时间
            .setTimeOut(1000*10)//超时时间
            .setRetrySleepTime(5000)//重试时间
            .setRetryTimes(40)//重试次数
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.62 Safari/537.36");
    @Override
    public void process(Page page) {
        System.out.println(site.getHeaders());
        page.putField("content",page.getHtml().xpath("//div[@id=TextContent]/html()")
                .replace("&emsp;"," ").replace("<br>","\r\n")
                .replace("<dt[\\s\\S]*</dt>","")
                .replace("<script[\\s\\S]*</script>","")
                .all());
        //不到最后一步，不要使用xpath中的html()，否则后续要再度使用xpath的时候，无论怎么用都会报错
//        List<String> i = l.get(0).xpath("/text()").all();
        page.putField("title",page.getHtml().xpath("//div[@id=mlfy_main_text]/h1/text()")
                        .replace("\\d+·","")
                .all());
        String baseurl = "https://www.rouwennp.club";
        String extendurl = "";
        List<Selectable> i = page.getHtml().xpath("//p[@class=mlfy_page]/a").nodes();

        for (Selectable j:i){
            if (j.xpath("//a/text()").toString().equals("下一章")){
                System.out.println("1");
                extendurl = j.xpath("//a/@href").toString();
                break;
            }
        }
        site.addHeader("Referer",starturl);
//        starturl = baseurl + extendurl;
        page.addTargetRequest(baseurl+extendurl);
        System.out.println(starturl);


    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new RouwennpBookPageProcessor3())
                .addUrl(starturl)
                .addPipeline(new ConsolePipeline())
                .addPipeline(new BookMypipeline("F:/javatest/book3.txt",2))
                .run();
        System.out.println("end");
    }

}
