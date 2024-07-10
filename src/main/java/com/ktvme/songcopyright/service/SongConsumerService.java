package com.ktvme.songcopyright.service;

import com.ktvme.songcopyright.exception.BusinessException;
import com.ktvme.songcopyright.model.ResultEnum;
import com.ktvme.songcopyright.model.entity.SongCopyrightDO;
import com.ktvme.songcopyright.model.entity.SongDO;
import com.ktvme.songcopyright.service.dao.SongCopyrightService;
import com.ktvme.songcopyright.spider.MusicSearch;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>Description: song信息搜索处理</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年07月09日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
@Component
@Slf4j
@AllArgsConstructor
@RocketMQMessageListener(topic = "song-topic", consumerGroup = "song_consumer_group")
public class SongConsumerService implements RocketMQListener<SongDO> {
    private final SongCopyrightService songCopyrightService;
    @Override
    public void onMessage(SongDO songDO) {
        // 睡眠2s，防止ip封禁
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<SongCopyrightDO> songCopyrightWYY = MusicSearch.searchCopyrightFromWYY(songDO);
        List<SongCopyrightDO> songCopyrightQQ = MusicSearch.searchCopyrightFromQQ(songDO, 1, 10);
        // 去重
        List<SongCopyrightDO> mergedList = new ArrayList<>();
        if (songCopyrightWYY != null) mergedList.addAll(songCopyrightWYY);
        if (songCopyrightQQ != null) mergedList.addAll(songCopyrightQQ);
        if (mergedList.isEmpty()){
            log.info("[INF] ---> 搜索不到该歌曲的信息: {}", songDO.getSongTitle());
            return;
        }
        // 按照 songTitle 排序并根据 songTitle、artist 和 company 进行去重
        List<SongCopyrightDO> saveList = mergedList.stream()
                .sorted(Comparator.comparing(SongCopyrightDO::getSongTitle))
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() ->
                                new TreeSet<>(Comparator.comparing(
                                        song -> song.getSongTitle() + song.getArtist() + song.getOriginalCompany()))),
                        ArrayList::new));

        boolean success = songCopyrightService.saveBatch(saveList);
        log.info("[INF] ---> songCopyRight save res: {}", success);

        if (!success){
            throw new BusinessException(ResultEnum.COMMON_FAILED.getCode(), "保存歌曲版权数据失败");
        }


    }
}