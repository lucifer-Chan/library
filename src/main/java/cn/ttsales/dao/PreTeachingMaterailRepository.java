package cn.ttsales.dao;

import cn.ttsales.domain.PreTeachingMaterail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by 露青 on 2016/10/10.
 */
@RepositoryRestResource(path = "materails")
public interface PreTeachingMaterailRepository extends JpaRepository<PreTeachingMaterail, Long> {

    List<PreTeachingMaterail> findByPubId(@Param("pubId") long pubId);

    List<PreTeachingMaterail> findByPubIdAndGradeIn(@Param("pubId") long pubId, @Param("grades") List<Integer> grades);

}
