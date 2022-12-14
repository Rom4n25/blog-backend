package pl.romanek.blog.repository.springdatajpa;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.repository.query.Param;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.romanek.blog.entity.Post;
import pl.romanek.blog.repository.PostRepository;

@Profile("spring-data-jpa")
public interface SpringDataPostRepository extends JpaRepository<Post, Integer>, PostRepository {

        // Queries not needed-only for educational purpose @Query(value="SELECT * FROM
        // post WHERE user_id = :id",nativeQuery=true)@Query(value="SELECT p FROM Post p
        // WHERE p.user.id = :id")@Override

        @EntityGraph(type = EntityGraphType.FETCH, attributePaths = { "user", "comment", "comment.pointComment",
                        "comment.user", "pointPost", "pointPost.user" })
        Page<Post> findAllByUserIdOrderByCreatedDesc(Integer id, Pageable pageable);

        @Override
        @EntityGraph(type = EntityGraphType.FETCH, attributePaths = { "user",
                        "comment", "comment.pointComment", "pointPost" })
        Page<Post> findAllByOrderByCreatedDesc(Pageable pageable);

        @Override
        @EntityGraph(type = EntityGraphType.FETCH, attributePaths = { "user",
                        "comment", "comment.pointComment", "comment.user", "pointPost" })
        Optional<Post> findById(Integer id);

        @Override
        @EntityGraph(type = EntityGraphType.FETCH, attributePaths = { "user",
                        "comment", "comment.pointComment", "pointPost", "pointPost.user" })
        @Query(value = "SELECT DISTINCT post FROM Post post JOIN post.pointPost ORDER BY post.points DESC")
        Page<Post> findTop(Pageable pageable);

        @Override
        @EntityGraph(type = EntityGraphType.FETCH, attributePaths = { "user", "comment", "comment.pointComment",
                        "comment.user", "pointPost", "pointPost.user" })
        @Query(value = "SELECT DISTINCT post FROM Post post JOIN post.tag tag WHERE tag.name=:name ORDER BY post.created DESC")
        Page<Post> findAllByTagByOrderByCreatedDesc(@Param("name") String name, Pageable pageable);

}