package pl.romanek.blog.repository.jpa;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import pl.romanek.blog.entity.Comment;
import pl.romanek.blog.repository.CommentRepository;

@Profile("jpa")
@Repository
public class JpaCommentRepository implements CommentRepository {

    @PersistenceContext
    EntityManager em;

    @SuppressWarnings("unchecked")
    @Override
    public List<Comment> findAllByPostIdOrderByCreatedAsc(Integer id) {
        return em.createQuery(
                "SELECT comment FROM Comment comment JOIN FETCH comment.user user JOIN FETCH user.role role LEFT JOIN FETCH comment.post post LEFT JOIN FETCH comment.pointComment pointComment WHERE comment.post.id="
                        + id + " ORDER BY comment.created ASC")
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Comment> findAll() {
        return em
                .createQuery(
                        "SELECT comment FROM Comment comment JOIN FETCH comment.user user JOIN FETCH user.role role JOIN FETCH comment.post post JOIN FETCH comment.pointComment pointComment")
                .getResultList();
    }

    @Override
    public Comment save(Comment comment) {
        em.persist(comment);
        return comment;
    }

    @Override
    public Optional<Comment> findById(Integer id) {
        Comment comment = em.find(Comment.class, id);
        return Optional.ofNullable(comment);
    }

    @Override
    public void deleteById(Integer id) {
        Comment comment = em.find(Comment.class, id);
        em.remove(comment);
    }
}
