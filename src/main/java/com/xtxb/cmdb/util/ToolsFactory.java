package com.xtxb.cmdb.util;

import com.xtxb.cmdb.util.sql.ExcelTemplete;
import com.xtxb.cmdb.util.sql.SqlCreater;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月18日-下午4:55
 * <p>
 * <p>
 * TODO
 */
public class ToolsFactory {
    private static Map<String,String> namesMap=new HashMap<>();

    private static Map<String,Class> claMap=new HashMap<>();

    static{
        namesMap.put("sql","将Excel中的模型信息转换成SQL脚本");
        namesMap.put("excel","生成用于整理i-CMDB模型的Excel模板");
        claMap.put("sql", SqlCreater.class);
        claMap.put("excel", ExcelTemplete.class);
    }

    public static Map<String,String> getNames(){
        return namesMap;
    }

    public static Tools getTools(String name) throws Exception{
        return (Tools)claMap.get(name).newInstance();
    }
}
