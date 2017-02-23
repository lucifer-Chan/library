package cn.ttsales.sandbox;

import net.sf.json.JSONArray;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 露青 on 2016/11/22.
 */
public class SqlBuilder {

    public SqlBuilder(String colsName){
        this.colsName = colsName;
    }

    private String colsName;

    public List<HashMap<String, Object>> toMap(List<Object []> source){
        String [] cols = colsName.split(",");
        List<HashMap<String, Object>> result = new ArrayList<>();
        for(int i = 0; i < source.size(); i ++){
            HashMap<String, Object> map = new HashMap<>();
            for(int j = 0; j < cols.length; j ++){
                map.put(cols[j], source.get(i)[j]);
            }
            result.add(map);

        }
        return result;
    }

//    public <T> List<T> findAll(Class<T> clz){
//        Query query = entityManager.createQuery("from " + clz.getName());
//        return  query.getResultList();
//    }

    public <T> List<T> toMap(Class<T> clz, List<Object []> source) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        String [] cols = colsName.split(",");
        List<T> result = new ArrayList<>();
        for(int i = 0; i < source.size(); i ++){
            T dto =  clz.newInstance();
            for(int j = 0; j < cols.length; j ++){
                Method m = getMethod(clz, cols[j]);
                m.invoke(dto, source.get(i)[j]);
            }
            result.add(dto);

        }
        return result;
    }

    private Method getMethod(Class<?> clazz, String fieldName) {
        String methodName = "set" + fieldName.substring(0, 1).toUpperCase()
                + fieldName.substring(1, fieldName.length());
        try {
            for(Method method : clazz.getMethods()){
                if(method.getName().equals(methodName))
                    return method;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }


    public static void main(String [] args) throws IllegalAccessException, InvocationTargetException, InstantiationException {

        SqlBuilder sqlBuilder = new SqlBuilder("id,name,email");
        //1、得到的数据，假数据
        List<Object[]> result = new ArrayList<Object[]>(){{
            add(new Object []{11,"name1", "11.qq.com"});
            add(new Object []{12,"name2", "22.qq.com"});
        }};

        List<Dto> ret = sqlBuilder.toMap(Dto.class, result);
        for(Dto d : ret)
            System.out.println(d);



//        JSONArray ret = JSONArray.fromObject(sqlBuilder.toMap(result));
//        System.out.println(ret);
//        System.out.println(sqlBuilder.getMethod(Dto.class, "id"));




//        System.out.println(sqlBuilder);
    }

    public static class Dto{
        private int id;
        private String name;
        private String email;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String toString(){
            return "name:" + name + ", id: " + id + ", email:" + email;
        }
    }

}
