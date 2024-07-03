package com.ktvme.songcopyright.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * <p>Description: 爬虫程序</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <P>Created Date: 2024年06月27日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
public class SpiderApplicaiton {

    public static void main(String[] args) {
        String s = "笔记本";
        final String encode = URLEncoder.encode(s);
        Document doc = null;
        try {
            doc = Jsoup.connect("https://re.jd.com/search?keyword="+encode)
                    .userAgent("Mozilla")
                    .timeout(5000)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(doc);
    }
}