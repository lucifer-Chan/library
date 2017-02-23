package cn.ttsales.dao;

import cn.ttsales.domain.PreBureau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by 露青 on 2016/10/18.
 */
public interface PreBureauRepository extends JpaRepository<PreBureau, Long>{
    List<PreBureau> findByCityCodeOrProvinceCode(@Param("city") Integer code, @Param("province") Integer province);

    List<PreBureau> findByProvinceCode(@Param("province") Integer province);

    List<PreBureau> findByCityCode(@Param("city") Integer city);

    /**
     *
     *
     * province_code
     * private Integer cityCode;
     private String provinceName;
     private Integer provinceCode;
     */
}
