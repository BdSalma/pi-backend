package tn.examen.truetech.services;

import tn.examen.truetech.entity.Model;
import tn.examen.truetech.entity.Phone;
import tn.examen.truetech.entity.User;

import java.util.List;

public interface IUserService {
    User createUser(User user);
    public void deleteUser(long id);
    public User retrieveUserById(long id);
    List<User> retrieveAllUsers();
}
