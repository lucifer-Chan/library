package cn.ttsales.bussiness;

import cn.ttsales.dao.TCommentRepository;
import cn.ttsales.domain.*;
import cn.ttsales.exception.MultipleEntityException;
import cn.ttsales.exception.NullEntityException;
import cn.ttsales.service.CommonService;
import cn.ttsales.util.BaseResult;
import cn.ttsales.util.Constant;
import cn.ttsales.util.ListUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by 露青 on 2016/10/14.
 */
public class Teacher {
    private WxTeacher wxTeacher;

    private CommonService commonService;

    private Teacher(CommonService commonService){
        this.commonService = commonService;
    }

    public Teacher(long id, CommonService commonService) throws NullEntityException{
        this(commonService);
        this.wxTeacher = commonService.teacherRepository.findOne(id);
        if(this.wxTeacher == null)
            throw new NullEntityException(WxTeacher.class, id);
    }

    public Teacher(String userid, CommonService commonService) throws MultipleEntityException{
        this(commonService);
        List<WxTeacher> list = commonService.teacherRepository.findByUserid(userid);
        if(null == list || list.isEmpty()){
            WxTeacher wxTeacher = new WxTeacher();
            setEntity((WxTeacher)wxTeacher.mergeExcludeNullValue(JSONObject.fromObject(commonService.wxApi.getUserInfo(userid))));
        } else if(list.size() > 1)
            throw new MultipleEntityException(WxTeacher.class, userid);
        else
            this.wxTeacher = list.get(0);
    }

    public Teacher(WxTeacher wxTeacher, CommonService commonService){
        this(commonService);
        this.wxTeacher = wxTeacher;
    }

    public WxTeacher getEntity(){
        return this.wxTeacher;
    }

    public void setEntity(WxTeacher wxTeacher){
        this.wxTeacher = commonService.teacherRepository.saveAndFlush(wxTeacher);
    }

    /**
     * 推荐
     * @param bookId
     * @param content
     * @return
     */
    public TComment comment(Long bookId, Long materialId, String content){
        return comment(bookId, materialId, content, true);

    }



    /**
     * 点赞
     * @param commentId
     * @return 点过赞的返回null
     */
    public TComment like(Long commentId){
        List<TCommentSupport> likedList = commonService.tCommentSupportRepository.findByCommentIdAndTeacherId(commentId, wxTeacher.getId());
        if(!ListUtil.isEmpty(likedList))
            return null;
        TComment comment = commonService.commentRepository.getOne(commentId);
        int likeNum = comment.getLikeNum();
        comment.setLikeNum(++ likeNum);
        TCommentSupport support = new TCommentSupport();
        support.setTeacherId(wxTeacher.getId());
        support.setCommentId(commentId);
        commonService.tCommentSupportRepository.save(support);
        return commonService.commentRepository.save(comment);
    }

    /**
     * 添加图书
     * @param book
     * @param materialId
     * @param content
     * @return
     */
    public BaseResult addBook(TBook book, Long materialId, String content){
        TBook book2 = commonService.bookRepository.findByIsbn(book.getIsbn());
        if(null != book2){
            comment(book2.getId(), materialId, content, true);
            return new BaseResult(Constant.E_EXIST_BOOK).addEntity(book2);
        } else {
            book = commonService.bookRepository.save(book);
            comment(book.getId(), materialId, content, false);
            return new BaseResult().addEntity(book);
        }
    }

    /**
     * 更新个人信息
     * @param info 个人信息
     * @param grades 年级id
     * @return
     */
    public List<TWxTeacherGrade> save(WxTeacher info, List<Integer> grades){
        if(null != info)
            commonService.teacherRepository.save((WxTeacher)wxTeacher.mergeExcludeNullValue(info.toCommonJSONObject()));
        List<TWxTeacherGrade> newInfos = new ArrayList<TWxTeacherGrade>();
        List<TWxTeacherGrade> oldInfos = commonService.tWxTeacherGradeRepository.findByTeacherId(wxTeacher.getId());
        if(!ListUtil.isEmpty(grades)){
            if(!ListUtil.isEmpty(oldInfos))
                commonService.tWxTeacherGradeRepository.delete(oldInfos);
            HashSet<Integer> savableGrades = new HashSet<>();
            for(int i : grades){
                savableGrades.add(i);
                savableGrades.add(i+1);
            }
            for(int grade : savableGrades){
                TWxTeacherGrade entity = new TWxTeacherGrade();
                entity.setGrade(grade);
                entity.setTeacherId(wxTeacher.getId());
                newInfos.add(entity);
            }
            commonService.tWxTeacherGradeRepository.save(newInfos);
        }
        return newInfos;
    }

    /**
     * 获取所在学校
     * @return
     */
    public PreSchool getSchool(){
        return commonService.preSchoolRepository.findOne(Long.parseLong(wxTeacher.getDepartment().split(",")[0]));
    }

    public PreBureau getPerBureau(){
        return commonService.preBureauRepository.findOne(getSchool().getBureauId());
    }

    /**
     * 获取年级关联表
     * @return
     */
    public List<TWxTeacherGrade> getTeacherGrades(){
        return commonService.tWxTeacherGradeRepository.findByTeacherId(wxTeacher.getId());
    }

    /**
     * 获取年级列表
     * @return
     */
    public List<PreGrade> getGrades(){
        List<Long> grades = getTeacherGrades()
                .parallelStream()
                .map(g->new Long(g.getGrade()))
                .collect(Collectors.toList());
        return commonService.preGradeRepository.findByIdIn(grades);
    }



