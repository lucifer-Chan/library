package cn.ttsales.domain;

import cn.ttsales.util.DateUtil;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import javax.persistence.*;

/**
 * Created by 露青 on 2016/10/11.
 * 推荐
 */
@Entity
public class TComment extends BaseEntity{
    @Id
    @GeneratedValue
    private Long id;
    private String content;
    private Long bookId;
    private Long materialId;
    private Long teacherId;
    private int likeNum;
    private int type;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH }, optional = true)
    @JoinColumn(name="bookId", insertable = false, updatable = false)
    private TBook book;

    public TBook getBook() {
        return book;
    }

    public void setBook(TBook book){
        this.book = book;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public TComment() {
    }

    @Override
    public JSONObject toCommonJSONObject(){
        JsonConfig config = new JsonConfig();
        config.setExcludes(new String[]{"createdAt","lastUpdateAt", "isDel","book"});
        JSONObject source = JSONObject.fromObject(this, config);
        source.put("created_at", DateUtil.time(getCreatedAt()));
        return getColumnFormat(source);
    }

    @Override
    public String toString(){
        return toCommonJSONObject().toString();
    }
}
