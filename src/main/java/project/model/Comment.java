package project.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    public Comment() {

    }

    public Comment(Integer id_comment, String commentBody, String commentTitle, User user, Date date) {
        this.idComment = id_comment;
        this.commentTitle = commentTitle;
        this.commentBody = commentBody;
        this.user = user;
        this.date = date;
    }

    @Id
    @Column(name = "id_comment", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idComment;

    public Integer getIdComment() {
        return idComment;
    }

    public void setIdComment(Integer id_comment) {
        this.idComment = id_comment;
    }

    @Column(name = "comment_title", unique = false, nullable = false, length = 40)
    private String commentTitle;

    public String getCommentTitle() {
        return commentTitle;
    }

    public void setCommentTitle(String comment_title) {
        this.commentTitle = comment_title;
    }

    @Column(name = "comment_body", unique = false, nullable = false, length = 255)
    private String commentBody;

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    @JsonBackReference
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    private Date date = new Date();

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

