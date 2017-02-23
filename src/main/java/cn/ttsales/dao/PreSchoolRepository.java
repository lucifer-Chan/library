package cn.ttsales.dao;

import cn.ttsales.domain.PreSchool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by 露青 on 2016/10/18.
 */
public interface PreSchoolRepository extends JpaRepository<PreSchool, Long> {
    List<PreSchool> findByBureauIdIn(@Param("bureauIds") List<Long> bureauId);

    List<PreSchool> findByBureauId(@Param("bureauIds") Long bureauId);
}
