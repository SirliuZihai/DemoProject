package com.zihai.h2Client.test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Created by lylhjh on 2020-05-16.
 * 本方法用于 数据库一对多联表查询数据导出Excel —> 先输出成json -->再输出excel格式。
 */
public class ExcelTest {
    private static final Logger LOGGER = Logger.getLogger(ExcelTest.class);
    private static CellStyle style;

    public static void main(String[] args) throws URISyntaxException, IOException {
        String jsontr =IOUtils.toString(ExcelTest.class.getClassLoader().getResource("testJson.json").toURI());
        JsonArray json = new Gson().fromJson(jsontr,JsonArray.class);
        LOGGER.debug(json.toString());
        Workbook wb = new XSSFWorkbook(); //or new HSSFWorkbook();
        Sheet sheet = wb.createSheet();
        style = wb.createCellStyle();
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        printTable(sheet,json,5,5);
        try (OutputStream fileOut = new FileOutputStream("D:\\xssf-align.xlsx")) {
            wb.write(fileOut);
            wb.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * @rowNum 起始行。
     * @param colNum 起始列
     * */
    public static void printTable(Sheet sheet,JsonArray arr,int colNum,int rowNum){
        colNum--; //起始列 先减1。以后每用一列先加1。
        for(JsonElement ele : arr){
            int[] lastColNum_RowNum = printRow(sheet,ele,colNum,rowNum);
            rowNum = lastColNum_RowNum[1] +1;
        }
    }
    public static int[] printRow(Sheet sheet,JsonElement obj,int colNum,int rowNum){
        int LastRowNum =rowNum+ShortOfnextRowNum(obj) - 1;
        LOGGER.debug("LastRowNum:"+LastRowNum);
        Row row;
        for(Map.Entry<String, JsonElement>key:obj.getAsJsonObject().entrySet()) {
            if (key.getValue().isJsonArray() && key.getValue().getAsJsonArray().size() > 0) {
                int rowNum2 = rowNum;
                int[] lastColNum_RowNum = new int[2];
                for (JsonElement ele : key.getValue().getAsJsonArray()) {
                    lastColNum_RowNum= printRow(sheet, ele, colNum, rowNum2);
                    rowNum2 = lastColNum_RowNum[1] +1;
                }
                colNum = lastColNum_RowNum[0];
            } else if (key.getValue().isJsonPrimitive()) {
                if (sheet.getRow(rowNum) == null) {
                    row = sheet.createRow(rowNum);
                } else {
                    row = sheet.getRow(rowNum);
                }
                LOGGER.debug("cell Value:"+ key.getValue().getAsString());
                row.createCell(++colNum).setCellValue(key.getValue().getAsString());
                if (LastRowNum != rowNum) {
                    sheet.addMergedRegion(new CellRangeAddress(rowNum, LastRowNum, colNum, colNum));
                }
                 row.getCell(colNum).setCellStyle(style);
            }
        }
        return new int[]{colNum,LastRowNum};
    }
    public static int ShortOfnextRowNum(JsonElement obj){
        int i = 1;
        boolean hasArray = false;
        if(obj.isJsonObject()){
            for(Map.Entry<String, JsonElement>key:obj.getAsJsonObject().entrySet()){
                if(key.getValue().isJsonArray()&&key.getValue().getAsJsonArray().size()>0){
                    hasArray = true;
                    for(JsonElement ele: key.getValue().getAsJsonArray()){
                        i += ShortOfnextRowNum(ele);
                    }
                }
            }
        }
        if(obj.isJsonArray()){
            for(JsonElement ele: obj.getAsJsonArray()){
                i += ShortOfnextRowNum(ele);
            }
        }
        return hasArray?i-1:i;
    }
}
