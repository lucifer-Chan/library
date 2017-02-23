package cn.ttsales.controller.common;

import cn.ttsales.config.CorpSetting;
import cn.ttsales.config.WxApiSetting;
import cn.ttsales.remote.WxApi;
import static cn.ttsales.util.CookieUtil.*;
import cn.ttsales.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Map;

/**
 * Created by 露青 on 2016/10/13.
 */
public abstract class CorpOauthController {
    private static final boolean DEBUG = true;

    private static final String debugUserId = "e165d04bf4394001a7a747e6497245ea";

    private static Logger log = LoggerFactory.getLogger(CorpOauthController.class);

    @Autowired
    private WxApiSetting wxApiSetting;

    @Autowired
    private CorpSetting corpSetting;

    @Autowired
    private WxApi wxApi;

    @RequestMapping("init")
    public String init(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> map)
            throws IOException {
        // 测试
        if (DEBUG) {
            map.put(KEY_USER_ID, debugUserId);
            addUserId2Cookie(response,debugUserId);
            return this.pageInit(request, response, map);
        }

        log.info("call commonController init");
        String userId = getUserIdFromCookie(request);
        //if (true) {
        if (null == userId) {
            String url = request.getRequestURL().toString();
            String queryString = request.getQueryString();
            if (!StringUtil.isEmpty(queryString))
                url = url + "?" + queryString;
            url = URLEncoder.encode(url.replaceAll("init", "oauthInit"), "utf-8");
            String toOAuthUrl = MessageFormat.format(wxApiSetting.getCode(), corpSetting.getId(), url, "code", "snsapi_base", "STATE");
            log.info("toOAuthUrl: " + toOAuthUrl);
            response.sendRedirect(toOAuthUrl);
            return null;
        }
        map.put(KEY_USER_ID, userId);
        return this.pageInit(request, response, map);
    }

    @RequestMapping("oauthInit")
    public String oauthInit(HttpServletRequest request,
                                  HttpServletResponse response, @RequestParam Map<String, String> map,
                                  @RequestParam String code, @RequestParam String state)
            throws IOException {
        log.info("call CorpOauthController oauthInit");
        log.info("code：" + code);
        log.info("state：" + state);
        String agentId = map.get("agentId");
        if (StringUtil.isEmpty(agentId))//未指定代理，则用controller指定的默认代理
            agentId = this.getAgentId();
        String userId = wxApi.getUserId(code, agentId);
        if (!StringUtil.isEmpty(userId))
            addUserId2Cookie(response,userId);
        map.put(KEY_USER_ID, userId);
        return this.pageInit(request, response, map);
    }

    protected abstract String pageInit(HttpServletRequest request,
                                             HttpServletResponse response, @RequestParam Map<String, String> map);

    protected abstract String getAgentId();
}
