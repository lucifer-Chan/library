package cn.ttsales.util;

import cn.ttsales.bussiness.Teacher;
import cn.ttsales.domain.PreGrade;
import cn.ttsales.domain.PreTeachingMaterailPub;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by 露青 on 2016/10/25.
 */
public class CourseResult extends BaseResult{

    public CourseResult format(){
        JSONArray list = this.getRet().getJSONArray("list");
        JSONObject pub = null;
        for(Object item : list){
            JSONObject json = (JSONObject)item;
            for(Object lessonObj : json.getJSONArray("list")){
                JSONObject lesson = (JSONObject)lessonObj;
                JSONObject grade = lesson.getJSONObject("pre_grade");
                JSONObject pubJson = lesson.getJSONObject("pre_teaching_materail_pub");
                json.put("grade", grade);
                if(null == pub && null != pubJson)
                    pub = pubJson;
                lesson.remove("pre_grade");
                lesson.remove("pre_teaching_materail_pub");
                lesson.remove("pub_id");
                lesson.remove("grade");
            }
        }
        JSONObject ret = new JSONObject();
        ret.put("pre_teaching_materail_pub", pub);
        ret.put("list", list);
        this.setRet(ret);
        return this;
    }

    /*

    public <T extends  BaseEntity> BaseResult addMapWithEntities(String keyName, Map<? extends Comparable, ? extends Collection<T>> map){
        JSONArray list = new JSONArray();
        if(null == map || map.isEmpty()){
            ret.put("list", list);
            return this;
        }
        map.keySet()
                .stream()
                .sorted((comparable1,comparable2)-> comparable1.compareTo(comparable2))
                .collect(Collectors.toList())
                .forEach(comparable ->
                    list.add(new HashMap<String, Object>(){{
                        put(keyName, comparable);
                        put("list", map.get(comparable).parallelStream().map(T::toCommonJSONObject).collect(Collectors.toList()));
                    }})
                );
        ret.put("list", list);
        return this;
    }
     */

    public CourseResult format(Map<Integer, Map<Boolean, List<Teacher.PreTeachingMaterailWithRecommend>>> map){
        JSONObject ret = new JSONObject();
        JSONArray list = new JSONArray();
        for(int i : map.keySet().stream().collect(Collectors.toList())){
            JSONObject gradeJson = new JSONObject();
            Map<Boolean, List<Teacher.PreTeachingMaterailWithRecommend>> grade = map.get(i);
            gradeJson.put("grade", toJSON(new Long(i) ,grade));
            list.add(gradeJson);
        }
        ret.put("pre_teaching_materail_pub", null ==pub ? new JSONObject() : pub.toCommonJSONObject());
        ret.put("list", list);
        this.setRet(ret);
        return this;
    }

    private JSONObject toJSON(Long gradeId, Map<Boolean, List<Teacher.PreTeachingMaterailWithRecommend>> grade){
        JSONObject result = new JSONObject();
        result.put("with", toJSON(grade.get(true)));
        result.put("without", toJSON(grade.get(false)));
        result.put("gradeInfo" , null == preGradeMap.get(gradeId) ? new JSONObject() : preGradeMap.get(gradeId).toCommonJSONObject());
        return result;
    }

    private JSONArray toJSON(List<Teacher.PreTeachingMaterailWithRecommend> list){
        JSONArray result = new JSONArray();
        if(ListUtil.isEmpty(list))
            return result;
        for(Teacher.PreTeachingMaterailWithRecommend p : list){
            PreGrade pg = p.getPreTeachingMaterail().getPreGrade();
            if(null == preGradeMap.get(pg.getId()))
                preGradeMap.put(pg.getId(), pg);
            if(null == pub && p.getPreTeachingMaterail().getPreTeachingMaterailPub() != null)
                pub = p.getPreTeachingMaterail().getPreTeachingMaterailPub();
            result.add(p.toJSON());
        }
        return result;
    }
    private HashMap<Long,PreGrade> preGradeMap = new HashMap<>();
    private PreTeachingMaterailPub pub = null;
}
