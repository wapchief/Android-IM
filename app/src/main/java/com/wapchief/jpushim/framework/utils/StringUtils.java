package com.wapchief.jpushim.framework.utils;

/**
 * Created by wapchief on 2017/8/1.
 */

public class StringUtils {

    //字符串判空
    public static Boolean isNull(String str){
        if (str.equals("")||str==null||str.equals("null")) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * 男=male
     * 女=female
     * 未知=unknown
     * @param s
     * @return 常量
     */
    public static String constant2String(String s){

        if (s=="unknown"){
            return "未知";
        }else if (s=="male"){
            return "男";
        }else {
            return "女";
        }
    }

    /**
     *
     * @param s
     * @return
     */
    public static String string2contant(String s){

        if (s=="男"||s.equals("男")){
            return "male";
        }else if (s=="女"||s.equals("女")){
            return "female";
        }else {
            return "unknown";
        }
    }
}
