package com.wapchief.jpushim.framework.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by wapchief on 2017/9/25.
 */

public class Base64Utils {
        // 加密
        public static String getBase64(String str) {
            String result = "";
            if( str != null) {
                try {
                    result = new String(Base64.encode(str.getBytes("utf-8"), Base64.NO_WRAP),"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        // 解密
        public static String getFromBase64(String str) {
            String result = "";
            if (str != null) {
                try {
                    result = new String(Base64.decode(str, Base64.NO_WRAP), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }


}
