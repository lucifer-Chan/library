package cn.ttsales.servlet;

import cn.ttsales.config.CorpSetting;
import cn.ttsales.core.util.DomUtil;
import cn.ttsales.remote.WxApi;
import cn.ttsales.remote.util.EventType;
import cn.ttsales.remote.util.MsgType;
import cn.ttsales.remote.util.Result;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by 露青 on 2016/10/13.
 */
@WebServlet("/qyCommonServlet")
public class QYCommonServlet extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(QYCommonServlet.class);

    @Autowired
    private CorpSetting corpSetting;

    @Autowired
    private WxApi wxApi;


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        log.info("QYCommonServlet doGet");
        // 微信加密签名
        String msgSignature = request.getParameter("msg_signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");
        try {
            System.out.println("{\"AESKEY\":"+ corpSetting.getAesKey() + ",\"TOKEN\":" + corpSetting.getToken() + ",\"APPID\":"+ corpSetting.getId() +"}");
            Result result = wxApi.verifySignature(echostr, corpSetting.getAesKey() , msgSignature, corpSetting.getToken(), timestamp, nonce, corpSetting.getId());
            PrintWriter out = response.getWriter();
            if (result.getCode() == 0 ) {
                out.print(result.getResult());
            } else {
                out.print("VerifySignature failed! error code:" + result.getCode());
                log.error("VerifySignature failed! error code:" + result.getCode());
            }
            out.close();
            out = null;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("QYCommonServlet doPost");
        // 微信加密签名
        String msgSignature = request.getParameter("msg_signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");

        BufferedReader reader = request.getReader();
        StringBuilder jb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null)
            jb.append(line);
        String fromXMLText = jb.toString();

        // 通过检验signature对请求进行校验
        PrintWriter out = response.getWriter();
        Result result = wxApi.fromTencent(fromXMLText, corpSetting.getAesKey() , msgSignature, corpSetting.getToken(), timestamp, nonce, corpSetting.getId());

        if (result.getCode() == 0) {// 消息解密正确
            String respMessage = "";
            Document document = DomUtil.loadDocument(result.getResult());
            Element root = document.getDocumentElement();
            List<Element> elementList = DomUtil.getChildElements(root);
            Map<String, String> requestMap = new HashMap<String, String>();
            for (Element e : elementList)
                requestMap.put(e.getNodeName(), e.getTextContent());
            String msgType = requestMap.get("MsgType");
            String event = requestMap.get("Event");
            out.print("");
            out.close();
            out=null;
            switch (MsgType.getByName(msgType)) {
                case IMAGE:
                    break;
                case LINK:
                    break;
                case LOCATION:
                    break;
                case MPNEWS:
                    break;
                case MUSIC:
                    break;
                case NEWS:
                    break;
                case TEXT:
                    break;
                case VIDEO:
                    break;
                case VOICE:
                    break;
                case EVENT: {
                    switch (EventType.getByName(event)) {
                        case CLICK:
                            //处理点击事件
                            dealClick(requestMap);
                            break;
                        case LOCATION:
                            break;
                        case LOCATION_SELECT:
                            break;
                        case SUBSCRIBE:{
                            //处理关注事件
                            dealSubscribe(requestMap);
                            break;
                        }
                        case UNSUBSCRIBE:{
                            //处理取消关注事件
                            dealUnSubscribe(requestMap);
                            break;
                        }
                        case VIEW: {
                            //处理点击菜单事件
                            break;
                        }
                        case EVENT_AGENT: {
                            //处理进入应用事件
                            dealEventAgent(requestMap);
                            break;
                        }
                        default:
                            break;
                    }
                    break;
                }
                default:
                    break;
            }

        } else {
            out.print("VerifySignature failed! error code:" + result.getCode());
            log.error("VerifySignature failed! error code:" + result.getCode());
        }
        if(out!=null){
            out.close();
            out=null;
        }
    }

    private void dealEventAgent(Map<String, String> requestMap) {
        System.out.println(JSONObject.fromObject(requestMap));
    }

    private void dealUnSubscribe(Map<String, String> requestMap) {
        System.out.println(JSONObject.fromObject(requestMap));
    }

    private void dealSubscribe(Map<String, String> requestMap) {
        System.out.println(JSONObject.fromObject(requestMap));
    }

    private void dealClick(Map<String,String> requestMap){
        System.out.println(JSONObject.fromObject(requestMap));
    }
}
