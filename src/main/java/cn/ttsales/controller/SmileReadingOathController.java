package cn.ttsales.controller;

import cn.ttsales.config.CorpSetting;
import cn.ttsales.controller.common.CorpOauthController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by 露青 on 2016/10/13.
 */
@Controller
public class SmileReadingOathController extends CorpOauthController {
    @Autowired
    private CorpSetting corpSetting;

    @Override
    protected String pageInit(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> map) {
//        return map.get("direct").replace(".", "/");
//        return "index.html#/" + map.get("direct");
        return "redirect:/index.html#/" + map.get("direct");
    }

    @Override
    protected String getAgentId() {
        return corpSetting.getSmileAgentId();
    }
}
