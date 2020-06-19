package com.xtxb.cmdb.util.sql;

import com.xtxb.cmdb.util.Tools;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddressList;

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
        System.out.println("开始生成模板");
        String fileName=null;
        if(args[0].endsWith("/")){
            fileName=args[0]+args[1];
        }else
            fileName=args[0]+ File.separator+args[1];
        writeFile(fileName,createExcel());
        System.out.println("生成模板完成!");
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
        HSSFRow row= sheet.createRow(0);
        HSSFCellStyle style=getStyle(sheet.getWorkbook());

        HSSFCell cell=row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue("中文名称");
        sheet.setColumnWidth(0,3000);

        cell=row.createCell(1);
        cell.setCellStyle(style);
        cell.setCellValue("英文名称");
        sheet.setColumnWidth(1,3000);

        cell=row.createCell(2);
        cell.setCellStyle(style);
        cell.setCellValue("父模型中文名称");
        sheet.setColumnWidth(2,5000);

        style=getDataStyle(sheet.getWorkbook());
        row= sheet.createRow(1);
        cell=row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue("样例:虚拟机");

        cell=row.createCell(1);
        cell.setCellStyle(style);
        cell.setCellValue("C_VM");

        cell=row.createCell(2);
        cell.setCellStyle(style);
        cell.setCellValue("服务器");
    }

    /**
     * 填充属性模板
     * @param sheet
     */
    private void initProperty(HSSFSheet sheet){
        HSSFRow row= sheet.createRow(0);
        HSSFCellStyle style=getStyle(sheet.getWorkbook());

        HSSFCell cell=row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue("中文名称");
        sheet.setColumnWidth(0,3000);

        cell=row.createCell(1);
        cell.setCellStyle(style);
        cell.setCellValue("英文名称");
        sheet.setColumnWidth(1,3000);

        cell=row.createCell(2);
        cell.setCellStyle(style);
        cell.setCellValue("关联模型的中文名称");
        sheet.setColumnWidth(2,5000);

        cell=row.createCell(3);
        cell.setCellStyle(style);
        cell.setCellValue("属性组");
        sheet.setColumnWidth(3,3000);

        cell=row.createCell(4);
        cell.setCellStyle(style);
        cell.setCellValue("属性类型");
        sheet.setColumnWidth(4,3000);

        cell=row.createCell(5);
        cell.setCellStyle(style);
        cell.setCellValue("默认值");
        sheet.setColumnWidth(5,3000);

        cell=row.createCell(6);
        cell.setCellStyle(style);
        cell.setCellValue("校验规则");
        sheet.setColumnWidth(6,3000);

        cell=row.createCell(7);
        cell.setCellStyle(style);
        cell.setCellValue("校验值");
        sheet.setColumnWidth(7,8000);

        // 加载下拉列表内容
        DVConstraint constraint = DVConstraint
                .createExplicitListConstraint(new String[] {"字符串","整型","浮点型","时间型","日期型","日期时间型"});
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(1,
                100, 4, 4);
        // 数据有效性对象
        HSSFDataValidation data_validation_list = new HSSFDataValidation(
                regions, constraint);
        sheet.addValidationData(data_validation_list);

        constraint = DVConstraint
                .createExplicitListConstraint(new String[] {"值域","正则","引用"});
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        regions = new CellRangeAddressList(1,
                100, 6, 6);
        // 数据有效性对象
        data_validation_list = new HSSFDataValidation(
                regions, constraint);
        sheet.addValidationData(data_validation_list);


        style=getDataStyle(sheet.getWorkbook());
        row= sheet.createRow(1);
        cell=row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue("样例:CPU位数");

        cell=row.createCell(1);
        cell.setCellStyle(style);
        cell.setCellValue("cpubit");

        cell=row.createCell(2);
        cell.setCellStyle(style);
        cell.setCellValue("虚拟机");

        cell=row.createCell(3);
        cell.setCellStyle(style);
        cell.setCellValue("基本信息");

        cell=row.createCell(4);
        cell.setCellStyle(style);
        cell.setCellValue("整型");

        cell=row.createCell(5);
        cell.setCellStyle(style);
        cell.setCellValue("64");


        cell=row.createCell(6);
        cell.setCellStyle(style);
        cell.setCellValue("值域");

        cell=row.createCell(7);
        cell.setCellStyle(style);
        cell.setCellValue("32|64，属性值校验规则值，枚举：以 | 分割的多个值； 正则：正则表达式；引用： 关联资源类型的名称");
    }


    /**
     *  创建表格标题行样式
     * @param workbook
     * @return
     */
    private HSSFCellStyle getStyle(HSSFWorkbook workbook) {
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontName("宋体");
        font.setFontHeight((short) 200);
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     *  创建表格标题行样式
     * @param workbook
     * @return
     */
    private HSSFCellStyle getDataStyle(HSSFWorkbook workbook) {
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(true);
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeight((short) 200);
        font.setColor(IndexedColors.RED.getIndex());
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
