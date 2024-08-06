package com.ktvme.songcopyright.utils;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;

/**
 * <p>Description: 邮件工具类</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年08月06日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
public class EmailUtil {

    /**
     * 发送邮件
     */
    public static void sendEmail(JavaMailSender javaMailSender, String title , String email, String text) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
            // a. 发件人
            mimeMessageHelper.setFrom("itcast_lt@126.com");
            // b. 收件人
            mimeMessageHelper.setTo(email);
            // c. 主题
            mimeMessageHelper.setSubject(title);
            // d. 正文
            mimeMessageHelper.setText(text, true);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}