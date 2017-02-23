package cn.ttsales.util;

import cn.ttsales.domain.BaseEntity;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class BaseResult {
    public final static String SUCCESS_MSG = "ok";
    public final static String FAILD_MSG = "faild";
    public final static String SUCCESS_CODE = "0";
    
    private JSONObject all = new JSONObject();

    private JSONObject ret = new JSONObject();

    protected String errcode;
    protected String errmsg;

    public BaseResult() {
    	 this.errcode = SUCCESS_CODE;
         this.errmsg = SUCCESS_MSG;
    }

    public BaseResult(String errcode){
        this.errcode = errcode;
        this.errmsg = SUCCESS_CODE.equals(errcode) ? SUCCESS_MSG : FAILD_MSG;
    }

    public BaseResult(String errcode, String errmsg) {
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    public BaseResult(KeyValue keyValue){
        this.errcode = keyValue.getKey().toString();
        this.errmsg = keyValue.getValue();
    }
    
    public BaseResult put(String key, Object value){
    	ret.put(key, value);
    	return this;
    }

    public BaseResult add(Map<String, Object> map){
        ret.putAll(map);
        return this;
    }

    public BaseResult addObj(Object obj){
        ret.putAll(JSONObject.fromObject(obj));
        return this;
    }

    public <T extends BaseEntity>BaseResult addEntity(T t){
        ret.putAll(t.toCommonJSONObject());
        return this;
    }

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

    public <T extends BaseEntity> BaseResult addPage(Page<T> page){
        return addEntityList(page.getContent())
                .put("totalElements", page.getTotalElements())
                .put("totalPages", page.getTotalPages())
                .put("number", page.getNumber())
                .put("numberOfElements", page.getNumberOfElements())
                .put("size", page.getSize());
    }

    public <T extends BaseEntity> BaseResult addEntityList(List<T> entities){
        if(ListUtil.isEmpty(entities)){
            ret.put("list", new ArrayList<JSONObject>());
            return this;
        }

        List<JSONObject> list = entities.parallelStream()
                .map(T::toCommonJSONObject)
                .collect(Collectors.toList());
        ret.put("list", list);
        return this;
    }

    public BaseResult addList(Iterable it){
        ret.put("list", JSONArray.fromObject(it));
        return this;
    }
    
    public String toString(){
		return toJSONObject().toString();
	}
    
    public JSONObject toJSONObject(){
    	all.put("errcode", errcode);
    	all.put("errmsg", errmsg);
        all.put("ret", ret);
    	return JSONObject.fromObject(all);
    }

    public String getErrcode() {
        return errcode;
    }


    public BaseResult setErrcode(String errcode) {
        this.errcode = errcode;
        return this;
    }


    public String getErrmsg() {
        return errmsg;
    }


    public BaseResult setErrmsg(String errmsg) {
        this.errmsg = errmsg;
        return this;
    }

    public boolean isSuccess() {
        return SUCCESS_CODE.equals(errcode);
    }

    public JSONObject getRet(){
        return this.ret;
    }

    protected void setRet(JSONObject ret){
        this.ret = ret;
    }
    
    public static void main(String [] ags){
    	JSONObject result = new JSONObject ();
    	result.put("name", "lucifer");
    	result.put("age", "31");
    	BaseResult br  = new BaseResult();
    	System.out.println(br.put("result",result).toString());
    }


}
