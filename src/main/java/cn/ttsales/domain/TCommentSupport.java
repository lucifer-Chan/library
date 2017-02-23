package cn.ttsales.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by 露青 on 2016/10/17.
 */
@Entity
public class TCommentSupport extends BaseEntity{
    @Id
    @GeneratedValue
    private Long id;
    private Long commentId;
    private Long teacherId;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public TCommentSupport() {

    }
}
