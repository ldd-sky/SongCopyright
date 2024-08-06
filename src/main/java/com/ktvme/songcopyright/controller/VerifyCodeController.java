package com.ktvme.songcopyright.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * <p>Description: 图像验证码控制器</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年08月06日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
@Controller
@RequestMapping("/verify-code")
public class VerifyCodeController {
    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @GetMapping("/{username}")
    public void verifyCode(@PathVariable("username") String username , HttpServletResponse response ) throws IOException {

        String VERIFY_CODES = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";

        int IMG_WIDTH = 72;
        int IMG_HEIGTH = 27;
        Random random = new Random();
        //创建图片
        BufferedImage image = new BufferedImage(IMG_WIDTH, IMG_HEIGTH, BufferedImage.TYPE_INT_RGB);
        //画板
        Graphics g = image.getGraphics();
        //填充背景
        g.setColor(Color.WHITE);
        g.fillRect(1,1,IMG_WIDTH-2,IMG_HEIGTH-2);

        g.setFont(new Font("楷体", Font.BOLD,25));

        StringBuilder sb = new StringBuilder();
        //写字
        for(int i = 1 ; i <= 4 ; i ++){
            //随机颜色
            g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
            int len = random.nextInt(VERIFY_CODES.length());
            String str = VERIFY_CODES.substring(len,len+1);
            sb.append(str);
            g.drawString(str, IMG_WIDTH / 6 * i , 22 );
        }
        // 将验证码保存redis中
        stringRedisTemplate.opsForValue().set("register" + username , sb.toString() );


        // 生成随机干扰线
        for (int i = 0; i < 30; i++) {
            //随机颜色
            g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
            int x = random.nextInt(IMG_WIDTH - 1);
            int y = random.nextInt(IMG_HEIGTH - 1);
            int x1 = random.nextInt(12) + 1;
            int y1 = random.nextInt(6) + 1;
            g.drawLine(x, y, x - x1, y - y1);
        }

        //响应到浏览器
        ImageIO.write(image,"jpeg", response.getOutputStream());
    }
}