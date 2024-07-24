package com.ktvme.songcopyright.utils;

import java.text.Normalizer;
import java.util.Locale;

/**
 * <p>Description: 判断搜索到的歌名和目标歌名是否一致</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年07月24日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
public class SongSearchUtil {
    /**
     * 在搜索的时候去除歌曲名中的[MTV]等
     */
    public static String cleanSongName(String songName) {
        if (songName == null) {
            return null;
        }
        return songName.replaceAll("\\[.*?\\]", "").trim();
    }

    public static boolean isSongTitleMatch(String targetName, String searchedName) {
        if (targetName == null || searchedName == null) {
            return true;
        }
        String normalizedTarget = normalizeAndClean(targetName);
        String normalizedSearched = normalizeAndClean(searchedName);

        return !normalizedTarget.equalsIgnoreCase(normalizedSearched) && !normalizedTarget.contains(normalizedSearched);
    }


    private static String normalizeAndClean(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                .replaceAll("[^\\p{IsAlphabetic}\\p{IsDigit}]", "")
                .toLowerCase(Locale.ROOT);
    }

}