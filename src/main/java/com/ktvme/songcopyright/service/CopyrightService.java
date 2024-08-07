package com.ktvme.songcopyright.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ktvme.songcopyright.model.Result;
import com.ktvme.songcopyright.model.par.SongCopyrightPagePar;
import com.ktvme.songcopyright.model.par.SongPar;
import com.ktvme.songcopyright.model.par.SongImportPar;
import com.ktvme.songcopyright.model.vo.SongCopyrightVO;

import java.util.Map;

/**
 * <p>Description: 版权查询业务操作接口</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年06月28日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
public interface CopyrightService {

    /**
     * 导入歌曲表
     * @param par   参数
     * @return      true-成功 false-失败
     */
    Result handleSongExcel(SongImportPar par);

    /**
     * 分页获取版权信息查询结果
     * @param par   参数
     * @return      {@link SongCopyrightVO}
     */
    IPage<SongCopyrightVO> pageSongCopyrightResults(SongCopyrightPagePar par);

    /**
     * 新增歌曲信息
     * @param par   参数
     * @return      true-成功 false-失败
     */
    Result addSongData(SongPar par);

    /**
     * 分页返回歌曲版权在线查询结果并入库
     * @param par   参数
     * @return      {@link SongCopyrightVO}
     */
    IPage<SongCopyrightVO> pageSongCopyrightSearchResults(SongCopyrightPagePar par);

    /**
     * 获取歌曲以及歌曲版权数量
     * @return      数量
     */
    Result<Map<String, Integer>> getCount();
}