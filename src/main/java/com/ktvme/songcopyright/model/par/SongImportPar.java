package com.ktvme.songcopyright.model.par;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * <p>Description: 歌曲单</p >
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
public class SongImportPar implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 歌曲Excel文件
     */
    @NotNull(message = "excel文件不能为空")
    private MultipartFile excelFile;
}