package cn.ttsales.remote;

import cn.ttsales.util.BaseResult;
import cn.ttsales.util.ListUtil;;import java.text.MessageFormat;

/**
 * Created by 露青 on 2016/10/12.
 */

public class RemoteUtils {

    public static String doGet(String uri){
        try{
            return new HttpRequestProxy().postMultiParams(uri, null, null, HttpRequestProxy.encoding);
        }catch (Exception e){
            return new BaseResult("-1", e.toString()).toString();
        }
    }

    public static String doPost(String uri, String postData){
        try{
            return new HttpRequestProxy().doPostRequest(uri, postData, null, HttpRequestProxy.encoding);
        }catch (Exception e){
            return new BaseResult("-1", e.toString()).toString();
        }
    }

    public static void main(String [] args){
//        String uri = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token={0}&userid={1}";
//        String accessTokenUri = "http://uu.ttsales.cn/ttsales-wxs/ratan/accessToken";
//        String accessToken = doGet(accessTokenUri);
//        String userInfo = doGet(MessageFormat.format(uri, "daEcx4M_JF518xL22lw7TWAY1vTG09GqI84DZLNJuDdKa0agp9WmBj6d7l59ZIGS", "e165d04bf4394001a7a747e6497245ea"));
//        System.out.println(userInfo);
        java.util.List l = new java.util.ArrayList();
        l.add("1");
        l.add("2");
        l.add("ass");
        System.out.println(ListUtil.toString(l));
    }


}
