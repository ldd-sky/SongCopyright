package com.ktvme.songcopyright.model.par;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

/**
 * <p>Description: todo</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: todo</p >
 * <P>Created Date: 2024年06月28日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
@Data
@Valid
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SongCopyrightDetailPar implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 歌曲名
     */
    @NotNull(message = "歌曲名不能为空")
    private String songTitle;

    /**
     * 演唱者
     */
    @NotNull(message = "演唱者不能为空")
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
    @NotNull(message = "发行年份不能为空")
    private String year;
}