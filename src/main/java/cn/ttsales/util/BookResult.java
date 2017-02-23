package cn.ttsales.util;

import cn.ttsales.domain.WxTeacher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by 露青 on 2016/10/26.
 */
public class BookResult extends BaseResult{
    public BookResult addTeacherInfo2Comment(List<WxTeacher> teachers){
        Map<Long, WxTeacher> teachersMap = teachers.stream().collect(Collectors.toMap(i -> i.getId(), i -> i));
        JSONArray list = this.getRet().getJSONArray("list");
        list.stream().forEach(obj->{
            JSONObject book = (JSONObject)obj;
            book.getJSONArray("comments").forEach(commentObj->{
                JSONObject comment = (JSONObject)commentObj;
                comment.put("teacher_info", teachersMap.get(comment.getLong("teacher_id")).toCommonJSONObject());
            });
        });
        return this;
    }

}