    /**
     * 我的课程-是否包含推荐
     * @return
     */

    public Map<Integer, Map<Boolean, List<PreTeachingMaterailWithRecommend>>> getCoursesGroupByHasRecommends(){
        Map<Integer, List<PreTeachingMaterail>> courses = getCourses();
        Map<Integer, Map<Boolean, List<PreTeachingMaterailWithRecommend>>> result = new HashMap<>();
        courses.keySet().forEach(i->{
//            Map<Integer, List<PreTeachingMaterailWithComment>> materailByGrade = new HashMap<>();
            List<PreTeachingMaterailWithRecommend> preTeachingMaterailWithCommentList = new ArrayList<>();
            courses.get(i).forEach(preTeachingMaterail -> {
                preTeachingMaterailWithCommentList.add(new PreTeachingMaterailWithRecommend(preTeachingMaterail, wxTeacher.getId(),commonService.commentRepository));
            });
            result.put(i,preTeachingMaterailWithCommentList.stream().collect(Collectors.groupingBy(PreTeachingMaterailWithRecommend::isHasRecomend, Collectors.toList())));
        });
        return result;
    }

    public static class PreTeachingMaterailWithRecommend{
        public PreTeachingMaterailWithRecommend(PreTeachingMaterail preTeachingMaterail, Long teacherId,  TCommentRepository commentRepository){
            this.preTeachingMaterail = preTeachingMaterail;
            this.recomendsNum = commentRepository.countDistinctBookIdByMaterialId(preTeachingMaterail.getId(), teacherId);
        }

        private PreTeachingMaterail preTeachingMaterail;

        private Long recomendsNum;

        public boolean isHasRecomend(){
            return recomendsNum.intValue() > 0;
        }

        public Long getRecomendsNum(){
            return this.recomendsNum;
        }

        public JSONObject toJSON(){
            JSONObject ret = this.preTeachingMaterail.toCommonJSONObject();
            ret.remove("pre_grade");
            ret.remove("pub_id");
            ret.remove("grade");
            ret.remove("pre_teaching_materail_pub");
            ret.put("recomendsNum", recomendsNum);
            return ret;
        }

        public PreTeachingMaterail getPreTeachingMaterail(){
            return this.preTeachingMaterail;
        }

    }


    /**
     * 我的课程
     * @return
     */
    public Map<Integer, List<PreTeachingMaterail>> getCourses(){

        Long pubId;
        PreSchool school = getSchool();
        pubId = school.getPubId();
        if(null == pubId);
            pubId = getPerBureau().getPubId();
        List<Integer> grades = getTeacherGrades()
                .parallelStream()
                .map(TWxTeacherGrade::getGrade)
                .collect(Collectors.toList());

        return null == pubId ? null : commonService.materailRepository.findByPubIdAndGradeIn(pubId, grades)
                .stream()
                .sorted((m1, m2) -> m1.getLessonNumber().compareTo(m2.getLessonNumber()))
                .collect(Collectors.groupingBy(PreTeachingMaterail::getGrade, Collectors.toList()));
    }

    /**
     * 我的推荐
     * @return
     */
    public List<TBook> getRecommendsByType0(Integer type){
        List<TComment> comments = null == type ? commonService.commentRepository.findByTeacherId(wxTeacher.getId()) :
                commonService.commentRepository.findByTeacherIdAndType(wxTeacher.getId(), type);
        List<Long> bookIds = comments
                        .parallelStream()
                        .map(TComment::getBookId)
                        .collect(Collectors.toList());
        return commonService.bookRepository.findByIdIn(bookIds);
    }



    /**
     * 单节课程-所有推荐
     * @param materialId
     * @return
     */
    public List<TBook> getAllRecommends(Long materialId){
        return getRecommends(materialId, false);
    }

    /**
     * 单节课程的-我的推荐
     * @param materialId
     * @return
     */
    public List<TBook> getRecommends(Long materialId){
       return getRecommends(materialId, true);
    }

    private List<TBook> getRecommends(Long materialId, boolean onlyMine){
        List<TComment> comments = onlyMine ?  commonService.commentRepository.findByMaterialIdAndTeacherId(materialId, wxTeacher.getId()) :
                commonService.commentRepository.findByMaterialId(materialId);
        List<Long> bookIds = comments.parallelStream()
                .map(TComment::getBookId)
                .collect(Collectors.toList());
        List<TBook> ret = commonService.bookRepository.findByIdIn(bookIds);
        //过滤无关评论
        ret.stream().forEach(book->{
            Set<TComment> commentsWithRightMateril = book.getComments()
                    .stream()
                    .filter(comment-> comment.getMaterialId().equals(materialId))
                    .collect(()-> new HashSet<>(),
                            (list, item) -> list.add(item),
                            (list1, list2) -> list1.addAll(list2));
            book.setComments(commentsWithRightMateril);
        });
        return ret;
    }

    private TComment comment(Long bookId, Long materialId, String content, boolean existBook){
        TComment comment = new TComment();
        comment.setBookId(bookId);
        comment.setContent(content);
        comment.setMaterialId(materialId);
        comment.setTeacherId(this.wxTeacher.getId());
        comment.setType(existBook ? 1 : 0);
        return commonService.commentRepository.save(comment);
    }

    public static void main(String [] a){
        List<Integer> list = Arrays.asList(1,3,5,7);
        List<Integer> result = new ArrayList<>();
        for(int i : list){
            result.add(i);
            result.add(i+1);
        }
        System.out.println(JSONArray.fromObject(result));
        for(int i : result)
            if(i%2!=0)
                System.out.println(i);
    }


//    public void save()

}
