package cn.ttsales.dao;

import cn.ttsales.domain.TComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by 露青 on 2016/10/11.
 */
@RepositoryRestResource(path = "comments")
public interface TCommentRepository extends JpaRepository<TComment, Long>{
    List<TComment> findByTeacherId(@Param("teacherId") Long teacherId);

    List<TComment> findByTeacherIdAndType(@Param("teacherId") Long teacherId, @Param("type") Integer type);

    List<TComment> findByTeacherIdAndBookId(@Param("teacherId") Long teacherId, @Param("bookId") Long bookId);

    List<TComment> findByBookId(@Param("bookId") Long bookId);

    List<TComment> findByMaterialId(@Param("materialId") Long materialId);

    List<TComment> findByMaterialIdAndTeacherId(@Param("materialId")Long materialId, @Param("teacherId") Long teacherId);

    List<TComment> findByMaterialIdAndTeacherIdAndType(@Param("materialId")Long materialId, @Param("teacherId") Long teacherId, @Param("type") int type);

    Long countByBookId(@Param("bookId") Long bookId);

    @Query("select count(distinct t.bookId) from TComment t where t.materialId = :materialId and t.teacherId = :teacherId")
    Long countDistinctBookIdByMaterialId(@Param("materialId") Long materialId, @Param("teacherId") Long teacherId);

    int countByTeacherIdAndType(@Param("teacherId")Long teacherId, @Param("type")int type);



    //select type, teacher_id ,COUNT(1) num from tcomment  group by teacher_id, type ;

    @Query(value="select c.type, c.teacher_id ,COUNT(c.id) num from tcomment c, wx_teacher w where c.teacher_id = w.id  and w.department =:department group by c.teacher_id, c.type", nativeQuery = true)
    List<Object []> groupByTeacherIdAndType(@Param("department") String department);

}
