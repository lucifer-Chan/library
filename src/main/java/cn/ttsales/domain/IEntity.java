package cn.ttsales.domain;

import java.io.Serializable;

/**
 * Created by 露青 on 2016/10/11.
 */
public interface IEntity extends Serializable{
    Long getId();

    default String id(){return "id";}
}
