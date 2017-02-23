package cn.ttsales.service;

import cn.ttsales.dao.WxTeacherRepository;
import cn.ttsales.domain.PreBureau;
import cn.ttsales.domain.WxTeacher;
import cn.ttsales.util.ListUtil;
import cn.ttsales.util.PageInfo;
import cn.ttsales.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by 露青 on 2016/11/2.
 */
@Service
public class TeacherSearchService {
    @Autowired
    private WxTeacherRepository wxTeacherRepository;

    @Autowired
    private CommonService commonService;

    public Page<WxTeacher> search(final WxTeacher wxTeacher, final PageInfo page){
        return wxTeacherRepository.findAll((root, query, cb) -> {
            if(null == wxTeacher)
                return query.getRestriction();
            List<Predicate> predicates = new ArrayList<>();
            if(!StringUtil.isEmpty(wxTeacher.getName()))
                predicates.add(cb.like(root.<String>get("name"), "%" + wxTeacher.getName() + "%"));
            if(!StringUtil.isEmpty(wxTeacher.getDepartment())){

                List<String> schoolIds = getSchoolIds(wxTeacher.getDepartment());
                if(!ListUtil.isEmpty(schoolIds)){
                    CriteriaBuilder.In<String> in = cb.in(root.<String>get("department"));
                    schoolIds.forEach(s -> in.value(s));
                    predicates.add(in);
                }
            }
            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
        }, new PageRequest(page.getPage() - 1, page.getSize(), new Sort(Sort.Direction.ASC, page.getSortName())));
    }

    private List<String> getSchoolIds(String sOrgId){
        if(null != commonService.preSchoolRepository.findOne(Long.parseLong(sOrgId)))
            return Arrays.asList(sOrgId);
        Integer orgId = Integer.parseInt(sOrgId);
        List<Long> bureauIds = commonService.preBureauRepository.findByCityCodeOrProvinceCode(orgId, orgId)
                .stream()
                .map(PreBureau::getId)
                .collect(Collectors.toList());
        return ListUtil.isEmpty(bureauIds)? null : commonService.preSchoolRepository.findByBureauIdIn(bureauIds)
                .stream()
                .map(preSchool -> preSchool.getId().toString())
                .collect(Collectors.toList());
    }
}