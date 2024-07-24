package com.ktvme.songcopyright.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ktvme.songcopyright.model.par.SongCopyrightPagePar;
import com.ktvme.songcopyright.model.par.SongImportPar;
import com.ktvme.songcopyright.model.par.SongPar;
import com.ktvme.songcopyright.model.vo.SongCopyrightVO;
import com.ktvme.songcopyright.service.CopyrightService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Description: 版权控制器</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年06月28日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping(path = "/song")
public class CopyrightController {

    private final CopyrightService copyrightService;

    /**
     * 处理song excel表
     *
     * @return true-成功 false-失败
     */
    @PostMapping(path = "/handle-excel-song")
    public boolean handleExcelSongData(@Valid SongImportPar par){
        return copyrightService.handleSongExcel(par);
    }

    /**
     * 分页获取歌曲版权信息
     * @param par 参数
     * @return {@link SongCopyrightVO}
     */
    @GetMapping(path = "page-songCopyright")
    public IPage<SongCopyrightVO> pageSongCopyrightDetails(@Valid SongCopyrightPagePar par){
        return copyrightService.pageSongCopyrightResults(par);
    }

    /**
     * 增加歌曲
     * @param par 参数
     * @return true-成功 false-失败
     */
    @PostMapping(path = "/add")
    public boolean addSongData(@Valid SongPar par){
        return copyrightService.addSongData(par);
    }

    /**
     * 在线搜索没有的歌曲版权信息
     * @param par   参数
     * @return      {@link SongCopyrightVO}
     */
    @GetMapping(path = "page-songCopyrightSearch")
    public IPage<SongCopyrightVO> pageSongCopyrightSearchDetails(@Valid SongCopyrightPagePar par){
        return copyrightService.pageSongCopyrightSearchResults(par);
    }
}