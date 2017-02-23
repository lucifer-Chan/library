package cn.ttsales.controller;

import cn.ttsales.domain.PreBureau;
import cn.ttsales.domain.PreTeachingMaterail;
import cn.ttsales.domain.TBook;
import cn.ttsales.domain.WxTeacher;
import cn.ttsales.service.BookSearchService;
import cn.ttsales.service.CommonService;
import cn.ttsales.service.TeacherSearchService;
import cn.ttsales.util.BaseResult;
import cn.ttsales.util.KeyValue;
import cn.ttsales.util.PageInfo;
import cn.ttsales.util.StringUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by 露青 on 2016/10/19.
 */
@RestController
@RequestMapping("search")
public class SearchController {
    @Autowired
    private CommonService commonService;

    @Autowired
    private BookSearchService bookSearchService;

    @Autowired
    private TeacherSearchService teacherSearchService;

    @RequestMapping("isbn")
    public BaseResult book(@RequestParam(value="isbn", required = false) String isbn){
        if(!StringUtil.isEmpty(isbn))
            return new BaseResult().addEntity(commonService.bookRepository.findByIsbn(isbn));
        return null;
    }

    @RequestMapping("book")
    public BaseResult searchBook(TBook book, @RequestParam(value = "page", required = false) Integer page) {
        return new BaseResult().addPage(bookSearchService.search(book, new PageInfo(null == page ? 1 : page, "name")));
    }

    @RequestMapping("teacher")
    public BaseResult searchTeacher(WxTeacher teacher, @RequestParam(value = "page", required = false) Integer page){
        return new BaseResult().addPage(teacherSearchService.search(teacher, new PageInfo(null == page ? 1 : page, "name")));
    }

    @RequestMapping("provinces")
    public BaseResult getProvinces(){
        List<JSONObject> ret = commonService.preBureauRepository.findAll()
                .stream().map(p-> new KeyValue(p.getProvinceCode(), p.getProvinceName()).toJSONObject())
                .distinct()
                .collect(Collectors.toList());
        return new BaseResult().addList(ret);
    }

    @RequestMapping("cities")
    public BaseResult getCities(Integer province){
        List<JSONObject> ret = commonService.preBureauRepository.findByProvinceCode(province)
                .stream().map(p-> new KeyValue(p.getCityCode(), p.getCityName()).toJSONObject())
                .distinct()
                .collect(Collectors.toList());
        return new BaseResult().addList(ret);
    }

    @RequestMapping("bureaus")
    public BaseResult getBureaus(Integer city){
        List<JSONObject> ret = commonService.preBureauRepository.findByCityCode(city)
                .stream().map(p-> new KeyValue(p.getId(), p.getName()).toJSONObject())
                .distinct()
                .collect(Collectors.toList());
        return new BaseResult().addList(ret);
    }

    @RequestMapping("schools")
    public BaseResult getSchools(Long bureau){
        List<JSONObject> ret = commonService.preSchoolRepository.findByBureauId(bureau)
                .stream().map(p->new KeyValue(p.getId(), p.getName()).toJSONObject())
                .collect(Collectors.toList());
        return new BaseResult().addList(ret);
    }

    @RequestMapping("comment/stat")
    public List<Object[]> statComments(String department){
        return commonService.commentRepository.groupByTeacherIdAndType(department);
    }


    @RequestMapping("materail")
    public BaseResult getMaterail(Long id){
        return new BaseResult().addEntity(commonService.materailRepository.findOne(id));
    }

//    @RequestMapping("teacher")
//    public  BaseResult searchTeacher(@RequestParam Map<String, String> map){
//        BaseResult br = new BaseResult();
//        if(null == map)
//            return br;
//        if(!StringUtil.isEmpty(map.get("name")))
//            return br.addEntityList(commonService.teacherRepository.findByNameContaining(map.get("name")));
//        if(!StringUtil.isEmpty(map.get("schoolId")))
//            return br.addEntityList(commonService.teacherRepository.findByDepartment(map.get("schoolId")));
//        if(!StringUtil.isEmpty(map.get(("orgId")))){
//            Integer orgId = Integer.parseInt(map.get("orgId"));
//            List<Long> bureauIds = commonService.preBureauRepository.findByCityCodeOrProvinceCode(orgId, orgId)
//                    .stream()
//                    .map(PreBureau::getId)
//                    .collect(Collectors.toList());
//
//            List<String> schoolIds = commonService.preSchoolRepository.findByBureauIdIn(bureauIds)
//                    .stream()
//                    .map(school -> school.getId().toString())
//                    .collect(Collectors.toList());
//
//            return br.addEntityList(commonService.teacherRepository.findByDepartmentIn(schoolIds));
//        }
//        return br.addEntityList(commonService.teacherRepository.findAll());
//    }



//    @RequestMapping("test")
//    public BaseResult test(int page){
//        return new BaseResult().addEntityList(commonService.bookRepository.findByQuery(page * Constant.PAGE_SIZE, Constant.PAGE_SIZE));
//
//    }

    /**
     *  List<TBook> findByAuthor(@Param("author") String author);

     List<TBook> findByName(@Param("name") String name);

     List<TBook> findByPublishing(@Param("publishing") String publishing);

     TBook findByIsbn(@Param("isbn") String isbn);

     List<TBook> findByNameContaining(@Param("name") String name);

     */
}
