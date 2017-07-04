package project.dto;


import project.model.Comment;

import java.util.Date;

public class CommentDTO {
    public CommentDTO(Comment comment) {
        this.id_comment = comment.getIdComment();
        this.comment_title = comment.getCommentTitle();
        this.comment_body = comment.getCommentBody();
        this.name = comment.getUser().getName();
        this.date = comment.getDate();
    }

    private Integer id_comment;

    public Integer getId_comment() {
        return id_comment;
    }

    public void setId_comment(Integer id_comment) {
        this.id_comment = id_comment;
    }

    private String comment_title;

    public String getComment_title() {
        return comment_title;
    }

    public void setComment_title(String comment_title) {
        this.comment_title = comment_title;
    }

    private String comment_body;

    public String getComment_body() {
        return comment_body;
    }

    public void setComment_body(String comment_body) {
        this.comment_body = comment_body;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private UserDTO user;

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
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
