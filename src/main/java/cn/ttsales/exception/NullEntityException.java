package cn.ttsales.exception;

import java.io.Serializable;

/**
 * Created by 露青 on 2016/10/14.
 */
public class NullEntityException extends Exception{
    public <T> NullEntityException(Class<T> clazz, Serializable id) {
        super("can't find entity with id(" + id + ") from class: " + clazz.getClass().getName() + "");
    }

    private Class clazz;
    private Serializable id;

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Serializable getId() {
        return id;
    }

    public void setId(Serializable id) {
        this.id = id;
    }
}
