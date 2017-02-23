package cn.ttsales.service;

import cn.ttsales.dao.WxTeacherRepository;
import cn.ttsales.domain.WxTeacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 露青 on 2016/10/10.
 */
@Service
public class WxTeacherService {
    @Autowired
    WxTeacherRepository wxTeacherRepository;

    @Cacheable(value="wx-teachers")
    public List<WxTeacher> findByName(String name){
        List<WxTeacher> ret = wxTeacherRepository.findByName(name);
        System.out.println("cache for " + name);
        return ret;
    }

    public static void main(String [] args){

    }

}
