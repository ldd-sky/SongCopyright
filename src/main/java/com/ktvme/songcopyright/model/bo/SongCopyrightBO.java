package com.ktvme.songcopyright.model.bo;

import com.ktvme.songcopyright.model.entity.SongCopyrightDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.Year;
import java.time.format.DateTimeFormatter;

/**
 * <p>Description: 歌曲版权表业务对象</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年06月28日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
@Data
@Slf4j
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SongCopyrightBO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    private Integer id;

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

    public Year convertStrToYear(){
        if (year == null) return null;
        return Year.parse(year, DateTimeFormatter.ofPattern("yyyy"));
    }

    public static SongCopyrightDO convertSongCopyrightBo2Do(SongCopyrightBO bo){
        return SongCopyrightDO.builder()
                .songTitle(bo.songTitle)
                .artist(bo.artist)
                .album(bo.album)
                .recordCompany(bo.recordCompany)
                .copyrightCompany(bo.copyrightCompany)
                .distributionCompany(bo.distributionCompany)
                .originalCompany(bo.originalCompany)
                .year(bo.convertStrToYear())
                .build();
    }
}