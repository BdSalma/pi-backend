package tn.examen.templateexamen2324.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import tn.examen.templateexamen2324.entity.*;
import tn.examen.templateexamen2324.repository.IndividuRepository;
import tn.examen.templateexamen2324.repository.SocietyRepository;
import tn.examen.templateexamen2324.repository.UserRepository;

import java.net.URI;
import java.util.*;

@Service
public class AuthService implements IAuthService{

    private static final String UPDATE_PASSWORD = "UPDATE_PASSWORD";
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    UserRepository userRepository;
    @Autowired
    IndividuRepository individuRepository;
    @Autowired
    SocietyRepository societyRepository;
    @Autowired
    HttpServletRequest request;
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String iuuserUrl;
    @Value("${spring.security.oauth2.client.registration.oauth2-client-credentials.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.oauth2-client-credentials.authorization-grant-type}")
    private String grantType;
    @Value("${keycloak.auth-server-url}")
    private String server_url;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.credentials.secret}")
    private String secret;
    private Keycloak keycloak;

    public AuthService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }


    @Override
    public ResponseEntity<?> login(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", clientId);
        requestBody.add("grant_type", grantType);
        requestBody.add("username", username);
        requestBody.add("password", password);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(requestBody, headers);
        try {
            ResponseEntity<LoginResponse> response = restTemplate.postForEntity("http://localhost:8082/realms/esprit-piazza/protocol/openid-connect/token", httpEntity, LoginResponse.class);
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        } catch (HttpClientErrorException ex) {
            ResponseMessage message = new ResponseMessage();
            if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                message.setMessage("There is no account with the provided credentials");
                return new ResponseEntity<>(message, ex.getStatusCode());
            } else {
                message.setMessage(ex.getResponseBodyAsString());
                return new ResponseEntity<>(message, ex.getStatusCode());
            }
        }
    }

    @Override
    public ResponseEntity<ResponseMessage> logout(String token) {
        ResponseMessage message = new ResponseMessage();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("client_id", clientId);
            map.add("client_secret", secret);
            map.add("refresh_token", token);
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
            restTemplate.postForEntity("http://localhost:8082/realms/esprit-piazza/protocol/openid-connect/logout", httpEntity, ResponseMessage.class);
            message.setMessage("Logged out successfully");
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (HttpClientErrorException ex) {
            message.setMessage(ex.getResponseBodyAsString());
            return new ResponseEntity<>(message, ex.getStatusCode());
        }
    }

    @Override
    public Object[] createUser(Map<String, String> userRegistration){
        boolean isIndividuRole = false;
        for (IndividuRole role : IndividuRole.values()) {
            if (role.toString().equals(userRegistration.get("role"))) {
                isIndividuRole = true;
                break;
            }
        }
        if(isIndividuRole){
            Individu userIndividu = new Individu(userRegistration);
            return createIndividu(userIndividu);
        }else{
            Society userSociety = new Society(userRegistration);
            return createSociety(userSociety);
        }
    }


    public Object[] createIndividu(Individu userRegistration) {
        ResponseMessage message = new ResponseMessage();
        int statusId = 0;
        try {
            UserRepresentation user = new UserRepresentation();
            user.setEnabled(true);
            user.setEmailVerified(false);
            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
            List<CredentialRepresentation> list = new ArrayList<>();
            Map<String, List<String>> attributes = new HashMap<>();
            List<String> attribute1Value = new ArrayList<>();
            attribute1Value.add("NO");
            attributes.put("approved", attribute1Value);
            user.setAttributes(attributes);

            user.setUsername(userRegistration.getUsername());
            user.setEmail(userRegistration.getEmail());
            user.setFirstName(userRegistration.getFirstName());
            user.setLastName(userRegistration.getLastName());
            credentialRepresentation.setValue(userRegistration.getPassword());

            list.add(credentialRepresentation);
            user.setCredentials(list);
            UsersResource usersResource = getUsersResource();
            Response response = usersResource.create(user);
            statusId = response.getStatus();
            if (Objects.equals(201, response.getStatus())) {
                URI location = response.getLocation();
                if (location != null) {
                    String userId = location.getPath().substring(location.getPath().lastIndexOf('/') + 1);
                    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    String hashedPassword = passwordEncoder.encode(userRegistration.getPassword());
                    User userData = userRegistration;
                    userData.setId(userId);
                    userData.setPassword(hashedPassword);
                    userRepository.save(userData);

                    //emailVerification(userId);
                    /*UserResource userResource = getUserResource(userId);
                    RolesResource rolesResource = keycloak.realm(realm).roles();
                    RoleRepresentation representation = rolesResource.get("test").toRepresentation();
                    System.out.println("this is role "+representation);
                    userResource.roles().realmLevel().add(Collections.singletonList(representation));*/

                    message.setMessage("Account created successfully");

                }
            } else if (statusId == 409) {
                message.setMessage("this account already exists");
            } else {
                message.setMessage("there was an error while creating this account");
            }

            return new Object[]{statusId, message};
        } catch (Exception e) {
            message.setMessage("Error occurred while creating the account: " + e.getMessage());
            return new Object[]{HttpStatus.INTERNAL_SERVER_ERROR.value(), message};
        }
    }

    public Object[] createSociety(Society userRegistration) {
        ResponseMessage message = new ResponseMessage();
        int statusId = 0;
        try {
            UserRepresentation user = new UserRepresentation();
            user.setEnabled(true);
            user.setEmailVerified(false);
            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
            List<CredentialRepresentation> list = new ArrayList<>();
            Map<String, List<String>> attributes = new HashMap<>();
            List<String> attribute1Value = new ArrayList<>();
            attribute1Value.add("NO");
            attributes.put("approved", attribute1Value);
            user.setAttributes(attributes);
            user.setUsername(userRegistration.getUsername());
            user.setEmail(userRegistration.getEmail());
            credentialRepresentation.setValue(userRegistration.getPassword());

            list.add(credentialRepresentation);
            user.setCredentials(list);
            UsersResource usersResource = getUsersResource();
            Response response = usersResource.create(user);
            statusId = response.getStatus();
            if (Objects.equals(201, response.getStatus())) {
                URI location = response.getLocation();
                if (location != null) {
                    String userId = location.getPath().substring(location.getPath().lastIndexOf('/') + 1);
                    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    String hashedPassword = passwordEncoder.encode(userRegistration.getPassword());
                    User userData = userRegistration;
                    userData.setId(userId);
                    userData.setPassword(hashedPassword);
                    userRepository.save(userData);

                    //emailVerification(userId);
                    /*UserResource userResource = getUserResource(userId);
                    RolesResource rolesResource = keycloak.realm(realm).roles();
                    RoleRepresentation representation = rolesResource.get("test").toRepresentation();
                    System.out.println("this is role "+representation);
                    userResource.roles().realmLevel().add(Collections.singletonList(representation));*/
                    message.setMessage("Account created successfully");
                }
            } else if (statusId == 409) {
                message.setMessage("this account already exists");
            } else {
                message.setMessage("there was an error while creating this account");
            }
            return new Object[]{statusId, message};
        } catch (Exception e) {
            message.setMessage("Error occurred while creating the account: " + e.getMessage());
            return new Object[]{HttpStatus.INTERNAL_SERVER_ERROR.value(), message};
        }
    }


    @Override
    public void emailVerification(String userId){
        UsersResource usersResource = getUsersResource();
        usersResource.get(userId).sendVerifyEmail();
    }

    private UsersResource getUsersResource() {
        RealmResource realm1 = keycloak.realm(realm);
        return realm1.users();
    }
    public UserResource getUserResource(String userId){
        UsersResource usersResource = getUsersResource();
        return usersResource.get(userId);
    }

    @Override
    public User getUserById(String userId) {
        return  userRepository.findById(userId).orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public ResponseEntity<String> deleteUserById(String userId) {
        try {
            Response response = getUsersResource().delete(userId);
            int statusCode = response.getStatus();
            switch (statusCode) {
                case 204:
                    userRepository.deleteById(userId);
                    return ResponseEntity.ok("User deleted successfully");
                case 401:
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
                case 403:
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized");
                default:
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
