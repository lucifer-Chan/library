package cn.ttsales.dao;

import cn.ttsales.domain.WxTeacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by 露青 on 2016/10/10.
 */
@Repository
public class RedisTeacherDao {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisTemplate<Object,Object> redisTemplate;

    @Resource(name= "stringRedisTemplate")
    ValueOperations<String, String> valOpsStr;

    @Resource(name="redisTemplate")
    ValueOperations<Object,Object> valOps;

    public void saveString(String key, String value){
        valOpsStr.set(key, value);
    }

    public void saveWxTeacher(WxTeacher wxTeacher){
        valOps.set(wxTeacher.getId(), wxTeacher);
    }

    public String getString(String key){
        return valOpsStr.get(key);
    }

    public WxTeacher getWxTeacher(long id){
        return (WxTeacher)valOps.get(id);
    }


}
