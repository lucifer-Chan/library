package cn.ttsales.util;

import java.util.Collection;
import java.util.List;

/**
 * Created by 露青 on 2016/10/17.
 */
public class ListUtil {
    public static boolean isEmpty(Collection list){
        return null == list || list.isEmpty();
    }

    public static String toString(Collection list, String spliter){
        if(isEmpty(list))
            return null;
        StringBuilder result=new StringBuilder();
        boolean flag=false;
        for (Object obj : list) {
            if (flag) {
                result.append(spliter);
            }else {
                flag=true;
            }
            result.append(obj);
        }
        return result.toString();
    }

    public static String toString(Collection list){
        return toString(list, ",");
    }
}
