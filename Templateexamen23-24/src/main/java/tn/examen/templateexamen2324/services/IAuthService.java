package tn.examen.templateexamen2324.services;

import org.springframework.http.ResponseEntity;
import tn.examen.templateexamen2324.entity.ResponseMessage;
import tn.examen.templateexamen2324.entity.User;

import java.util.List;
import java.util.Map;

public interface IAuthService {
    public ResponseEntity<?> login(String username, String password);
    public ResponseEntity<ResponseMessage> logout(String token);
    public Object[] createUser(Map<String, String> userRegistration);
    public void emailVerification(String userId);
    public User getUserById(String userId);
    public ResponseEntity<String> deleteUserById(String userId);
    public List<User> getAllUsers();
    public void addRoleToUser(String userId, String roleName);
    public Object[] updateUser(String id,Map<String, String> userRegistration);
}
