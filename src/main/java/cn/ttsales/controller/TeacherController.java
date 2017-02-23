package cn.ttsales.controller;

import cn.ttsales.bussiness.Teacher;
import cn.ttsales.domain.*;
import cn.ttsales.exception.MultipleEntityException;
import cn.ttsales.exception.NullEntityException;
import cn.ttsales.service.CommonService;
import cn.ttsales.util.*;

import static cn.ttsales.util.Constant.*;
import static cn.ttsales.util.CookieUtil.*;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by 露青 on 2016/10/17.
 */
@RestController
@RequestMapping("teacher")
public class TeacherController {
    @Autowired
    private CommonService commonService;

    @RequestMapping("init")
    public BaseResult init(@CookieValue(name=KEY_USER_ID) String userid){
        System.out.println(userid);
        return new BaseResult().put("userid", userid);
    }


    /**
     * 我的课程
     *
     * @param userid
     * @return 未注册年级-000001
     * @throws MultipleEntityException
     */
    @RequestMapping("course")
    public BaseResult myCourse(@CookieValue(name = KEY_USER_ID) String userid) throws MultipleEntityException {
        Teacher teacher = new Teacher(userid, commonService);
        WxTeacher wxTeacher = teacher.getEntity();
        if (ListUtil.isEmpty(commonService.tWxTeacherGradeRepository.findByTeacherId(wxTeacher.getId()))) {
            return new BaseResult(E_EMPTY_GRADE).addObj(wxTeacher.toCommonJSONObject());
        }
        CourseResult ret = new CourseResult();
        ret.addMapWithEntities("grade", teacher.getCourses());
        return ret.format();
        //return new CourseResult().addEntityList("grade", teacher.getCourses()).
        //return new BaseResult().addMapWithEntities("grade", teacher.getCourses());
    }

    /**
     * 所有推荐
     * @return
     */
    @RequestMapping("allRecommends")
    public BaseResult allRecommends(@CookieValue(name = KEY_USER_ID) String userid, Long materialId) throws MultipleEntityException{
        return recommends(userid, materialId, false);
    }

    /**
     * 我的推荐
     *
     * @return
     * @throws MultipleEntityException
     */
    @RequestMapping("myRecommends")
    public BaseResult myRecommend(@CookieValue(name = KEY_USER_ID) String userid, @RequestParam(value="materialId", required = false) Long materialId) throws MultipleEntityException {
        return recommends(userid, materialId, true);
    }

    /**
     * 某个教师的推荐
     * @param teacherId
     * @return
     * @throws MultipleEntityException
     */
    @RequestMapping("recommends")
    public BaseResult getRecommendsByTeacher(Long teacherId) throws MultipleEntityException, NullEntityException {
        return new BaseResult().addEntityList(new Teacher(teacherId, commonService).getRecommendsByType0(0));
    }

    /**
     * 我的课程--分组By自己是否有推荐
     * @param userid
     * @return
     * @throws MultipleEntityException
     */
//    @RequestMapping("myCoursesGroupByHasRecommends")
//    public BaseResult myCoursesGroupByHasRecommends(@CookieValue(name = KEY_USER_ID) String userid) throws MultipleEntityException{
//        Teacher teacher = new Teacher(userid, commonService);
//        Map<Integer, Map<Boolean, List<PreTeachingMaterailWithComment>>> source = teacher.getCoursesGroupByHasRecommends();
//        JSONObject ret = new JSONObject();
//        source.keySet().forEach(i->{
//            Map<Boolean, List<PreTeachingMaterailWithComment>> map = source.get(i);
//        });
//
//        return null;
//    }
    @RequestMapping("myCoursesGroupByHasRecommends")
    public Object myCoursesGroupByHasRecommends(@CookieValue(name = KEY_USER_ID) String userid) throws MultipleEntityException{
        Teacher teacher = new Teacher(userid, commonService);
        return new CourseResult().format(teacher.getCoursesGroupByHasRecommends());
//        return new Teacher(userid, commonService).getCoursesGroupByHasRecommends();
    }

    /**
     * 个人信息
     * @param userid
     * @return
     */
    @RequestMapping("info")
    public BaseResult myInfo(@CookieValue(name = KEY_USER_ID) String userid) throws MultipleEntityException{
        Teacher teacher = new Teacher(userid, commonService);
        List<PreGrade> grades = teacher.getGrades();
        PreSchool school = teacher.getSchool();
        PreBureau bureau = teacher.getPerBureau();
        Object gradeJsonArray =  new BaseResult().addEntityList(grades).getRet().get("list");
        return new BaseResult()
                .put("teacher", teacher.getEntity().toCommonJSONObject())
                .put("school", school.toCommonJSONObject())
                .put("bureau", bureau.toCommonJSONObject())
                .put("grades", gradeJsonArray);
    }

