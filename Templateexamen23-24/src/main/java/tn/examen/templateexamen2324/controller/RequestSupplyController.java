package tn.examen.templateexamen2324.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import tn.examen.templateexamen2324.entity.RequestSupply;
import tn.examen.templateexamen2324.entity.User;
import tn.examen.templateexamen2324.services.AuthService;
import tn.examen.templateexamen2324.services.RequestSupplyIService;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins="http://localhost:4200")
@RestController
@RequestMapping("/requestSupply")
public class RequestSupplyController {
    @Autowired
    RequestSupplyIService requestSupplyIService;
    @Autowired
    AuthService authService;
    @PostMapping("/addRequestSupply")
    public ResponseEntity<RequestSupply> ajouterRequestSupply(@RequestBody RequestSupply r, Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        RequestSupply requestSupply = requestSupplyIService.addRequestSupply(r,userId);
        return new ResponseEntity<>(requestSupply, HttpStatus.CREATED);
    }
    @GetMapping("/retrieveAllRequests")
    @ResponseBody
    public List<RequestSupply> getRequestSupplies() {
       return requestSupplyIService.retrieveAllRequestSupplies();
    }
    @GetMapping("/getRequestSupply/{id}")
    @ResponseBody
    public RequestSupply getRequestSupplyById(@PathVariable("id") int idRequestSupply) {
        return requestSupplyIService.retrieveRequestSupplyById(idRequestSupply);
    }
    @DeleteMapping("/deleteRequestSupply/{id}")
    @ResponseBody
    public void deleteRequestSupply(@PathVariable("id") int idRequestSupply) {
        requestSupplyIService.deleteRequestSupply(idRequestSupply);
    }
    @PutMapping("/updateRequestSupply/{id}")
    @ResponseBody
    public RequestSupply updateRequest(@PathVariable("id") int id, @RequestBody RequestSupply updatedRequestSupply) {
        RequestSupply existingRequestSupply = requestSupplyIService.retrieveRequestSupplyById(id);
            existingRequestSupply.setQuantity(updatedRequestSupply.getQuantity());
            existingRequestSupply.setDescription(updatedRequestSupply.getDescription());
            existingRequestSupply.setCategory(updatedRequestSupply.getCategory());
            existingRequestSupply.setDate(updatedRequestSupply.getDate());
            existingRequestSupply.setValidity(updatedRequestSupply.getValidity());
            return  requestSupplyIService.updateRequestSupply(existingRequestSupply.getIdRequestSupply());

    }
    @GetMapping("/retrieveAllRequestSuppliesByIndividus")
    @ResponseBody
    public List<RequestSupply> getsuppliesByIndividus( Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        List<RequestSupply> list = requestSupplyIService.getRequestSupplyByIndividus(userId);

        return list;
    }
    @GetMapping("/retrieveOldRequestSuppliesByIndividus")
    @ResponseBody
    public List<RequestSupply> getOldsuppliesByIndividus( Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        List<RequestSupply> list = requestSupplyIService.getOLdRequestSupplyByIndividus(userId);

        return list;
    }
    @GetMapping("/recommendRequestsForSociety")
    @ResponseBody
    public List<RequestSupply> recommendRequestsForSociety(Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        return requestSupplyIService.recommendNewRequestsForSociety(userId);
    }

    @GetMapping("/getRequestSupplyByForumTheme/{theme}")
    @ResponseBody
    public List<RequestSupply> getRequestSupplyByForumTheme(@PathVariable("theme") String theme) {
        return requestSupplyIService.getRequestSupplyByForumTheme(theme);
    }
    @GetMapping("/getIndividuStatistics")
    public ResponseEntity<Map<String, Integer>> getIndividuStatistics(Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        Map<String, Integer> statistics = requestSupplyIService.getIndividuStatistics(userId);
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/getSocietyStatistics")
    public ResponseEntity<Map<String, Integer>> getSocietyStatistics(Authentication authentication) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        Map<String, Integer> statistics = requestSupplyIService.getSocietyStatistics(userId);
        return ResponseEntity.ok(statistics);
    }
}
