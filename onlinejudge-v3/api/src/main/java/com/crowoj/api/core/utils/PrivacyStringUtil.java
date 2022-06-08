package com.crowoj.api.core.utils;
 
import cn.hutool.core.util.StrUtil;

public class PrivacyStringUtil {
 
    private static final String OVERLAY = "****";
    private static final int START = 3;
    private static final int END = 7;


    private static int indexOf(CharSequence cs, CharSequence searchChar, int start) {
        return cs.toString().indexOf(searchChar.toString(), start);
    }

    private static int indexOf(CharSequence seq, CharSequence searchSeq) {
        return seq != null && searchSeq != null ? indexOf(seq, searchSeq, 0) : -1;
    }

    private static String overlay(String str,String overlay,int start,int end){
        if (str == null) {
            return null;
        } else {
            if (overlay == null) {
                overlay = "";
            }

            int len = str.length();
            if (start < 0) {
                start = 0;
            }

            if (start > len) {
                start = len;
            }

            if (end < 0) {
                end = 0;
            }

            if (end > len) {
                end = len;
            }

            if (start > end) {
                int temp = start;
                start = end;
                end = temp;
            }

            return str.substring(0, start) + overlay + str.substring(end);
        }
    }

    private static String substring(String str, int start) {
        if (str == null) {
            return null;
        } else {
            if (start < 0) {
                start += str.length();
            }

            if (start < 0) {
                start = 0;
            }

            return start > str.length() ? "" : str.substring(start);
        }
    }

    private static String substring(String str, int start, int end) {
        if (str == null) {
            return null;
        } else {
            if (end < 0) {
                end += str.length();
            }

            if (start < 0) {
                start += str.length();
            }

            if (end > str.length()) {
                end = str.length();
            }

            if (start > end) {
                return "";
            } else {
                if (start < 0) {
                    start = 0;
                }

                if (end < 0) {
                    end = 0;
                }

                return str.substring(start, end);
            }
        }
    }
    /**
     * 135****0169
     *
     * @param content
     * @return
     */
    public static String maskMobile(String content) {
        if (StrUtil.isEmpty(content)) {
            return "";
        }
        return overlay(content, OVERLAY, START, END);
    }
 
    /**
     * 过滤邮箱账号
     * 13234234****@163.com
     *
     * @param email
     * @return
     */
    public static String maskEmail(String email) {
        if (StrUtil.isEmpty(email)) {
            return "";
        }
        String at = "@";
        if (!email.contains(at)) {
            return email;
        }
        /**
         * 这里主要逻辑是需要保留邮箱的注册商 比如@qq.com
         */
        int length = indexOf(email, at);
        String content = substring(email, 0, length);
        String mask = overlay(content, OVERLAY, START, END);
        return mask + substring(email, length);
    }
 
    /**
     * 身份证打码操作
     * 420****99126548631
     * @param idCard
     * @return
     */
    public static String maskIdCard(String idCard) {
        if (StrUtil.isEmpty(idCard)) {
            return "";
        }
        return overlay(idCard, OVERLAY, START, END);
    }
 
 
}
