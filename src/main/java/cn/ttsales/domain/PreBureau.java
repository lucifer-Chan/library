package cn.ttsales.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by 露青 on 2016/10/18.
 */
@Entity
public class PreBureau extends BaseEntity{
    @Id
    private Long id;
    private String name;
    private String cityName;
    private Integer cityCode;
    private String provinceName;
    private Integer provinceCode;
    private Long pubId;


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

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getCityCode() {
        return cityCode;
    }

    public void setCityCode(Integer cityCode) {
        this.cityCode = cityCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Integer getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(Integer provinceCode) {
        this.provinceCode = provinceCode;
    }

    public Long getPubId() {
        return pubId;
    }

    public void setPubId(Long pubId) {
        this.pubId = pubId;
    }

    public PreBureau() {

    }
}
