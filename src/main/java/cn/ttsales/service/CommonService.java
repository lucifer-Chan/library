package cn.ttsales.service;

import cn.ttsales.dao.*;
import cn.ttsales.remote.WxApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by 露青 on 2016/10/11.
 */
@Service
public class CommonService {
    @Autowired
    public PreTeachingMaterailRepository materailRepository;

    @Autowired
    public PreSchoolRepository preSchoolRepository;

    @Autowired
    public PreBureauRepository preBureauRepository;

    @Autowired
    public PreGradeRepository preGradeRepository;

    @Autowired
    public TBookRepository bookRepository;

    @Autowired
    public TCommentRepository commentRepository;

    @Autowired
    public WxTeacherRepository teacherRepository;

    @Autowired
    public TWxTeacherGradeRepository tWxTeacherGradeRepository;

    @Autowired
    public TCommentSupportRepository tCommentSupportRepository;

    @Autowired
    public WxApi wxApi;
}
