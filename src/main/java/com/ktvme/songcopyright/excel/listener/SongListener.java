package com.ktvme.songcopyright.excel.listener;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.ktvme.songcopyright.excel.model.Song;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>Description: 歌单监听</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年06月28日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
@Slf4j
public class SongListener implements ReadListener<Map<Integer, String>> {

    private Integer limitRowSize = 2;
    private Integer limitColSize = 2;

    private Integer startHeadColIndex = 0;
    private Map<Integer, String> heads = new HashMap<>();
    private Integer rowHeadIndex = 0;

    @Getter
    private Map<String, Song> songMap = new HashMap<>();

    private static final String ROW_SONG_TITLE = "歌曲名";
    private static final String ROW_ARTIST = "演唱者";

    public SongListener(int limitRowSize, int limitColSize, int startHeadColIndex, int rowHeadIndex) {
        this.limitRowSize = limitRowSize;
        this.limitColSize = limitColSize;
        this.startHeadColIndex = startHeadColIndex;
        this.rowHeadIndex = rowHeadIndex;
    }

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext analysisContext) {
        log.info("[INF] ----> 解析了一条数据: {}", data);
    }

    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        Integer rowIndex = context.readSheetHolder().getRowIndex();
        log.info("[INF] ----> 解析头了: {}", headMap);
        if (Objects.equals(rowHeadIndex, rowIndex)) {
            // 需要记录head
            heads.clear();
            songMap.clear();
            headMap.forEach((index, cellData) -> {
                if (index >= startHeadColIndex && index < Integer.sum(startHeadColIndex, limitColSize)) {
                    // 要处理的head列
                    heads.put(index, cellData.getStringValue());
                    songMap.put(cellData.getStringValue(), Song.builder()
                            .build());
                }
            });
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("[INF] ----> 所有数据解析完毕!");
    }

    @Override
    public boolean hasNext(AnalysisContext context) {
        Integer rowIndex = context.readSheetHolder().getRowIndex();

        Integer headRowNumber = context.readSheetHolder().getHeadRowNumber();
        return rowIndex < Integer.sum(headRowNumber, limitRowSize - 1);
    }
}