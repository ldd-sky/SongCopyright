package com.ktvme.songcopyright.service;

import com.ktvme.songcopyright.exception.BusinessException;
import com.ktvme.songcopyright.model.ResultEnum;
import com.ktvme.songcopyright.model.entity.SongCopyrightDO;
import com.ktvme.songcopyright.model.entity.SongDO;
import com.ktvme.songcopyright.service.dao.SongCopyrightService;
import com.ktvme.songcopyright.spider.NetEaseMusicSearch;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.util.List;

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
        List<SongCopyrightDO> songCopyrightDOS = NetEaseMusicSearch.searchCopyrightFromNetEaseMusic(songDO);

        boolean success = songCopyrightService.saveBatch(songCopyrightDOS);
        log.info("[INF] ---> songCopyRight save res: {}", success);

        if (!success){
            throw new BusinessException(ResultEnum.COMMON_FAILED.getCode(), "保存歌曲版权数据失败");
        }

        // 每隔2秒消费一条消息，防止ip被封
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}