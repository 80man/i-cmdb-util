package com.xtxb.cmdb.util;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月18日-下午4:48
 * <p>
 * <p>
 *  用于辅助CMDB管理的工具集的接口
 */
public interface Tools {

    /**
     * 执行工具的具体任务
     * @param args
     */
    public void work(String[] args);

    /**
     * 返回所需参说的说明，用于辅助提示工具的参数
     * @return
     */
    public String[] getParamList();
}
