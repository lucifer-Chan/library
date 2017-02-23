package cn.ttsales.domain;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by 露青 on 2016/10/11.
 */
@Entity
public class TBook extends BaseEntity{
    @Id
    @GeneratedValue
    private Long id;
    private String isbn;
    private String name;
    private String publishing;
    private Double price;
    private String author;
    private String digest;
    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @OneToMany(cascade = CascadeType.ALL ,mappedBy ="book", fetch = FetchType.LAZY)
    private Set<TComment> comments = new HashSet<TComment>();

    public Set<TComment> getComments() {
        return comments;
    }

    public void setComments(Set<TComment> comments) {
        this.comments = comments;
    }

    @Override
    public JSONObject toCommonJSONObject(){
        JsonConfig config = new JsonConfig();
        config.setExcludes(new String[]{"createdAt","lastUpdateAt", "isDel","comments"});
        JSONObject source = JSONObject.fromObject(this, config);
        JSONArray array = new JSONArray();
        getComments().stream().sorted((c1,c2)->c2.getCreatedAt().compareTo(c1.getCreatedAt())).forEach(comment->
            array.add(comment.toCommonJSONObject())
        );
        source.put("comments", array);
        return getColumnFormat(source);
    }

    @Override
    public String toString(){
        return toCommonJSONObject().toString();
    }



    public TBook() {
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublishing() {
        return publishing;
    }

    public void setPublishing(String publishing) {
        this.publishing = publishing;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }
}
