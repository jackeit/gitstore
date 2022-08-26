package Test;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

public class ClawlerTest implements PageProcessor {
    public void process(Page page) {
        page.putField("author", page.getHtml().css("h1").all());
    }

    private Site site = Site.me();
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new ClawlerTest())
                //初始访问url地址
                .addUrl("https://www.jd.com/moreSubject.aspx")
                .run();
    }
}
