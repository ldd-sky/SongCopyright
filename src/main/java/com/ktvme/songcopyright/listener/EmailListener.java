package com.ktvme.songcopyright.listener;

import com.alibaba.fastjson.JSONObject;
import com.ktvme.songcopyright.model.vo.UserEmail;
import com.ktvme.songcopyright.utils.EmailUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p>Description: 监听email-topic</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年08月06日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
@Component
@Slf4j
@AllArgsConstructor
@RocketMQMessageListener(topic = "email-topic", consumerGroup = "email_consumer_group")
public class EmailListener implements RocketMQListener<String> {
    @Resource
    private JavaMailSender javaMailSender;

    @Override
    public void onMessage(String message) {
        UserEmail userEmail = JSONObject.parseObject(message, UserEmail.class);
        if (userEmail.getEmail() != null) {
            EmailUtil.sendEmail(javaMailSender, "验证码", userEmail.getEmail(), userEmail.getText());
        }
    }
}