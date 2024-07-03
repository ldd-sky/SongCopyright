package com.ktvme.songcopyright.utils;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.ktvme.songcopyright.model.bo.SongBO;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 歌曲excel处理工具</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年06月28日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
@Slf4j
@UtilityClass
public class SongExcelUtil {

    @Getter
    private List<SongBO> songList = new ArrayList<>();

    /**
     * 开始解析excel
     * @param excelFile 要解析的文件
     */
    public void doRead(MultipartFile excelFile){
        songList.clear();
        InputStream inputStream;
        try {
            inputStream = excelFile.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
            doReadWorkbook(workbook, excelFile);
        } catch (IOException e) {
            log.error("[ERR] ----> IO异常: ", e);
            throw new RuntimeException(e);
        }
    }

    private void doReadWorkbook(Workbook workbook, final MultipartFile excel) throws IOException {
        int numberOfSheets = workbook.getNumberOfSheets();
        log.info("[INF] ---> start handle sheets: {}", numberOfSheets);

        for (int i=0; i<numberOfSheets; i++){
            Sheet sheet = workbook.getSheetAt(i);
            String sheetName = sheet.getSheetName().trim();
            log.info("[INF] ---> handle sheet: {}", sheetName);

            ExcelUtil.readBySax(excel.getInputStream(), i,
                    createRowHandler(songList));
        }

        log.info("[INF] ---> 处理完了excel: {}", songList);
    }

    private RowHandler createRowHandler(List<SongBO> list){
        return new RowHandler() {
            @Override
            public void handle(int sheetIndex, long rowIndex, List<Object> rowlist) {
                log.info("[INF] ---> [{}] [{}] {}", sheetIndex, rowIndex, rowlist);
                SongBO song = SongBO.builder()
                        .songTitle(rowlist.get(0).toString())
                        .artist(rowlist.get(1).toString())
                        .build();
                list.add(song);
            }

            @Override
            public void doAfterAllAnalysed() {
                log.info("[INF] ---> all file has been analysed");

            }
        };
    }
}