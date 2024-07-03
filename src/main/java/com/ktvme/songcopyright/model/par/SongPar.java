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
 * <p>Description: 增加歌曲传递参数</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
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
public class SongPar implements Serializable {
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
}