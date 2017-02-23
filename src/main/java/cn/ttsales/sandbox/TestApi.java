package cn.ttsales.sandbox;

import cn.ttsales.remote.RemoteUtils;
import net.sf.json.JSONObject;

import java.text.MessageFormat;

/**
 * Created by 露青 on 2016/10/18.
 */
public class TestApi {
    private static final String corpid = "wx26498b8be3141e48";
    private static final String broker = "ratan";
    private static final String secret = "j7gV6jloBcFnCtGcXe1BEQpiLZjgInVOonCPFIx22x8";
    private static final String token = "RatanSalesQYH";
    private static final int smileAgentId = 59;
    private static final String basUri = "http://uu.ttsales.cn/ttsales-wxs";
    private static final String rootId = "143410631";

    private static final String departmentURI = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token={0}&id={1}";
    private static final String kfListURI = "https://api.weixin.qq.com/cgi-bin/customservice/getkflist?access_token={0}";
    public static void main(String [] args) {
//        System.out.println(getAccessToken2());
        System.out.println(getKfList());
//        System.out.println(getDepartments());
    }

    /**
     * 呼朋勾友
     * @return
     */
    private static String getAccessToken2(){
        String uri = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";
        uri = MessageFormat.format(uri, "wxe740117414544eda", "8cd25ee543f4025537661643cb0beae6");
        JSONObject json = JSONObject.fromObject(RemoteUtils.doGet(uri));
        return json.getString("access_token");
    }

    private static String  getAccessToken(){
        String uri = basUri + "/" + broker + "/accessToken";
        try{
            JSONObject json = JSONObject.fromObject(RemoteUtils.doGet(uri));
            return json.getString("access_token");
        } catch (Exception e){
            return null;
        }
    }

    private static String getDepartments(){
        String uri = MessageFormat.format(departmentURI, getAccessToken(), rootId);
        return RemoteUtils.doGet(uri);
    }

    private static String getKfList(){
        String uri = MessageFormat.format(kfListURI, getAccessToken2());
        return RemoteUtils.doGet(uri);
    }


}
