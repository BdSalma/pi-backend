package tn.examen.templateexamen2324.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.examen.templateexamen2324.entity.Forum;
import tn.examen.templateexamen2324.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
