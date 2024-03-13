package tn.examen.templateexamen2324.controller;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import tn.examen.templateexamen2324.entity.*;
import tn.examen.templateexamen2324.services.AuthService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins="http://localhost:4200")
public class AuthController {

    @Autowired
    AuthService authService;

    @GetMapping("/hello")
    @PreAuthorize("hasRole('Admin')")
    public String hello(){
        return "Hello there!";
    }

    @GetMapping("/hello-2")
    //@PreAuthorize("hasRole('client_societe') OR hasRole('client_individu')")
    @PreAuthorize("hasRole('Student')")
    public String hello2(){
        return "Hello there! - Student";
    }

    @PostMapping("/login")
    public ResponseEntity<?> Login (@RequestBody Map<String, String> requestBody) {
        return authService.login(requestBody.get("username"),requestBody.get("password"));
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseMessage> logout (@RequestBody Map<String, String> requestBody) {
        return authService.logout(requestBody.get("token"));
    }

    @PostMapping("/create-user")
    public ResponseEntity<ResponseMessage> createUser(@RequestBody Map<String, String> userRegistration) {
        Object[] obj = authService.createUser(userRegistration);
        int status = (int) obj[0];
        ResponseMessage message = (ResponseMessage) obj[1];
        return ResponseEntity.status(status).body(message);
    }

    @GetMapping("/user-id/{userId}")
    @PreAuthorize("hasRole('Admin')")
    public User getUser(@PathVariable String userId) {
        return authService.getUserById(userId);
    }

    @GetMapping("/all-users")
    @PreAuthorize("hasRole('Admin')")
    public List<User> getAllUsers() {
        return authService.getAllUsers();
    }

    @DeleteMapping("delete-user/{userId}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> deleteUserById(@PathVariable String userId) {
        return authService.deleteUserById(userId);
    }


    @GetMapping("/user-details")
    public User getUserDetails(Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        return authService.getUserById(userId);
    }

    @PutMapping("/emailVerification")
    public ResponseEntity<ResponseMessage> emailVerification(Authentication authentication) {
        ResponseMessage message = new ResponseMessage();
        try {
            Jwt jwtToken = (Jwt) authentication.getPrincipal();
            authService.emailVerification(jwtToken.getClaim("sub"));
            message.setMessage("Email verification sent successfully, check your Inbox");
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            message.setMessage("Failed to send email verification");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }

    @GetMapping("/check-user")
    public ResponseEntity<?> checkValidity(Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        return authService.checkUser(jwtToken);
    }

    @PutMapping("/addRoleToUser/{userId}")
    public void checkValidity(@PathVariable String userId, @RequestBody Map<String, String> requestBody) {
        authService.addRoleToUser(userId,requestBody.get("role"));
    }

    @PutMapping("/update-user/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable String userId,@RequestBody Map<String, String> requestBody) {
        Object[] result = authService.updateUser(userId, requestBody);
        int statusCode = (Integer) result[0];
        if (statusCode == 200) {
            Object data = result[1];
            return ResponseEntity.ok(data);
        } else {
            ResponseMessage errorMessage = (ResponseMessage) result[1];
            return ResponseEntity.status(statusCode).body(errorMessage);
        }
    }

    @PutMapping("/approve-user/{userId}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<?> approveUser(@PathVariable String userId) {
        return authService.approveUser(userId);
    }

    @PutMapping("/activate-user/{userId}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<?> activateUser(@PathVariable String userId) {
       return authService.activateUser(userId);
    }

    @GetMapping("/get-All-individu")
    @PreAuthorize("hasRole('Admin')")
    public List<Individu> getAllIndividu() {
        return authService.getAllIndividu();
    }

    @GetMapping("/individus-byRole/{role}")
    @PreAuthorize("hasRole('Admin')")
    public List<Individu> getAllIndividuFilteredByRole(@PathVariable IndividuRole role) {
        return authService.getAllIndividuFilteredByRole(role);
    }

    @GetMapping("/individus-byFiled/{field}")
    @PreAuthorize("hasRole('Admin')")
    public List<Individu> getAllIndividuFilteredByFields(@PathVariable String field) {
        return authService.getAllIndividuFilteredByFields(field);
    }

    @GetMapping("/get-All-society")
    @PreAuthorize("hasRole('Admin')")
    public List<Society> getAllSociety() {
        return authService.getAllSociety();
    }

    @GetMapping("/societies-byRole/{role}")
    @PreAuthorize("hasRole('Admin')")
    public List<Society> getAllSocietiesFilteredByRole(@PathVariable SocietyRole role) {
        return authService.getAllSocietiesFilteredByRole(role);
    }

    @GetMapping("/societies-byFiled/{field}")
    @PreAuthorize("hasRole('Admin')")
    public List<Society> getAllSocietiesFilteredByFields(@PathVariable String field) {
        return authService.getAllSocietiesFilteredByFields(field);
    }

}
