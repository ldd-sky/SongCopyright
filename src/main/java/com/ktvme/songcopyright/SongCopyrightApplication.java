package com.ktvme.songcopyright;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.ktvme.songcopyright.dao")
public class SongCopyrightApplication {

    public static void main(String[] args) {
        SpringApplication.run(SongCopyrightApplication.class, args);
    }

}
