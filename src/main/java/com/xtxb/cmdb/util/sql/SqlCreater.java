package com.xtxb.cmdb.util.sql;

import com.xtxb.cmdb.util.Tools;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.*;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月18日-下午4:28
 * <p>
 * <p>
 * 用于将Excel中填写的模型信息，转换成SQL初始化脚本
 */
public class SqlCreater implements Tools {
    @Override
    public void work(String[] args) {
        HSSFWorkbook wb=getExcel(args[0]);
        if(wb==null)
            return;

        Map<String,String[]> models=getModels(wb.getSheetAt(0));
        if(models==null || models.size()==0)
            return;
        Map<String,String[]> properties=getProperties(wb.getSheetAt(1));


        //检查资源类型是否存在
        for (Iterator iterator = models.values().iterator(); iterator.hasNext(); ) {
            String[] model =  (String[])iterator.next();
            if(model[3]!=null && !model[3].equals("") && models.get(model[3])==null){
                System.out.println("第"+model[0]+"行关联的父模型中文名称不存在，请检查Excel并修复问题后导入");
                return;
            }
        }

        //检查属性关联的资源类型是否存在
        for (Iterator iterator = properties.values().iterator(); iterator.hasNext(); ) {
            String[] property =  (String[])iterator.next();
            if(models.get(property[3])==null){
                System.out.println("第"+property[0]+"行关联的父模型中文名称不存在，请检查Excel并修复问题后导入");
                return;
            }
        }

        writeSQLDDL(models,properties);
        writeSQLDML(models,properties);
    }

    /**
     * 从Excel文件读取表格信息
     * @param name
     * @return
     */
    private HSSFWorkbook getExcel(String name){
        HSSFWorkbook wb=null;
        try(FileInputStream is = new FileInputStream(name);){
            wb=new HSSFWorkbook(is);
        }catch(Exception e){
            e.printStackTrace();
        }
        return wb;
    }

    /**
     * 解析表格，获取所有的模型信息
     * @param sheet
     * @return
     */
    private Map<String,String[]> getModels(HSSFSheet sheet){
        Map<String,String[]> models=new HashMap<>();
        HSSFRow row=null;
        int i=1;
        while((row=sheet.getRow(i++))!=null){
            String cnName=getCellValue(row.getCell(0));
            String enName=getCellValue(row.getCell(1));
            String pName=getCellValue(row.getCell(2));

            if(isNull(cnName) && isNull(enName) && isNull(pName)){
                return models;
            }else if(isNull(cnName) || isNull(enName)){
                System.out.println("第"+i+"行有未填写的信息，请检查Excel并修复问题后导入");
                return null;
            }

            if(models.containsKey(cnName)){
                System.out.println("第"+i+"行中文名称与第"+models.get(cnName)[0]+"行重复，请检查Excel并修复问题后导入");
                return null;
            }
            models.put(cnName,new String[]{""+i,cnName,enName,pName});
        }

        return models;
    }

    /**
     * 解析表格，获取所有的属性信息
     * @param sheet
     * @return
     */
    private Map<String,String[]> getProperties(HSSFSheet sheet){
        Map<String,String[]> properties=new HashMap<>();
        HSSFRow row=null;
        int i=1;
        while((row=sheet.getRow(i++))!=null){
            String cnName=getCellValue(row.getCell(0));
            String enName=getCellValue(row.getCell(1));
            String pName=getCellValue(row.getCell(2));
            String group=getCellValue(row.getCell(3));
            String type=getCellValue(row.getCell(4));
            String defValue=getCellValue(row.getCell(5));
            String matchRuleType=getCellValue(row.getCell(6));
            String matchRuleValue=getCellValue(row.getCell(7));

            if(isNull(cnName) && isNull(enName) && isNull(pName) && isNull(group) && isNull(type) && isNull(defValue) && isNull(matchRuleType) && isNull(matchRuleValue)){
                return properties;
            }else if(isNull(cnName) || isNull(enName) || isNull(pName) || isNull(group) || isNull(type)){
                System.out.println("第"+i+"行有未填写的信息，请检查Excel并修复问题后导入");
                return null;
            }

            if(properties.containsKey(cnName) && pName.equals(properties.get(cnName)[3])){
                System.out.println("第"+i+"行中文名称与第"+properties.get(cnName)[0]+"行重复，请检查Excel并修复问题后导入");
                return null;
            }
            properties.put(cnName,new String[]{""+i,cnName,enName,pName,group,type,defValue,matchRuleType,matchRuleValue});
        }
        return properties;
    }

