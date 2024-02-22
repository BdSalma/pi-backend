package tn.examen.templateexamen2324.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.examen.templateexamen2324.entity.Interview;
import tn.examen.templateexamen2324.entity.RequestSupply;
import tn.examen.templateexamen2324.services.IinterviewService;
import tn.examen.templateexamen2324.services.RequestSupplyIService;

import java.util.List;
@CrossOrigin(origins="http://localhost:4200")
@RestController
@RequestMapping("/requestSupply")
public class RequestSupplyController {
    @Autowired
    RequestSupplyIService requestSupplyIService;
    @PostMapping("/addRequestSupply")
    public ResponseEntity<RequestSupply> ajouterRequestSupply(@RequestBody RequestSupply r) {
        RequestSupply requestSupply = requestSupplyIService.addRequestSupply(r);
        return new ResponseEntity<>(requestSupply, HttpStatus.CREATED);
    }
    @GetMapping("/retrieveAllRequestSupplies")
    @ResponseBody
    public List<RequestSupply> getRequestSupplies() {
        List<RequestSupply> listRequests = requestSupplyIService.retrieveAllRequestSupplies();
        return listRequests;
    }

    @DeleteMapping("/deleteRequestSupply/{id}")
    @ResponseBody
    public void deleteRequestSupply(@PathVariable("id") int idRequestSupply) {

        requestSupplyIService.deleteRequestSupply(idRequestSupply);
    }

    @PutMapping("/updateRequestSupply/{id}")
    @ResponseBody
    public RequestSupply updateBloc(@PathVariable("id") int idRequestSupply, @RequestBody RequestSupply requestSupply) {
        return requestSupplyIService.updateRequestSupply(idRequestSupply, requestSupply);
    }


}
