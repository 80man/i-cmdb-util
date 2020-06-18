package com.xtxb.cmdb;

import com.xtxb.cmdb.util.Tools;
import com.xtxb.cmdb.util.ToolsFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月18日-下午4:28
 * <p>
 * <p>
 * 工具集入口
 */
public class App 
{
    public static void main( String[] args ) {

        System.out.println("当前支持的工具为："+Arrays.asList(ToolsFactory.getNames()));
        String name=null;
        try{
            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
            System.out.print("请输入工具名称: ");
            while((name=br.readLine())!=null){
                if(Arrays.binarySearch(ToolsFactory.getNames(),name)>=0)
                    break;
                else if(name.equals("C"))
                    System.exit(0);
                else
                    System.out.print("请输入正确的名称[退出:C]: ");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("==============开始使用 "+name+" 工具=================");
        try {
            Tools tool = ToolsFactory.getTools(name);
            String[] params = tool.getParamList();
            if (params == null || params.length == 0)
                tool.work(null);
            else {
                try  {
                    BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
                    int index = 0;
                    System.out.print(params[index]+":");
                    String line=null;
                    while ((line = br.readLine()) != null) {
                        params[index++]=line;
                        if(index==params.length)
                            break;
                    }
                    tool.work(params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