    /**
     * 创建DML脚本
     * @param models
     * @param properties
     */
    private void writeSQLDML(Map<String,String[]> models,Map<String,String[]> properties){
        StringBuilder sb=new StringBuilder();
        for (Iterator iterator = models.values().iterator(); iterator.hasNext(); ) {
            String[] model =  (String[])iterator.next();
            if(model[3]!=null && !model[3].equals(""))
                sb.append("insert into  M_META values('"+model[2]+"','"+model[1]+"','"+models.get(model[3])[2]+"');\n");
            else
                sb.append("insert into  M_META values('"+model[2]+"','"+model[1]+"',NULL);\n");
        }


        sb.append("\n");

        for (Iterator iterator = properties.values().iterator(); iterator.hasNext(); ) {
            String[] property =  (String[])iterator.next();
            String pname=models.get(property[3])[2];
            int type=1;
            if(property[5].equals("字符串")){
                type=1;
            }else if(property[5].equals("整型")){
                type=2;
            }else if(property[5].equals("浮点型")){
                type=3;
            }else if(!property[5].equals("时间型")){
                type=4;
            }else if(!property[5].equals("日期型")){
                type=5;
            }else if(!property[5].equals("日期时间型")){
                type=6;
            }

            int rtype=1;
            if(property[7].equals("值域")){
                rtype=1;
            }else if(property[7].equals("正则")){
                rtype=2;
            }else{
                rtype = 3;
            }
            sb.append("insert into  P_META values('"+property[2]+"','"+property[1]+"','"+pname+"','"+property[4]+"',"+type+"," +
                    ((property[6]==null || property[6].equals(""))?"NULL,":("'"+property[6]+"',")) +
                    rtype+"," +
                    ((property[8]==null || property[8].equals(""))?"NULL":("'"+property[8]+"'"))  +
                    ");\n");
        }
        try(
                BufferedWriter bw=new BufferedWriter(new FileWriter(System.getProperty("user.dir")+"/DML.sql"))
        ){
            bw.write(sb.toString());
            bw.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 创建DDL脚本
     * @param models
     * @param properties
     */
    private void writeSQLDDL(Map<String,String[]> models,Map<String,String[]> properties){

        List<String> temp=null;
        StringBuilder sb=new StringBuilder();
        sb.append("CREATE TABLE M_META (\n");
        sb.append("ENNAME varchar(32),\n");
        sb.append("CNNANE varchar(32),\n");
        sb.append("PNANE varchar(32)\n");
        sb.append(");\n ");

        sb.append("CREATE TABLE P_META (\n");
        sb.append("ENNAME varchar(32),\n");
        sb.append("CNNANE varchar(100),\n");
        sb.append("PNANE varchar(32),\n");
        sb.append("PGROUP varchar(100),\n");
        sb.append("PTYPE numeric(1),\n");
        sb.append("DEFVALUE varchar(200),\n");
        sb.append("MATCHRULE numeric(1),\n");
        sb.append("MATCHRULEVALUE varchar(200)\n");
        sb.append(");\n");

        for (Iterator iterator = models.values().iterator(); iterator.hasNext(); ) {
            temp=new ArrayList<>(4);
            String[] model =  (String[])iterator.next();
            getParent(model[1],models,temp);
            String tname=model[2].toUpperCase();
            if(!model[2].startsWith("C_"))
                tname="C_"+model[2].toUpperCase();

            sb.append("CREATE TABLE "+tname +" (\n");
            sb.append("P_OID numeric(20)  not null primary key,\n");
            sb.append("P_SID varchar(32) ");
            for (Iterator<String[]> Iterator2 = properties.values().iterator(); Iterator2.hasNext(); ) {
                String[] property =  Iterator2.next();
                if(temp.contains(property[3])){
                    String cName=property[2].toLowerCase();
                    if(!cName.startsWith("p_")){
                        cName="P_"+cName;
                    }

                    String type="varchar(200)";
                    if(property[5].equals("整型")){
                        type="numeric(20)";
                    }else if(property[5].equals("浮点型")){
                        type="numeric(20,2)";
                    }else if(!property[5].equals("字符串")){
                        type="numeric(20)";
                    }

                    sb.append(",\n"+cName+"  "+type);
                }
            }

            sb.append("\n);\n");
            sb.append("ALTER TABLE "+tname+" ADD INDEX "+tname+"_IND_P_SID (P_SID);\n\n");
        }

        try(
                BufferedWriter bw=new BufferedWriter(new FileWriter(System.getProperty("user.dir")+"/DDL.sql"))
        ){
            bw.write(sb.toString());
            bw.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getParent(String cnName , Map<String,String[]> models, List<String> list){
        list.add(cnName);
        String[] model=models.get(cnName);
        if(model==null)
            return;
        if(model[3]!=null && !model[3].trim().equals("")){
            getParent(model[3],models,list);
        }
    }

    private String getCellValue(HSSFCell cell){
        if(cell==null || cell.getRichStringCellValue()==null)
            return null;
       return cell.getRichStringCellValue().getString().trim();
    }

    private  boolean isNull(String value){
        if(value==null || value.trim().equals(""))
            return true;
        else
            return false;
    }

    @Override
    public String[] getParamList() {
        return new String[]{"Excel完整路径(包含文件名)"};
    }
}
