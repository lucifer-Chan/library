package cn.ttsales.dao;

import cn.ttsales.domain.PreGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by 露青 on 2016/10/17.
 */
public interface PreGradeRepository extends JpaRepository<PreGrade, Long>{
    List<PreGrade> findByIdIn(@Param("ids") List<Long> ids);

    @Override
    List<PreGrade> findAll();
}
