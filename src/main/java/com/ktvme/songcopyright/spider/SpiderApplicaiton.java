package com.ktvme.songcopyright.spider;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;

/**
 * <p>Description: 爬虫程序</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <P>Created Date: 2024年06月27日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
public class SpiderApplicaiton {

    public static void main(String[] args) throws IOException {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        //当JS执行出错的时候是否抛出异常, 这里选择不需要
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        //当HTTP的状态非200时是否抛出异常, 这里选择不需要
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setActiveXNative(false);
        //是否启用CSS, 因为不需要展现页面, 所以不需要启用
        webClient.getOptions().setCssEnabled(false);
        //很重要，启用JS。有些网站要开启！
        webClient.getOptions().setJavaScriptEnabled(true);
        //很重要，设置支持AJAX
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setRedirectEnabled(false);
        webClient.getCache().setMaxSize(100);
        HtmlPage page = webClient.getPage("https://www.baidu.com");
        HtmlElement element= (HtmlElement) page.getByXPath("//div[@id='title_id']/h1").get(0);
        System.out.println(element.asXml());
        System.out.println(element.getAttribute("href"));
    }
}