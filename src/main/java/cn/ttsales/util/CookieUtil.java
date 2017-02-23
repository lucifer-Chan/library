package cn.ttsales.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 露青 on 2016/10/13.
 */
public class CookieUtil {
    public static final int EXPIRY = 365*24*60*60;

    public static final String KEY_OPEN_ID = "openid";

    public static final String KEY_USER_ID = "userid";

    public static String getUserIdFromCookie(HttpServletRequest request){
        return getCookieValue(KEY_USER_ID, request);
    }

    public static void addUserId2Cookie(HttpServletResponse response, String userId){
        addCookie(KEY_USER_ID, userId, response);
    }

    public static String getOpenidFromCookie(HttpServletRequest request){
        return getCookieValue(KEY_OPEN_ID, request);
    }

    public static void addOpenid2Cookie(HttpServletResponse response, String openid){
        addCookie(KEY_OPEN_ID, openid, response);
    }

    public static void addCookie(String cookieName,String data,HttpServletResponse response){
        Cookie cookie = new Cookie(cookieName,data);
        cookie.setMaxAge(EXPIRY);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static String getCookieValue(String cookieName,HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String vlaue = null;
        if(cookies!=null){
            for (int i = 0; i < cookies.length; i++){
                Cookie c = cookies[i];
                if(c.getName().equalsIgnoreCase(cookieName)){
                    vlaue = c.getValue();
                    break;
                }
            }
        }
        return vlaue;
    }
}
