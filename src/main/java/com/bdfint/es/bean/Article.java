package com.bdfint.es.bean;

import com.bdfint.es.common.Global;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Component
@Document(indexName = Global.INDEX_BOOT, type = Global.TYPE_ARTICLE)
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotEmpty
    @Length(min = 1, max = 50, message = "标题长度必须介于 1 和 50 之间")
    @Field(store = true, analyzer = Global.IK_MAX_WORD, searchAnalyzer = Global.IK_MAX_WORD, type = FieldType.text)
    private String title; // 标题

    @NotEmpty
    @Length(min = 1, max = 2000, message = "内容长度必须介于 1 和 2000 之间")
    @Field(store = true, analyzer = Global.IK_MAX_WORD, searchAnalyzer = Global.IK_MAX_WORD, type = FieldType.text)
    private String content; // 内容

    private String keywords; // 关键字

    private Long hits; // 点击数

    @Field(format = DateFormat.date_time, index = false, store = true, type = FieldType.Long)
    private Date createDate; // 创建日期

    public Article() {
    }

    public Article(String id, String title, String content, String keywords, Long hits, Date createDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.keywords = keywords;
        this.hits = hits;
        this.createDate = createDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Long getHits() {
        return hits;
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
