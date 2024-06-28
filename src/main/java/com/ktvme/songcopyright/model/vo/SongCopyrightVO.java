package com.ktvme.songcopyright.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>Description: 歌曲版权展示对象</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年06月28日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SongCopyrightVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 歌曲名
     */
    private String songTitle;

    /**
     * 演唱者
     */
    private String artist;

    /**
     * 专辑名称
     */
    private String album;

    /**
     * 唱片公司
     */
    private String recordCompany;

    /**
     * 版权公司
     */
    private String copyrightCompany;

    /**
     * 发行公司
     */
    private String distributionCompany;

    /**
     * 原始唱片公司或发行公司
     */
    private String originalCompany;

    /**
     * 发行年份
     */
    private String year;
}