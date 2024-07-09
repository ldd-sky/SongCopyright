package com.ktvme.songcopyright.spider;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ktvme.songcopyright.exception.BusinessException;
import com.ktvme.songcopyright.model.ResultEnum;
import com.ktvme.songcopyright.model.entity.SongCopyrightDO;
import com.ktvme.songcopyright.model.entity.SongDO;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 调用python接口查询网易云音乐信息</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年07月04日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */

@Slf4j
public class NetEaseMusicSearch {
    private static final String pythonScriptPath = "src/main/java/com/ktvme/songcopyright/spider/pyScript/search.py";
    private static final String pythonInterpreter = "/Users/liuyuhan/opt/anaconda3/envs/video/bin/python3.8";

    @Nullable
    public static JsonObject searchCopyrightFrom163(String songTitle){
        String[] cmd = new String[]{pythonInterpreter, pythonScriptPath, songTitle};

        try {
            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                Gson gson = new Gson();
                return gson.fromJson(output.toString(), JsonObject.class);
            } else {
                System.err.println("Python script failed with exit code: " + exitCode);
                return null;
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<SongCopyrightDO> searchCopyrightFromNetEaseMusic(SongDO songDO) {
        String title = songDO.getSongTitle();
        JsonObject songObjects = searchCopyrightFrom163(title);
        if (songObjects == null) {
            throw new BusinessException(ResultEnum.FILE.getCode(), "网易云没有" + title + "这首歌");
        }

        JsonObject result = songObjects.getAsJsonObject("result");
        JsonArray songs = result.getAsJsonArray("songs");
        List<SongCopyrightDO> res = new ArrayList<>();

        // 遍历songs数组并提取所需信息
        for (JsonElement songElement : songs) {
            JsonObject song = songElement.getAsJsonObject();

            // 获取歌曲名
            String songName = song.get("name").getAsString();

            // 获取所有歌手的名字拼接起来
            JsonArray artists = song.getAsJsonArray("artists");
            StringBuilder artistNames = new StringBuilder();
            for (JsonElement artistElement : artists) {
                JsonObject artist = artistElement.getAsJsonObject();
                if (artistNames.length() > 0) {
                    artistNames.append("/");
                }
                artistNames.append(artist.get("name").getAsString());
            }

            // 获取专辑名字
            JsonObject album = song.getAsJsonObject("album");
            String albumName = album.get("name").getAsString();
            if (albumName == null) {
                albumName = "";
            }
            // 获取专辑的公司
            String company = album.get("company").getAsString();


            long publishTimeMillis = album.get("publishTime").getAsLong();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String publishDateFormatted = sdf.format(new java.util.Date(publishTimeMillis));

            res.add(SongCopyrightDO.builder()
                    .songTitle(songName)
                    .artist(artistNames.toString())
                    .album(albumName)
                    .originalCompany(company)
                    .date(Date.valueOf(publishDateFormatted))
                    .build());
        }
        return res;
    }
}

