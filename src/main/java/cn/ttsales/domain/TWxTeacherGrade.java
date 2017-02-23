package cn.ttsales.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by 露青 on 2016/10/17.
 */
@Entity
public class TWxTeacherGrade extends BaseEntity{
    @Id
    @GeneratedValue
    private Long id;

    private Integer grade;

    private Long teacherId;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public TWxTeacherGrade() {

    }
}
