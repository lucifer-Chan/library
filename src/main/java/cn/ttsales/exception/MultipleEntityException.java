package cn.ttsales.exception;

/**
 * Created by 露青 on 2016/10/14.
 */
public class MultipleEntityException extends Exception{
    public <T> MultipleEntityException(Class<T> clazz, Object key){
        super("There is more the one entity with the key(" + key + ") from class:" + clazz.getName());
        this.clazz = clazz;
        this.key = key;
    }

    private Class clazz;
    private Object key;

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

/**
     * public <T> NullEntityException(Class<T> clazz, Serializable id) {
     super("can't find entity with id(" + id + ") from class: " + clazz.getClass().getName() + "");
     }
     */
}
