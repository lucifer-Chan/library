package cn.ttsales.dao;

import cn.ttsales.domain.WxTeacher;
import cn.ttsales.sandbox.SqlBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Created by 露青 on 2016/9/29.
 */
@RepositoryRestResource(path = "teachers")//修改rest的映射名称
public interface WxTeacherRepository extends JpaRepository<WxTeacher, Long>,JpaSpecificationExecutor<WxTeacher> {
//    /**
//     * 暴露为Rest资源
//     * @param name
//     * @return
//     */
//    @RestResource(path="nicknames", rel="findByNickname")//修改rest的对应的方法名
//    List<WxTeacher> findByNickname(@Param("name") String name);

    List<WxTeacher> findByUserid(@Param("userid") String userid);

    List<WxTeacher> findByName(@Param("name") String name);

    List<WxTeacher> findByNameContaining(@Param("name") String name);

    List<WxTeacher> findByDepartment(@Param("schoolId") String shcoolId);

    List<WxTeacher> findByDepartmentIn(@Param("shcoolIds") List<String> shcoolIds);

    List<WxTeacher> findByIdIn(@Param("ids") List<Long> ids);

    int countByUserid(@Param("openid") String openid);

    //test
    @Query(value="select id,name,email from wx_teacher", nativeQuery = true)
    List<Object []> findNative();



}
