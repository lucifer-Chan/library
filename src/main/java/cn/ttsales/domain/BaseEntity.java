package cn.ttsales.domain;

/**
 * Created by 露青 on 2016/9/29.
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@MappedSuperclass
public abstract class BaseEntity implements Serializable ,IEntity{

    private static final long serialVersionUID = 1L;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date createdAt;

    @Column(name = "last_update_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateAt;

    @Column(name = "is_del")
    private Integer isDel;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastUpdateAt() {
        return lastUpdateAt;
    }

    public void setLastUpdateAt(Date lastUpdateAt) {
        this.lastUpdateAt = lastUpdateAt;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    @PrePersist
    protected void prePersist() {
        isDel = 0;
        createdAt = new java.util.Date();
    }

    @PreUpdate
    protected void preUpdate() {
        lastUpdateAt = new java.util.Date();
    }

    protected JSONObject getColumnFormat(JSONObject source){
        JSONObject ret = new JSONObject();
        for(Object  key : source.keySet().toArray()){
            String newKey = "";
            for(char c : ((String)key).toCharArray())
                newKey += Character.isUpperCase(c) ? "_" + Character.toLowerCase(c) : c;
            ret.put(newKey, source.get(key));
        }
        return ret;
    }

    /**
     * 只取需要的属性,Date 转为Long
     * @param attrs
     * @return
     */
    public JSONObject toDisplayJSON(String ... attrs){
        JSONObject source = toJSONObject(false);
        JSONObject ret = new JSONObject();

        if(null == attrs || attrs.length == 0){
            Object [] keys  =	source.keySet().toArray();
            attrs = new String[keys.length];
            for(int i = 0 ;i < keys.length; i ++)
                attrs[i] = keys[i].toString();
        }
        for(String attr : attrs){
            Object value = source.get(attr);
            if(null != value && value instanceof  JSONObject){
                try{
                    value = ((JSONObject)value).getLong("time");
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
            ret.put(attr, value);
        }
        return ret;
    }

    public JSONObject toCommonJSONObject(){
        JsonConfig config = new JsonConfig();
        config.setExcludes(new String[]{"createdAt","lastUpdateAt", "isDel"});
        return getColumnFormat(JSONObject.fromObject(this, config));
    }

    /**
     * 只取需要的属性
     * @param isColumnFormat
     * @param attrs
     * @return
     */
    public JSONObject toJSON(boolean isColumnFormat, String ... attrs){
        JSONObject source = toJSONObject(isColumnFormat);
        JSONObject ret = new JSONObject();
        for(String attr : attrs)
            ret.put(attr, source.get(attr));
        return ret;
    }

    public JSONObject toJSON(String ... attrs){
        return toJSON(false, attrs);
    }


    public JSONObject toJSONObject(boolean isColumnFormat){
        JSONObject ret = JSONObject.fromObject(this);
        return isColumnFormat ? getColumnFormat(ret) : ret;
    }

    public JSONObject toJSONObject(){
        return toJSONObject(false);
    }

    public String toString(boolean isColumnFormat){
        return toJSONObject(isColumnFormat).toString();
    }

    public String toString(){
        return toJSONObject().toString();
    }

    public void merge(String key, String value){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(key, value);
        merge(map);
    }

    private void invoke(Method m, Object value, boolean includeNummValue) throws Exception{
        if(includeNummValue || (null != value && !"".equals(value)))
            m.invoke(this, value);
    }

    private BaseEntity merge(Map<String, Object> map, boolean includeNullmValue){
        try{
            for(String key : map.keySet().toArray(new String[0])){
                Object value = map.get(key);
                Method m = getMethod(key);
                if(m != null && !id().equals(key)){
                    Type t = m.getParameterTypes()[0];
                    if(t == Date.class){
                        Date date =  null == value || "".equals(value) ? null : new Date(new Long(value.toString()));
                        invoke(m, date, includeNullmValue);
                    } else if(t == Integer.class){
                        Integer i =  null == value || "".equals(value) ? null : new Integer(value.toString());
                        invoke(m, i, includeNullmValue);
                    }  else if(t == Long.class){
                        Long l = null == value || "".equals(value) ? null : new Long(value.toString());
                        invoke(m, l, includeNullmValue);
                    } else{
                        invoke(m, value, includeNullmValue);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 空值不赋值
     * @param map
     * @return
     */
    public BaseEntity mergeExcludeNullValue(Map<String, Object> map){
        return merge(map, false);
    }

    public BaseEntity merge(Map<String, Object> map) {
        return merge(map, true);
//        try{
//            for(String key : map.keySet().toArray(new String[0])){
//                Object value = map.get(key);
//                Method m = getMethod(key);
//                if(m != null){
//                    Type t = m.getParameterTypes()[0];
//                    if(t == Date.class){
//                        Date date =  null == value || "".equals(value) ? null : new Date(new Long(value.toString()));
//                        m.invoke(this, date);
//                    } else if(t == Integer.class){
//                        Integer i =  null == value || "".equals(value) ? null : new Integer(value.toString());
//                        m.invoke(this, i);
//                    }  else if(t == Long.class){
//                        Long l = null == value || "".equals(value) ? null : new Long(value.toString());
//                        m.invoke(this, l);
//                    } else{
//                        m.invoke(this, value);
//                    }
//                }
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        return this;
    }

    private String gerFieldName(String fieldName) {
        String[] ss = fieldName.split("_");
        String ret = "";
        for (int i = 0; i < ss.length; i++) {
            String rep = i == 0 ? ss[i] : ss[i].substring(0, 1).toUpperCase()
                    + ss[i].substring(1, ss[i].length());
            ret += rep;
        }
        return "".equals(ret) ? fieldName : ret;
    }

    private Method getMethod(String _fieldName) {
        String fieldName = gerFieldName(_fieldName);
        String methodName = "set" + fieldName.substring(0, 1).toUpperCase()
                + fieldName.substring(1, fieldName.length());
        Class<?> clazz = this.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                for(Method method : clazz.getMethods()){
                    if(method.getName().equals(methodName))
                        return method;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}