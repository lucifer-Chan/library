package cn.ttsales.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by 露青 on 2016/10/18.
 */
@Entity
public class PreSchool extends BaseEntity{
    @Id
    private Long id;
    private Long bureauId;
    private String name;
    private Long pubId;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBureauId() {
        return bureauId;
    }

    public void setBureauId(Long bureauId) {
        this.bureauId = bureauId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPubId() {
        return pubId;
    }

    public void setPubId(Long pubId) {
        this.pubId = pubId;
    }

    public PreSchool() {

    }
}
