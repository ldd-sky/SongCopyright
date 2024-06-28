package com.ktvme.songcopyright.spider;

import com.ktvme.songcopyright.utils.HttpUtils;
import org.jsoup.nodes.Document;
import org.junit.Test;
/**
 * <p>Description: HttpUtil 测试类</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <P>Created Date: 2024年06月27日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */

public class HttpUtilsTest {
    private static final String TEST_URL = "http://www.google.com/";

    @Test
    public void testGetHtmlPageResponse() {
        HttpUtils httpUtils = HttpUtils.getInstance();
        httpUtils.setTimeout(30000);
        httpUtils.setWaitForBackgroundJavaScript(30000);
        try {
            String htmlPageStr = httpUtils.getHtmlPageResponse(TEST_URL);
            //TODO
            System.out.println(htmlPageStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetHtmlPageResponseAsDocument() {
        HttpUtils httpUtils = HttpUtils.getInstance();
        httpUtils.setTimeout(30000);
        httpUtils.setWaitForBackgroundJavaScript(30000);
        try {
            Document document = httpUtils.getHtmlPageResponseAsDocument(TEST_URL);
            //TODO
            System.out.println(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}