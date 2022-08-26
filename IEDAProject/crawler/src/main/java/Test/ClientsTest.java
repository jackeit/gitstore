package Test;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.HttpConstant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientsTest {
    public static void main(String[] args) {
        try {
//            getCookies1();
//            huanxiangjitest();
            pixivgetCookies2();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        WebmagineTest webmagineTest = new WebmagineTest();
    }

    public static String getCookies1(){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        List<NameValuePair> paramlist = new ArrayList<>();
        paramlist.add(new BasicNameValuePair("email","1111@163.com"));
        paramlist.add(new BasicNameValuePair("password","222222"));
        HttpPost httpPost = new HttpPost("http://localhost:8080/signin");
        UrlEncodedFormEntity entity = null;
        String resultString = "";
        try {
            entity = new UrlEncodedFormEntity(paramlist,"UTF-8");
            httpPost.setEntity(entity);
            response = httpClient.execute(httpPost);
            Header[] cookies = response.getHeaders("Set-Cookie");
            for(Header i : cookies){
                System.out.println("name: "+i.getName());
                System.out.println("value: "+i.getValue());
            }
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");


        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(resultString);
        return resultString;
    }
    public static String pixivgetCookies2() throws  Exception{
        CookieStore cookieStore = new BasicCookieStore();
//        CloseableHttpClient httpClient = HttpClients.createDefault();
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
//        httpPost.setHeader("Connection", "keep-alive");
        response = httpClient.execute(httpPost);
        Header[] cookies = response.getHeaders("Set-cookie");
        HttpGet httpGet = new HttpGet("https://www.pixiv.net/novel/show.php?id=14957292");
        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
        response = httpClient.execute(httpGet);
        List<String> cookieslist = new ArrayList<>();

        for(Header i : cookies){
            cookieslist.add(i.getName()+i.getValue());
            System.out.println("name:"+i.getName()+",value:"+i.getValue());

        }
        for (Cookie i : cookieStore.getCookies()){
            System.out.println("name:"+i.getName()+",value:"+i.getValue());
        }
        return "";


    }

    public static void huanxiangjitest() throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        List<NameValuePair> pairList = new ArrayList<>();
        pairList.add(new BasicNameValuePair("small-username","jackitchen"));
        pairList.add(new BasicNameValuePair("small-password","fdhsuiyrf7842."));
//        pairList.add(new BasicNameValuePair("usecookie","0"));
        pairList.add(new BasicNameValuePair("action","login"));
//        pairList.add(new BasicNameValuePair("submit","%CC%E1+%BD%BB"));
        HttpPost httpPost = new HttpPost("http://www.huanxiangji.com/");
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairList,"gbk");

//        entity.setContentType("application/json;charset=gbk");
//        http://www.huanxiangji.com/Login.php
        httpPost.setEntity(entity);
        httpPost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
        httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
        httpPost.setHeader("Cache-Control", "max-age=0");
        httpPost.setHeader("Connection", "keep-alive");
        httpPost.setHeader("Accept-Encoding", "gzip,deflate");
        httpPost.setHeader("Host","www.huanxiangji.com");
        httpPost.setHeader("Cache-Control","no-cache");
        httpPost.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        try{
            response = httpClient.execute(httpPost);
            Header[] cookies = response.getHeaders("Set-Cookie");
            System.out.println("cookies:"+cookies);
        } catch(Exception e){
            e.printStackTrace();
        }finally {
            String entityString = EntityUtils.toString(response.getEntity(),"gbk");
            System.out.println("entity:"+entityString);
            System.out.println(response);
            System.out.println("end");
            httpPost.abort();
        }



    }
    public static void gethuanxiang() throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        HttpGet httpGet = new HttpGet("http://www.huanxiangji.com/");
        UrlEncodedFormEntity entity;
        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
        httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
        httpGet.setHeader("Cache-Control", "max-age=0");
        httpGet.setHeader("Connection", "keep-alive");
        httpGet.setHeader("Accept-Encoding", "gzip,deflate");
        httpGet.setHeader("Host","www.huanxiangji.com");
        response = httpClient.execute(httpGet);
        Header[] cookies = response.getHeaders("Set-Cookie");

    }
    public static void getpixivnoval() throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        HttpGet httpGet = new HttpGet("https://www.pixiv.net/novel/show.php?id=14957292");
        UrlEncodedFormEntity entity;
        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
        httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
        httpGet.setHeader("Cache-Control", "max-age=0");
        httpGet.setHeader("Connection", "keep-alive");
        httpGet.setHeader("Accept-Encoding", "gzip,deflate");
        httpGet.setHeader("Host","www.huanxiangji.com");
        response = httpClient.execute(httpGet);
        Header[] cookies = response.getHeaders("Set-Cookie");

    }
}