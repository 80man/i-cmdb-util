package com.xtxb.cmdb.util;

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
    private static String[] names=new  String[]{"sql"};

    private static Map<String,Class> claMap=new HashMap<>();

    static{
        claMap.put("sql", SqlCreater.class);
    }

    public static String[] getNames(){
        return names;
    }

    public static Tools getTools(String name) throws Exception{
        return (Tools)claMap.get(name).newInstance();
    }
}
