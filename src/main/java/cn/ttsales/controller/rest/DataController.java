package cn.ttsales.controller.rest;

import cn.ttsales.config.CorpSetting;
import cn.ttsales.dao.RedisTeacherDao;
import cn.ttsales.dao.TBookRepository;
import cn.ttsales.domain.TBook;
import cn.ttsales.domain.WxTeacher;
import cn.ttsales.dao.WxTeacherRepository;
import cn.ttsales.remote.WxApi;
import cn.ttsales.service.WxTeacherService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by 露青 on 2016/9/29.
 */
@RestController
@RequestMapping("_teacher")
public class DataController {
    @Autowired
    WxTeacherRepository wxTeacherRespository;

    @Autowired
    WxTeacherService wxTeacherService;

    @Autowired
    TBookRepository tBookRepository;

    @Autowired
    RedisTeacherDao redisTeacherDao;

    @Autowired
    CorpSetting corpSetting;

    @Autowired
    WxApi wxApi;

    @RequestMapping("/redis/setStr")
    public void setString2Redis(String key, String value){
        System.out.println(key);
        redisTeacherDao.saveString(key, value);
    }

    @RequestMapping("/redis/getStr")
    public String getStringFromRedis(String key){
        return redisTeacherDao.getString(key);
    }

    @RequestMapping("/redis/setTeacher")
    public void setTeacher(long id){
        redisTeacherDao.saveWxTeacher(wxTeacherRespository.getOne(id));
    }
    @RequestMapping("/redis/getTeacher")
    public WxTeacher getTeacher(long id){
        return redisTeacherDao.getWxTeacher(id);
    }


    //测试缓存
    @RequestMapping("nickname")
    public List<WxTeacher> findByName(String name){
        return wxTeacherService.findByName(name);
    }

    @RequestMapping("save")
    public WxTeacher save(String name, String openid){
        WxTeacher t = new WxTeacher();
        t.setOpenid(openid);
        return wxTeacherRespository.save(t);
    }

//    /**
//     * find By nickname
//     * @return
//     */
//    @RequestMapping("q1")
//    public List<WxTeacher> q1(String name){
//        return wxTeacherRespository.findByNickname(name);
//    }
//
//    /**
//     *
//     * @param name
//     * @param city
//     * @return
//     */
//    @RequestMapping("q2")
//    public List<WxTeacher> q2(String name, String city){
//        return wxTeacherRespository.findByNicknameAndCity(name, city);
//    }
//
//    @RequestMapping("q3")
//    public List<WxTeacher> q3(String name, String province){
//        return wxTeacherRespository.withNameAndProvinceQuery(name, province);
//    }
//
//    @RequestMapping("q4")
//    public List<WxTeacher> q4(){
//        return wxTeacherRespository.hasCityNamedQuery();
//    }

    @RequestMapping("sort")
    public List<WxTeacher> sort(){
        return wxTeacherRespository.findAll(new Sort(Sort.Direction.ASC, "nickname"));
    }

    @RequestMapping("page")
    public Page<WxTeacher> page(){
        Page<WxTeacher> ret = wxTeacherRespository.findAll(new PageRequest(0,15, new Sort(Sort.Direction.ASC, "nickname")));
//        Page<WxTeacher> ret = wxTeacherRespository.findAll(new PageRequest(1,15));
        return ret;
    }

    @RequestMapping("corp/info")
    public String corpInfo(){
        return corpSetting.toString();
    }


    @RequestMapping("initBook")
    public void initBook(){
        TBook book1 = new TBook();
        book1.setIsbn("9787121282089");
        book1.setName("Spring Boot实战");
        book1.setPrice(89.00);
        book1.setAuthor("鲁迅");
        book1.setDigest("在当今Java EE 开发中，Spring 框架是当之无愧的王者。而Spring Boot 是Spring 主推的基于“习惯优于配置”的原则，让你能够快速搭建应用的框架，从而使得Java EE 开发变得异常简单。《JavaEE开发的颠覆者: Spring Boot实战》从Spring 基础、Spring MVC 基础讲起，从而无难度地引入Spring Boot 的学习...");
        book1.setPublishing("电子工业出版社");
        tBookRepository.save(book1);
        TBook book2 = new TBook();
        book2.setIsbn("9787302063841");
        book2.setPrice(29.8);
        book2.setName("人件");
        book2.setAuthor("汤姆");
        book2.setPublishing("清华大学出版社");
        tBookRepository.save(book2);
    }

    @RequestMapping("api/accessToken")
    public  String getAccessToken(){
        return wxApi.getAccessToken();
    }

    @RequestMapping("api/departments")
    public JSONObject getDepatments(){
        return JSONObject.fromObject(wxApi.getDepartmentList("143410631"));
    }

}
