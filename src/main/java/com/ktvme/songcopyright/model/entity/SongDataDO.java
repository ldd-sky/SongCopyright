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

/**
 * <p>Description: 表song</p >
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
@TableName(value = "song")
public class SongDataDO implements Serializable {

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