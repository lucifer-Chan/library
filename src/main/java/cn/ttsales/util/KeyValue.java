package cn.ttsales.util;

import net.sf.json.JSONObject;

import java.io.Serializable;

/**
 * Created by 露青 on 2016/10/17.
 */
public class KeyValue implements Jsonable{
    private Serializable key;
    private String value;

    public Serializable getKey() {
        return key;
    }

    public void setKey(Serializable key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public KeyValue(Serializable key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof  KeyValue))
            return false;
        KeyValue k2 = (KeyValue)obj;
        return key.equals(k2.getKey()) && value.equals(k2.getValue());
    }

    @Override
    public int hashCode(){
        return 17;
    }

    @Override
    public String toString(){
        return toJSONObject().toString();
    }

    public JSONObject toJSONObject(){
        JSONObject ret = new JSONObject();
        ret.put("key", key.toString());
        ret.put("value", value);
        return ret;
    }
}
