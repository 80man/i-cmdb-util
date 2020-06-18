package com.xtxb.cmdb.util.sql;

import com.xtxb.cmdb.util.Tools;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月18日-下午6:12
 * <p>
 * <p>
 * 创建存储模型信息的Excel模板
 */
public class ExcelTemplete implements Tools {
    @Override
    public void work(String[] args) {
        String fileName=null;
        if(args[0].endsWith("/")){
            fileName=args[0]+args[1];
        }else
            fileName=args[0]+ File.separator+args[1];
        writeFile(fileName,createExcel());

    }

    @Override
    public String[] getParamList() {
        return new String[]{"模板存放路径","模板名称"};
    }

    /**
     * 创建Excel
     * @return
     */
    private HSSFWorkbook createExcel(){
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheetc=workbook.createSheet("资源类型信息");
        HSSFSheet sheetp=workbook.createSheet("属性信息");

        initModel(sheetc);
        initProperty(sheetp);

        return workbook;
    }


    /**
     * 填充资源类型模板
     * @param sheet
     */
    private void initModel(HSSFSheet sheet){
        HSSFRow row= sheet.createRow(1);
        HSSFCellStyle style=getStyle(sheet.getWorkbook());
        HSSFCell cell=row.createCell(1);
        cell.setCellStyle(style);
        cell.setCellValue("模型名称");
        sheet.setColumnWidth(1,3000);
    }

    /**
     * 填充属性模板
     * @param sheet
     */
    private void initProperty(HSSFSheet sheet){

    }


    /**
     *  创建表格标题行样式
     * @param workbook
     * @return
     */
    private HSSFCellStyle getStyle(HSSFWorkbook workbook) {
        // 创建单元格样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        // 指定单元格居中对齐
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        // 指定单元格垂直居中对齐
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 指定当单元格内容显示不下时自动换行
        cellStyle.setWrapText(true);
        // 设置单元格字体
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontName("宋体");
        font.setFontHeight((short) 200);
        cellStyle.setFont(font);
        return cellStyle;
    }
    /**
     * Excel信息写入文件
     * @param fileName
     * @param workbook
     */
    private void writeFile(String fileName,HSSFWorkbook workbook){
        try(FileOutputStream fOut = new FileOutputStream(fileName)) {
            workbook.write(fOut);
            fOut.flush();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
