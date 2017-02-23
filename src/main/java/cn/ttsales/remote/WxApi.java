package cn.ttsales.remote;

import cn.ttsales.config.CorpSetting;
import cn.ttsales.config.WxApiSetting;
import cn.ttsales.remote.util.Prpcrypt;
import cn.ttsales.remote.util.Result;
import cn.ttsales.remote.util.SHA1;
import cn.ttsales.remote.util.XMLParse;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * Created by 露青 on 2016/10/12.
 */
@Component
public class WxApi {
    @Autowired
    private CorpSetting corpSetting;

    @Autowired
    private WxApiSetting wxApiSetting;

    /**
     *
     * @return accessToken
     */
    public String getAccessToken(){
        String uri = wxApiSetting.getBaseUrl() + "/" + corpSetting.getBroker() + "/accessToken";
        //return RemoteUtils.doGet(uri);
        try{
            JSONObject json = JSONObject.fromObject(RemoteUtils.doGet(uri));
            return json.getString("access_token");
        } catch (Exception e){
            return null;
        }
    }

    /**
     *
     * @param id
     * @return 部门列表
     */
    public String getDepartmentList(String id){
        String uri = MessageFormat.format(wxApiSetting.getDepartmentList(), getAccessToken(), id);
        return RemoteUtils.doGet(uri);
    }

    /**
     *
     * @param code
     * @param agentId
     * @return
     */
    public String getUserId(String code, String agentId){
        String uri = MessageFormat.format(wxApiSetting.getUserinfo(), getAccessToken(), code, agentId);
        System.out.println("In WxApi's getUserId");
        System.out.println("uri:" + uri);
        try{
            JSONObject json =  JSONObject.fromObject(RemoteUtils.doGet(uri));
            System.out.println("the result is blow:");
            System.out.println(json);
            return json.getString("UserId");
        }catch (Exception e){
            return null;
        }
    }

    public JSONObject getUserInfo(String userId){
        //
//        https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token={0}&userid={1}
        String uri = MessageFormat.format(wxApiSetting.getUser(), getAccessToken(), userId);
        System.out.println("in getUserInfo :" + uri);
        try{
            return JSONObject.fromObject(RemoteUtils.doGet(uri));
        } catch(Exception e){
            return new JSONObject();
        }
    }

    /**
     * 公众平台发送消息给第三方 1. 利用收到的密文生成安全签名,进行签名验证; 2. 若验证通过，则提取xml中的加密消息； 3. 对消息进行解密。
     *
     */
    public Result verifySignature(String echostr, String encodingAesKey, String msgSignature,
                                  String token, String timestamp, String nonce, String appid) {

        if (encodingAesKey.length() != 43) {
            return new Result(-40004, "");
        }

        // 密钥，公众账号的app secret
        Prpcrypt pc = new Prpcrypt(encodingAesKey);

        // 验证安全签名
        Object[] signature = SHA1.getSHA1(token, timestamp, nonce, echostr);
        if ((Integer) signature[0] != 0) {
            return new Result((Integer) signature[0], "");
        }

        // 和URL中的签名比较是否相等
        // System.out.println("第三方收到URL中的签名：" + msg_sign);
        // System.out.println("第三方校验签名：" + signature);
        if (!signature[1].equals(msgSignature)) {
            // System.out.println("签名错误 ");
            /* 不安全消息处理 */
            signature[0] = -40001;
            signature[1] = "";
            return new Result((Integer) signature[0], "");
        }
        // 解密
        Result result = pc.decrypt(echostr, appid);
        return result;
    }

    /**
     * 回复消息给公众平台 1. 对要发送的消息进行AES-CBC加密； 2. 生成安全签名； 3. 将消息密文和安全签名打包成xml格式。
     *
     * @param text 第三方需要发送的明文
     * @return 返回码和第三方发给公众平台的xml消息
     */
    public static Result toTencent(String text, String encodingAesKey, String nonce, String token,
                                   String appid, String timestamp) {
        Prpcrypt pc = new Prpcrypt(encodingAesKey);

        // 加密
        Result encrypt = pc.encrypt(text, appid);
        if (encrypt.getCode() != 0) {
            return new Result(encrypt.getCode(), encrypt.getResult());
        }

        // 生成安全签名
        if (timestamp == "") {
            timestamp = Long.toString(System.currentTimeMillis());
        }

        Object[] signature = SHA1.getSHA1(token, timestamp, nonce, encrypt.getResult());
        if ((Integer) signature[0] != 0) {
            return new Result(encrypt.getCode(), "");
        }

        // System.out.println("发送给平台的签名是: " + signature[1].toString());
        // 生成发送的xml
        String result = XMLParse.generate(encrypt.getResult(), signature[1].toString(), timestamp,
                nonce);
        return new Result(0, result);
    }

    /**
     * 公众平台发送消息给第三方 1. 利用收到的密文生成安全签名,进行签名验证; 2. 若验证通过，则提取xml中的加密消息； 3. 对消息进行解密。
     *
     * @param text     第三方收到的xml格式加密消息
     * @param msgSignature 第三方收到的签名
     * @throws Exception
     */
    public Result fromTencent(String text, String encodingAesKey, String msgSignature,
                              String token, String timestamp, String nonce, String appid) {

        if (encodingAesKey.length() != 43) {
            return new Result(-40004, "");
        }

        // 密钥，公众账号的app secret
        Prpcrypt pc = new Prpcrypt(encodingAesKey);
        // 提取密文
        Object[] encrypt = XMLParse.extract(text);
        if ((Integer) encrypt[0] != 0) {
            return new Result((Integer) encrypt[0], "");
        }

        // 验证安全签名
        Object[] signature = SHA1.getSHA1(token, timestamp, nonce, encrypt[1].toString());
        if ((Integer) signature[0] != 0) {
            return new Result((Integer) signature[0], "");
        }

        // 和URL中的签名比较是否相等
        // System.out.println("第三方收到URL中的签名：" + msg_sign);
        // System.out.println("第三方校验签名：" + signature);
        if (!signature[1].equals(msgSignature)) {
            // System.out.println("签名错误 ");
            /* 不安全消息处理 */
            signature[0] = -40001;
            signature[1] = "";
            return new Result((Integer) signature[0], "");
        }
        // 解密
        Result result = pc.decrypt(encrypt[1].toString(), appid);
        return result;
    }

}
