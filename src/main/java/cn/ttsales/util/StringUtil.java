package cn.ttsales.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by 露青 on 2016/10/13.
 */
public class StringUtil {

    public static String value(String str){
        return null == str || "".equals(str) || "null".equals(str) ? null : str;
    }

    public static String valueOf(Object o){
        return null == o ? null : value(o.toString());
    }

    /**
     * 判断字对象是否为NULL 或者 “”
     *
     * @param object
     * @return 空：true 非空：false
     * @author dandyzheng
     * @date 2012-6-7
     */
    public static boolean isEmpty(Object object) {
        if(object==null) return true;
        if("".equals(object.toString())){return true;}
        return false;
    }

    /**
     * @param args
     * @author dandyzheng
     * @date 2012-6-7
     * @see
     */
    public static void main(String[] args) {
//		System.out.println(makeEOName("USER_NAME"));
        String s = "Hello!{0},my name is {1}, I am {2}";
        String ret = format(s, "Lily", "Lucy", 18);
        System.out.println(ret);

    }

    /**
     * 将java命名转换成数据库命名，例如：userNameDs--->USER_NAME_DS
     * @param key
     * @return
     * @author Administrator
     * @date 2012-8-8
     * @see
     */
    public static String makeDBName(String key) {
        if(isEmpty(key)){
            return null;
        }
        char[] chars = key.toCharArray();
        String result = "";
        for (int i=0;i<chars.length;i++) {
            char c = chars[i];
            if(c>='A' && c<='Z'){
                result += "_"+c;
            }else{
                result += c;
            }
        }
        return result.toUpperCase();
    }
    /**
     * 将java命名转换成数据库命名，例如：USER_NAME--->userName
     * @param key
     * @return
     * @author zheng shanwei
     * @date 2012-8-9
     * @see
     */
    public static String makeEOName(String key) {

        char[] chars = key.toLowerCase().toCharArray();
        String result = "";
        int i=0;
        while(i<chars.length){
            char c = chars[i];
            if(c=='_'){
                result += (chars[i+1]+"").toUpperCase();
                i++;
            }else{
                result += c;
            }
            i++;
        }
        return result;
    }

    /**
     * 从链接地址得到参数集合
     * @param url
     * 			bds/project/prestoreproject.xhtml?projectType=01&isDetail=01
     * @return
     * 			{projectType=01,isDetail=01}
     * @author xyg
     * @date 2013-2-20
     * @see
     */
    public static List<String> getParamFromUrl(String url){
        if(StringUtil.isEmpty(url) || url.indexOf("?")<0){
            return null;
        }
        String[] str = url.split("\\?");
        if(str.length<2){
            return null;
        }
        String paramStr = str[1];
        //得到参数字符串
        List<String> params = new ArrayList<String>();
        String[] paramArray = null;
        if(!StringUtil.isEmpty(paramStr) && paramStr.indexOf("&")>0){
            paramArray = paramStr.split("&");
            for (String param : paramArray) {
                if(param.indexOf("Id")<0){
                    //排除动态的参数
                    params.add(param);
                }
            }
        }else{
            if(paramStr.indexOf("Id")<0){
                params.add(paramStr);
            }
        }
        return params;
    }

    /**
     * 从链接地址获得资源路径
     * @param url
     * 			bds/project/prestoreproject.xhtml?projectType=01
     * @return
     * 			bds/project/prestoreproject.xhtml
     * @author xyg
     * @date 2013-2-20
     * @see
     */
    public static String getResouceAddrFromUrl(String url){
        if(StringUtil.isEmpty(url) || url.indexOf("?")<0){
            return url;
        }
        return url.substring(0, url.indexOf("?"));
    }


    /**
     *
     * @Title: getGUID
     * @Description: TODO 生成uuid{上传文件生成随机文件名}
     * @return String    返回类型
     * @throws
     */
    public static String getGUID(){
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 随机i位数
     * @param i
     * @return
     */
    public static String randomNum(int i) {
        return String.valueOf(Math.random()).substring(2, 2 + i);
    }

    public static String format(String content, Object ... f){
        return MessageFormat.format(content, f);
    }

}