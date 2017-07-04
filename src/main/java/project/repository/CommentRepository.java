package project.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import project.model.Comment;

public interface CommentRepository extends JpaRepository<Comment,Integer> {

}
