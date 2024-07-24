package com.ktvme.songcopyright.spider;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ktvme.songcopyright.model.entity.SongCopyrightDO;
import com.ktvme.songcopyright.model.entity.SongDO;
import com.ktvme.songcopyright.utils.CopyrightSearchUtil;
import com.ktvme.songcopyright.utils.SongSearchUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 调用python接口查询音乐版权信息</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年07月04日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */

@Slf4j
public class MusicSearch {

    public static List<SongCopyrightDO> searchCopyrightFromWYY(SongDO songDO) {
        String title = songDO.getSongTitle();
        JsonObject songObjects = CopyrightSearchUtil.searchCopyrightFromWYY(SongSearchUtil.cleanSongName(title));
        if (songObjects == null) {
            log.info("[INF] ---> 网易云音乐没有 {} 这首歌", title);
            return null;
        }

        JsonObject result = songObjects.getAsJsonObject("result");
        if (result == null) {
            log.warn("[WARN] ---> result 对象为空");
            return null;
        }

        JsonArray songs = result.getAsJsonArray("songs");
        if (songs == null || songs.isEmpty()) {
            return null;
        }
        List<SongCopyrightDO> res = new ArrayList<>();

        // 遍历songs数组并提取所需信息
        for (JsonElement songElement : songs) {
            JsonObject song = songElement.getAsJsonObject();
            if (song == null) continue;

            // 获取歌曲名
            String songName = song.get("name").getAsString();
            if (SongSearchUtil.isSongTitleMatch(songName, title)) continue;

            // 获取所有歌手的名字拼接起来
            JsonArray artists = song.getAsJsonArray("artists");
            String artistNames = artistConcat(artists);

            if (!songName.contains(title) || !artistNames.contains(songDO.getArtist())){
                continue;
            }

            // 获取专辑名字
            JsonObject album = song.getAsJsonObject("album");
            String albumName = album.get("name").getAsString();

            String company = "";
            String publishDateFormatted;
            Date date = null;

            if (!albumName.isEmpty()){
                company = album.get("company").getAsString();
                long publishTimeMillis = album.get("publishTime").getAsLong();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                publishDateFormatted = sdf.format(new java.util.Date(publishTimeMillis));
                date = Date.valueOf(publishDateFormatted);
            }
            if (company.isEmpty() && albumName.isEmpty()) continue;
            if (company.equals("None")) company = "未确定";

            res.add(SongCopyrightDO.builder()
                    .songTitle(songName)
                    .artist(artistNames)
                    .album(albumName)
                    .originalCompany(company)
                    .date(date)
                    .build());
        }
        return res;
    }

    public static List<SongCopyrightDO> searchCopyrightFromQQ(SongDO songDO, Integer current, Integer size) {
        String title = songDO.getSongTitle();
        JsonObject songObjects = CopyrightSearchUtil.searchMusicInfoFromQQ(SongSearchUtil.cleanSongName(title), current.toString(), size.toString());
        if (songObjects == null) {
            log.info("[INF] ---> QQ音乐没有 {} 这首歌", title);
            return null;
        }
        JsonArray songList;
        try {
            songList = songObjects.getAsJsonObject("data")
                    .getAsJsonObject("song")
                    .getAsJsonArray("list");
        } catch (Exception ignored){
            log.warn("[WARN] ---> data 对象为空");
            return null;
        }

        List<SongCopyrightDO> res = new ArrayList<>();

        for (JsonElement songElement : songList) {
            JsonObject song = songElement.getAsJsonObject();
            if (song == null) continue;

            // 获取歌曲名
            String songTitle = song.get("songname").getAsString();
            if (SongSearchUtil.isSongTitleMatch(songTitle, title)) continue;

            // 获取所有歌手的名字拼接起来
            JsonArray artists = song.getAsJsonArray("singer");
            String artistNames = artistConcat(artists);
            if (!songTitle.contains(title) || !artistNames.contains(songDO.getArtist())){
                continue;
            }

            // 获取专辑名字
            String albumName = song.get("albumname").getAsString();
            String company = "";
            String publishDateFormatted;
            Date date = null;
            if (!albumName.isEmpty()) {
                // 获取专辑的公司
                String albumId = song.get("albummid").getAsString();
                JsonObject albumInfo = CopyrightSearchUtil.searchCompanyFromQQ(albumId).getAsJsonObject("data");
                company = albumInfo.get("company").getAsString();
                publishDateFormatted = albumInfo.get("aDate").getAsString();
                date = Date.valueOf(publishDateFormatted);
            }
            if (company.isEmpty() && albumName.isEmpty()) continue;

            res.add(SongCopyrightDO.builder()
                    .songTitle(songTitle)
                    .artist(artistNames)
                    .album(albumName)
                    .originalCompany(company)
                    .date(date)
                    .build());
        }
        return res;
    }

    private static String artistConcat(JsonArray artists) {
        StringBuilder artistNames = new StringBuilder();
        for (JsonElement artistElement : artists) {
            JsonObject artist = artistElement.getAsJsonObject();
            if (artistNames.length() > 0) {
                artistNames.append("/");
            }
            artistNames.append(artist.get("name").getAsString());
        }
        return artistNames.toString();
    }

    public static void main(String[] args) {
        SongDO songDO = SongDO.builder()
                .songTitle("吹梦到西洲")
                .artist("")
                .build();
        List<SongCopyrightDO> a;
        a = searchCopyrightFromWYY(songDO);
        assert a != null;
        for (SongCopyrightDO i : a){
            System.out.println(i);
        }
    }
}

