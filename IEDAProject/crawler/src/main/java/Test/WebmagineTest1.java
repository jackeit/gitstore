package Test;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebmagineTest1 implements PageProcessor {
    private Site site = Site.me()
            .setCharset("gbk")//编码
            .setSleepTime(200)//抓取间隔时间
            .setTimeOut(1000*10)//超时时间
            .setRetrySleepTime(5000)//重试时间
            .setRetryTimes(20)//重试次数
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.62 Safari/537.36");
    @Override
    public void process(Page page) {
        List<String> list = page.getHtml().xpath("//tr/html()").all();
        page.addTargetRequest("http://www.huanxiangji.com/modules/article/bookcase.php");
        page.putField("title",page.getHtml().xpath("title").all());
        System.out.println("hr");
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
//        Map<String,Object> paramList = new HashMap<>();
//        paramList.put("email","1111@163.com");
//        paramList.put("password","222222");
        Request request = new Request("http://www.huanxiangji.com/modules/article/bookcase.php");
        request.setMethod(HttpConstant.Method.POST);
        request.setRequestBody(HttpRequestBody.json("{'username':'jackitchen','password':'fdhsuiyrf7842.'}","gbk"));
//        request.setRequestBody(HttpRequestBody.form(paramList,"utf-8"));
        Spider spider = Spider.create(new WebmagineTest1())
                .addRequest(request);
        spider.run();

    }
}