    @RequestMapping("userInfo")
    public BaseResult userInfo(@CookieValue(name=KEY_USER_ID) String userid) throws MultipleEntityException{
        return new BaseResult().put("user", new Teacher(userid, commonService).getEntity().toCommonJSONObject());
    }

    @RequestMapping("grades")
    public BaseResult getGrades(@CookieValue(name = KEY_USER_ID) String userid) throws MultipleEntityException{
        return new BaseResult().addEntityList(new Teacher(userid, commonService).getGrades());
    }


    @RequestMapping(value = "save", method = RequestMethod.POST)
    public BaseResult saveInfo(@CookieValue(name = KEY_USER_ID) String userid, WxTeacher info, String grades) throws MultipleEntityException{
        info.setId(null);
        System.out.println(info);
        List<Integer> gradeIds = StringUtil.isEmpty(grades) ? new ArrayList<>() :  Arrays.asList(grades.split(",")).stream().map(s->new Integer(s)).collect(Collectors.toList());
        new Teacher(userid,commonService).save(info, gradeIds);
        return new BaseResult();
    }

    /**
     * 获取评论
     * @param userid
     * @param bookid
     * @return
     * @throws MultipleEntityException
     */
    @RequestMapping("comments")
    public BaseResult getComments(@CookieValue(name = KEY_USER_ID) String userid, @RequestParam(value ="bookid", required = false) Long bookid)throws MultipleEntityException{
        Teacher teacher = new Teacher(userid, commonService);
        List<TComment> t = null == bookid ?
                commonService.commentRepository.findByTeacherId(teacher.getEntity().getId()) : commonService.commentRepository.findByTeacherIdAndBookId(teacher.getEntity().getId(), bookid);
        return new BaseResult().addEntityList(t);
    }

    /**
     * 添加图书
     * @param userid
     * @param content
     * @param materialid
     * @param book
     * @return
     * @throws MultipleEntityException
     */
    @RequestMapping(value = "doAddBook", method = RequestMethod.POST)
    public BaseResult doAddBook(@CookieValue(name = KEY_USER_ID) String userid, String content, long materialid, TBook book) throws MultipleEntityException {
        return new Teacher(userid, commonService).addBook(book, materialid, content);
    }

    /**
     * 评论
     * @param userid
     * @param content
     * @param materialid
     * @param bookid
     * @return
     * @throws MultipleEntityException
     */
    @RequestMapping(value = "doComment", method = RequestMethod.POST)
    public BaseResult doComment(@CookieValue(name = KEY_USER_ID) String userid, String content, long materialid, long bookid) throws MultipleEntityException {
        return new BaseResult().addEntity(new Teacher(userid, commonService).comment(bookid, materialid, content));
    }

    /**
     * 点赞评论
     * @param userid
     * @param commentid
     * @return
     * @throws MultipleEntityException
     */
    @RequestMapping(value = "support", method = RequestMethod.POST)
    public BaseResult support(@CookieValue(name=KEY_USER_ID) String userid, long commentid) throws MultipleEntityException{
        TComment comment = new Teacher(userid, commonService).like(commentid);
        return null == comment ? new BaseResult(E_SUPPORTED_COMMENT) : new BaseResult().addEntity(comment) ;
    }

    private BaseResult recommends(String userid, Long materialId, boolean onlyMine) throws MultipleEntityException{
        Teacher teacher = new Teacher(userid, commonService);
        WxTeacher wxTeacher = teacher.getEntity();
        if (ListUtil.isEmpty(commonService.tWxTeacherGradeRepository.findByTeacherId(wxTeacher.getId()))) {
            return new BaseResult(E_EMPTY_GRADE).addEntity(wxTeacher);
        }

        if(null == materialId)
            return new BaseResult().addEntityList(teacher.getRecommendsByType0(null));
        List<TBook> books = onlyMine ? teacher.getRecommends(materialId) : teacher.getAllRecommends(materialId);
        BookResult ret = new BookResult();
        ret.addEntityList(books);

        List<Long> teacherIds = new ArrayList<>();
        books.stream().forEach(book ->
                teacherIds.addAll(book.getComments().stream().map(TComment::getTeacherId).collect(Collectors.toList()))
        );
        return ret.addTeacherInfo2Comment(commonService.teacherRepository.findByIdIn(teacherIds));
    }
}
