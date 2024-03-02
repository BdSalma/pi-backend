package tn.examen.templateexamen2324.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.examen.templateexamen2324.entity.Society;
import tn.examen.templateexamen2324.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, String> {
}
