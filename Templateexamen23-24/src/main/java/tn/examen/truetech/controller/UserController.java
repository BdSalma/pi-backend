package tn.examen.truetech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.examen.truetech.entity.User;
import tn.examen.truetech.services.IUserService;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class UserController {
    @Autowired
    IUserService userService;

    @PostMapping("/add-user")
    @ResponseBody
    public ResponseEntity<User> createUser(@RequestBody User user) {

        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);

    }

    @GetMapping("/find-all-users")
    @ResponseBody
    public List<User> getUsers() {
        List<User> listUser = userService.retrieveAllUsers();
        return listUser;
    }



    @GetMapping("/find-user/{userId}")
    @ResponseBody
    public User getUserById(@PathVariable("userId") long userId) {
        return  userService.retrieveUserById(userId);
    }

    /*@PutMapping("/update-model/{id}")
    @ResponseBody
    public ResponseEntity<Model> updateModel(@PathVariable("id") Long modelId, @RequestParam("name") String name,
                                             @RequestParam("file") MultipartFile file) {
        try {
            Model model = new Model();
            model.setName(name);
            Model updatedModel = modelService.updateModel(modelId,model, file);
            return new ResponseEntity<>(updatedModel, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
*/
    @DeleteMapping("/delete-user/{userId}")
    @ResponseBody
    public void deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
    }
}