package com.ktvme.songcopyright.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Year;

/**
 * <p>Description: 表song_copyright</p >
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
@TableName(value = "song_copyright")
public class SongCopyrightDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    @TableField(value = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 歌曲名
     */
    @TableField(value = "song_title")
    private String songTitle;

    /**
     * 演唱者
     */
    @TableField(value = "artist")
    private String artist;

    /**
     * 专辑名称
     */
    @TableField(value = "album")
    private String album;

    /**
     * 唱片公司
     */
    @TableField(value = "record_company")
    private String recordCompany;

    /**
     * 版权公司
     */
    @TableField(value = "copyright_company")
    private String copyrightCompany;

    /**
     * 发行公司
     */
    @TableField(value = "distribution_company")
    private String distributionCompany;

    /**
     * 原始唱片公司或发行公司
     */
    @TableField(value = "original_company")
    private String originalCompany;

    /**
     * 发行年份
     */
    @TableField(value = "year")
    private Year year;

    /**
     * 删除标记
     */
    @TableField(value = "del_tag")
    private boolean delTag;

    /**
     * 数据创建时间
     */
    @TableField(value = "created_at")
    private LocalDateTime createdAt;

    /**
     * 数据更新时间
     */
    @TableField(value = "updated_at")
    private LocalDateTime updatedAt;
}