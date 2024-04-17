package tn.examen.templateexamen2324.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.examen.templateexamen2324.entity.Pack;
import tn.examen.templateexamen2324.entity.Stand;
import tn.examen.templateexamen2324.services.IPackService;
import tn.examen.templateexamen2324.services.IStandService;

import java.util.List;

@RestController
@RequestMapping("/stand")
@CrossOrigin(origins="http://localhost:4200")
public class StandController {

    @Autowired
    IStandService standService;

    @GetMapping("/find-all-stands")
    @ResponseBody
    public List<Stand> getPacks() {
        List<Stand> listPack = standService.retrieveAllStand();
        return listPack;
    }

    @PostMapping("/add-stand")
    @ResponseBody
    public Stand createStand(@RequestBody Stand b) {
        Stand stand = standService.addStand(b);
        return stand;
    }

    @DeleteMapping("/delete-stand/{id}")
    @ResponseBody
    public void deleteStand(@PathVariable("id") int standId) {
        standService.deleteStand(standId);
    }

    @PutMapping("/update-stand/{id}")
    @ResponseBody
    public Stand updateStand(@PathVariable("id") int standId, @RequestBody Stand stand) {
        return standService.updateStand(standId, stand);
    }
    @GetMapping("/find-stand-By-Status/{Status}")
    @ResponseBody
    public List<Stand> findStandByStatut(@PathVariable("Status") Boolean  statut ) {
        return  standService.findStandByStatut(statut);
    }

    @GetMapping("/find-stand/{idStand}")
    @ResponseBody
    public Stand findStandByStatut(@PathVariable("idStand") Long  idStand ) {
        return standService.findStandById(idStand);
    }

}