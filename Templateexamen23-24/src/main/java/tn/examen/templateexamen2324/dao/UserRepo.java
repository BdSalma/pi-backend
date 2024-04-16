package tn.examen.templateexamen2324.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tn.examen.templateexamen2324.entity.User;

@Repository

public interface UserRepo extends CrudRepository<User,String> {
}
