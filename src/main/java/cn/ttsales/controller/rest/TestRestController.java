package cn.ttsales.controller.rest;

import cn.ttsales.dao.WxTeacherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by 露青 on 2016/9/27.
 */
@RestController
@RequestMapping("test")
public class TestRestController {

    @Autowired
    private WxTeacherRepository wxTeacherRepository;

    @RequestMapping("/native")
    public List<Object []> testNative(){

        List<Object []> ret = wxTeacherRepository.findNative();
        //[ [ 11, "露青.陈", "chenluqing@ttsales.cn" ], [ 12, "xxx", "xxxx" ] ]
        return ret;
    }


    private static Logger logger = LoggerFactory.getLogger(TestRestController.class);

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @RequestMapping("/index")
    public String index(){
        return "Hello~ Spring boot";
    }



}
