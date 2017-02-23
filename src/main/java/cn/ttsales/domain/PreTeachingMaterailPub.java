package cn.ttsales.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by 露青 on 2016/10/19.
 */
@Entity
public class PreTeachingMaterailPub extends BaseEntity{
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String shortName;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public PreTeachingMaterailPub() {

    }
}
