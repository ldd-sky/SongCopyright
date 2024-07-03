package com.ktvme.songcopyright.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ktvme.songcopyright.model.par.SongCopyrightPagePar;
import com.ktvme.songcopyright.model.par.SongPar;
import com.ktvme.songcopyright.model.par.SongImportPar;
import com.ktvme.songcopyright.model.vo.SongCopyrightVO;

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
    boolean handleSongExcel(SongImportPar par);

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
    boolean addSongData(SongPar par);
}