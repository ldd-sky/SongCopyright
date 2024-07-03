package com.ktvme.songcopyright.model.bo;

import com.ktvme.songcopyright.model.entity.SongDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * <p>Description: 歌曲表业务对象</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年07月03日</P>
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
public class SongBO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 歌曲名
     */
    private String songTitle;

    /**
     * 演唱者
     */
    private String artist;

    public static SongDO convertSongBo2Do(SongBO bo){
        return SongDO.builder()
                .id(null)
                .songTitle(bo.songTitle)
                .artist(bo.artist)
                .build();
    }
}