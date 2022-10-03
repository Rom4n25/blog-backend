package pl.romanek.blog.entity;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Post {

    @Id
    private int id;
    private String text;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "post")
    private Set<Comment> comment;
}
