import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
*
*
* 适合pixiv小说的网络爬虫，待完善*/
public class PixivBookPageProcessor implements PageProcessor {
    private static String starturl = "https://www.pixiv.net/novel/show.php?id=14957292";
//    爬虫配置设定
    private static Site site = Site.me()
            .setCharset("UTF-8")
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.62 Safari/537.36")
            .setSleepTime(300)//抓取间隔时间
            .setTimeOut(1000*10)//超时时间
            .setRetrySleepTime(5000)//重试时间
            .setRetryTimes(8)
            .addHeader("Accept-Encoding", "/")
            .addHeader("Connection", "keep-alive")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.9")
            .setDomain(".pixiv.net");

//        .addCookie();
//        .setDomain("https://www.pixiv.net/")
//        .addCookie()

    @Override
    public Site getSite() {
        return site;
    }
    @Override
    public void process(Page page) {
//        标题获取
        page.putField("title",page.getHtml().xpath("//h1/text()").all());
//        内容获取
        page.putField("content",page.getHtml().xpath("//div[@class='sc-iXeHaJ gUvCZZ']/p[@class='sc-ljsmAU iBHPvh']/html()")
                .all());
        String baseurl ="https://www.pixiv.net/";
        String extendurl=page.getHtml().xpath("//div[@class='sc-fv2g1c-4 ffOiTG']" +
                "/a[@class='sc-d98f2c-0 sc-1ij5ui8-1 jmbJSP gtm-novel-work-series-next']/@href").toString();
//        设置referer
        String nexturl = baseurl+extendurl;
        site.addHeader("referer",starturl);
        starturl = nexturl;

//        page.addTargetRequest(nexturl);
    }
    public static void main(String[] args) {
        Request request = new Request("https://www.pixiv.net/novel/show.php?id=14957292");
        Map<String, Object> map = new HashMap();
        map.put("login_id","abacyx@hotmail.com");
        map.put("password","20000428ABcd");
        request.setMethod(HttpConstant.Method.POST);
        request.setRequestBody(HttpRequestBody.form(map,"utf-8"));
//        request.addHeader("cookie", cookie);
//        try {
//            setCookies();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Spider.create(new PixivBookPageProcessor())
                .addUrl(starturl)
                .addPipeline(new ConsolePipeline())
                .addPipeline(new BookMypipeline("F:/javatest/pixivbook.txt",1))
                .run();

    }
    public static void setCookies() throws Exception{
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build();
        CloseableHttpResponse response = null;
        List<NameValuePair> paramList = new ArrayList<>();
        paramList.add(new BasicNameValuePair("login_id","abacyx@hotmail.com"));
        paramList.add(new BasicNameValuePair("password","20000428ABcd"));
        HttpPost httpPost = new HttpPost("https://accounts.pixiv.net/login?return_to=https%3A%2F%2Fwww.pixiv.net%2F&lang=zh&source=pc&view_type=page");
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList,"UTF-8");
        httpPost.setEntity(entity);
        httpPost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
        httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
//        httpPost.setHeader("Connection", "keep-alive");
        response = httpClient.execute(httpPost);
        for(Cookie i :cookieStore.getCookies()){
            site.addCookie(i.getName(),i.getValue());
        }

    }

}
