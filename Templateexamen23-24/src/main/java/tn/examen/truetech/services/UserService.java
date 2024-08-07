package tn.examen.truetech.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.examen.truetech.entity.Model;
import tn.examen.truetech.entity.Phone;
import tn.examen.truetech.entity.User;
import tn.examen.truetech.repository.UserRepository;

import java.util.List;

@Service
public class UserService implements IUserService{

    @Autowired
    private UserRepository userRepository;
    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }


    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User retrieveUserById(long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

}
