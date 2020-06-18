package com.xtxb.cmdb.util.sql;

import com.xtxb.cmdb.util.Tools;

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
        System.out.println(args[0]);
    }

    @Override
    public String[] getParamList() {
        return new String[]{"Excel完整路径(包含文件名)"};
    }
}
