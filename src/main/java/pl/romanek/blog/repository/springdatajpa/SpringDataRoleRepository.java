package pl.romanek.blog.repository.springdatajpa;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.romanek.blog.entity.Role;
import pl.romanek.blog.repository.RoleRepository;

@Profile("spring-data-jpa")
public interface SpringDataRoleRepository extends JpaRepository<Role, Integer>, RoleRepository {

}
