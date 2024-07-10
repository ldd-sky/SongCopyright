package com.ktvme.songcopyright.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * <p>Description: 版权信息查询工具类</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年07月10日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
public class CopyrightSearchUtil {
    private static final String pythonInterpreter = "/Users/liuyuhan/opt/anaconda3/envs/video/bin/python3.8";
    /**
     * 网易云音乐
     */
    private static final String wyySearchScriptPath = "src/main/java/com/ktvme/songcopyright/spider/pyScript/wyy_search.py";
    /**
     * qq音乐
     */
    private static final String qqSearchScriptPath = "src/main/java/com/ktvme/songcopyright/spider/pyScript/qq_search.py";
    /**
     * qq音乐专辑信息
     */
    private static final String qqAlbumSearchScriptPath = "src/main/java/com/ktvme/songcopyright/spider/pyScript/qq_album_search.py";

    public static JsonObject searchCopyrightFromWYY(String title){
        String[] cmd = new String[]{pythonInterpreter, wyySearchScriptPath, title};
        return getJsonObject(cmd);
    }

    public static JsonObject searchMusicInfoFromQQ(String title, String current, String size){
        String[] cmd = new String[]{pythonInterpreter, qqSearchScriptPath, title, current, size};
        return getJsonObject(cmd);
    }

    public static JsonObject searchCompanyFromQQ(String albumId){
        String[] cmd = new String[]{pythonInterpreter, qqAlbumSearchScriptPath, albumId};
        return getJsonObject(cmd);
    }

    @Nullable
    private static JsonObject getJsonObject(String[] cmd) {
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
}