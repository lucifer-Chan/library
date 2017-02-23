package cn.ttsales.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by 露青 on 2016/10/10.
 */
@Entity
public class PreTeachingMaterail extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private Long pubId;
    private Integer grade;
    private Integer lessonNumber;
    private String lessonName;
    private String author;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "grade", referencedColumnName = "code", insertable = false, updatable = false)
    private PreGrade preGrade;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pubId", referencedColumnName = "id", insertable = false, updatable = false)
    private PreTeachingMaterailPub preTeachingMaterailPub;

    public PreGrade getPreGrade() {
        return preGrade;
    }

    public void setPreGrade(PreGrade preGrade) {
        this.preGrade = preGrade;
    }

    public PreTeachingMaterailPub getPreTeachingMaterailPub() {
        return preTeachingMaterailPub;
    }

    public void setPreTeachingMaterailPub(PreTeachingMaterailPub preTeachingMaterailPub) {
        this.preTeachingMaterailPub = preTeachingMaterailPub;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getPubId() {
        return pubId;
    }

    public void setPubId(Long pubId) {
        this.pubId = pubId;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getLessonNumber() {
        return lessonNumber;
    }

    public void setLessonNumber(Integer lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
