package com.zihai.h2Client.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Map;

public class ExcelUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);
    private CellStyle style;

    public ExcelUtil(CellStyle style){
        this.style = style;
    }
    /**
     * @rowNum 起始行。
     * @param colNum 起始列
     * */
    public void printTable(Sheet sheet, JsonArray arr, int colNum, int rowNum){
        colNum--; //起始列 先减1。以后每用一列先加1。
        for(JsonElement ele : arr){
            int[] lastColNum_RowNum = printRow(sheet,ele,colNum,rowNum);
            rowNum = lastColNum_RowNum[1] +1;
        }
    }
    public int[] printRow(Sheet sheet, JsonElement obj, int colNum, int rowNum){
        int LastRowNum =rowNum+ShortOfnextRowNum(obj) - 1;
        //LOGGER.debug("LastRowNum:"+LastRowNum);
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
               // LOGGER.debug("cell Value:"+ key.getValue().getAsString());
                row.createCell(++colNum).setCellValue(key.getValue().getAsString());
                if (LastRowNum != rowNum) {
                    sheet.addMergedRegion(new CellRangeAddress(rowNum, LastRowNum, colNum, colNum));
                }
                row.getCell(colNum).setCellStyle(style);
            }
        }
        return new int[]{colNum,LastRowNum};
    }
    public int ShortOfnextRowNum(JsonElement obj){
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
    public static void main(String[] args) throws URISyntaxException, IOException {
        /*String jsontr = IOUtils.toString(ExcelUtil.class.getClassLoader().getResource("testJson.json").toURI());
        JsonArray json = new Gson().fromJson(jsontr,JsonArray.class);
        LOGGER.debug(json.toString());
        Workbook wb = new XSSFWorkbook(); //or new HSSFWorkbook();
        Sheet sheet = wb.createSheet();
        CellStyle style = wb.createCellStyle();
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        new ExcelUtil(style).printTable(sheet,json,5,5);
        try (OutputStream fileOut = new FileOutputStream("D:\\xssf-align.xlsx")) {
            wb.write(fileOut);
            wb.close();
        }catch (Exception e) {
            e.printStackTrace();
        }*/

        XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(new File("D://data (2).xlsx")));
        Sheet sheet = xwb.getSheetAt(0);
        for(int i=1;i<=sheet.getLastRowNum();i++){
            Row row = sheet.getRow(i);
            for(int j=0;j<row.getLastCellNum();j++){
                System.out.println(row.getCell(j));
            }
        }
    }
}
