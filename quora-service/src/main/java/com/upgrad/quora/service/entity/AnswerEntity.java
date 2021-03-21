package com.upgrad.quora.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table(name= "answer")
@NamedQueries({
        @NamedQuery(name = "answerByUuid", query = "select u from AnswerEntity u where u.uuid = :uuid")
})
public class AnswerEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="uuid")
    @Size(max=200)
    private String uuid;

    @Column(name="ans")
    @Size(max=255)
    private String ans;

    @Column(name="date")
    private ZonedDateTime date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToOne
    @JoinColumn(name = "question_id")
    private QuestionEntity questionEntity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public QuestionEntity getQuestion() {
        return questionEntity;
    }

    public void setQuestion(QuestionEntity questionEntity) {
        this.questionEntity = questionEntity;
    }
}
