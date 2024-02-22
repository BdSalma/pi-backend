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

import tn.examen.templateexamen2324.entity.ResponseMessage;
import tn.examen.templateexamen2324.entity.User;
import tn.examen.templateexamen2324.services.AuthService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins="http://localhost:4200")
public class AuthController {

    @Autowired
    AuthService authService;

    @GetMapping("/hello")
    @PreAuthorize("hasRole('Student')")
    public String hello(){
        return "Hello there!";
    }

    @GetMapping("/hello-2")
    //@PreAuthorize("hasRole('client_societe') OR hasRole('client_individu')")
    @PreAuthorize("hasRole('client_societe')")
    public String hello2(){
        return "Hello there! - Admin";
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
    @PreAuthorize("hasRole('admin')")
    public User getUser(@PathVariable String userId) {
        return authService.getUserById(userId);
    }

    @GetMapping("/all-users")
    @PreAuthorize("hasRole('admin')")
    public List<User> getAllUsers() {
        return authService.getAllUsers();
    }

    @DeleteMapping("delete-user/{userId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> deleteUserById(@PathVariable String userId) {
        return authService.deleteUserById(userId);
    }


    @GetMapping("/user-details")
    public Map<String, Object> getUserDetails(Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        Map<String, Object> claims = jwtToken.getClaims();
        //String username = jwtToken.getClaim("preferred_username");
        //String email = jwtToken.getClaim("email");
        return claims;
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
        ResponseMessage message = new ResponseMessage();
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        Map<String, Object> claims = jwtToken.getClaims();
        String approved = jwtToken.getClaim("approved");
        boolean email = jwtToken.getClaim("email_verified");
        if(email){
            if(Objects.equals(approved, "NO")){
                message.setMessage("Not approved");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
            }
        }else{
            message.setMessage("Not verified");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
        return ResponseEntity.ok(claims);
    }

}
