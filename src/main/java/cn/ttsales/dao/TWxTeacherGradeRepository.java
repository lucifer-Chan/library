package cn.ttsales.dao;

import cn.ttsales.domain.TWxTeacherGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by 露青 on 2016/10/17.
 */
public interface TWxTeacherGradeRepository extends JpaRepository<TWxTeacherGrade, Long> {
    List<TWxTeacherGrade> findByTeacherId(@Param("teacherId") Long teacherId);
}
