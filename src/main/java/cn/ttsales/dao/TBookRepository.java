package cn.ttsales.dao;

import cn.ttsales.domain.TBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by 露青 on 2016/10/11.
 */

@RepositoryRestResource(path = "books")
public interface TBookRepository extends JpaRepository<TBook, Long> ,JpaSpecificationExecutor<TBook> {
//    List<TBook> findByAuthor(@Param("author") String author);
//
//    List<TBook> findByPublishing(@Param("publishing") String publishing);
//
//    List<TBook> findByName(@Param("name") String name);
//
//    List<TBook> findByAuthorContaining(@Param("author") String author);
//
//    List<TBook> findByPublishingContaining(@Param("publishing") String publishing);
//
//    List<TBook> findByNameContaining(@Param("name") String name);

    TBook findByIsbn(@Param("isbn") String isbn);

    List<TBook> findByIdIn(List<Long> ids);



}
