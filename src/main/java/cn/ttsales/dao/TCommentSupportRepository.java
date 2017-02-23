package cn.ttsales.dao;

import cn.ttsales.domain.TCommentSupport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by 露青 on 2016/10/17.
 */
public interface TCommentSupportRepository extends JpaRepository<TCommentSupport, Long> {
    List<TCommentSupport> findByCommentIdAndTeacherId(@Param("commentId") Long commentId, @Param("teacherId") Long teacherId);
}
