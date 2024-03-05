package tn.examen.templateexamen2324.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.examen.templateexamen2324.entity.*;
import tn.examen.templateexamen2324.services.IPackService;
import tn.examen.templateexamen2324.services.IStandService;

import java.util.List;

@RestController
@RequestMapping("/pack")
@CrossOrigin(origins="http://localhost:4200")
public class PackController {

    @Autowired
    IPackService packService;

    @GetMapping("/find-all-packs")
    @ResponseBody
    public List<Pack> getPacks() {
        List<Pack> listPack = packService.retrieveAllPacks();
        return listPack;
    }

    @GetMapping("/find-all-packs-by-forum/{forum}")
    @ResponseBody
    public List<Pack> getPacksByForum(@PathVariable("forum") Forum forum) {
        List<Pack> listPack = packService.retrieveAllPacksByForum(forum);
        return listPack;
    }

    @GetMapping("/find-pack/{packId}")
    @ResponseBody
    public Pack getPackById(@PathVariable("packId") long packId) {
       return  packService.getPackById(packId);

    }

    @GetMapping("/find-pack-By-Status/{Status}")
    @ResponseBody
    public List<Pack> getPackById(@PathVariable("Status") Boolean  statut ) {
        return  packService.findPackByStatut(statut);

    }

    @GetMapping("/find-pack-By-typePack-reservationStatus/{type}/{reservationStatus}")
    @ResponseBody
    public List<Pack> getPackByTypeAndReservationStatus(@PathVariable("type") TypePack type, @PathVariable("reservationStatus") ReservationStatus reservationStatus ) {
        return  packService.findPackByTypePackAndReservationStatus(type,reservationStatus);

    }

    @PostMapping("/add-pack")
    @ResponseBody
    public Pack createPack(@RequestBody Pack b) {
        Pack pack = packService.addPack(b);
        return pack;
    }

    @DeleteMapping("/delete-pack/{packId}")
    @ResponseBody
    public void deletePack(@PathVariable("packId") long packId) {
        packService.deletePack(packId);
    }

    @PutMapping("/update-pack/{packId}")
    @ResponseBody
    public Pack updateBloc(@PathVariable("packId") long packId, @RequestBody Pack pack) {
        return packService.updatePack(packId, pack);

    }
    @PostMapping("/create_Pack_And_Assign_To_Stand/{idStand}")
    @ResponseBody
    public Pack createPackAndAssignToStand(@PathVariable("idStand") Long idStand, @RequestBody Pack pack){
        return packService.createPackAndAssignToStand(idStand,pack);
    }

    @PutMapping("/unassign_Stand_from_Pack/{idPack}")
    @ResponseBody
    public Pack unassignStandfromPack(@PathVariable("idPack") Long idPack){
        return packService.unassignStandfromPack(idPack);
    }

    @PutMapping("/book_Pack/{idPack}/{idUser}")
    @ResponseBody
    public Pack bookPack(@PathVariable("idPack") Long idPack,@PathVariable("idUser") Long idUser){
        return packService.bookPack(idUser,idPack);
    }

    @PutMapping("/validate_Reservation/{idPack}")
    @ResponseBody
    public Pack validate_Reservation(@PathVariable("idPack") Long idPack){
        return packService.validateReservation(idPack);
    }

    @PutMapping("/cancel_Reservation/{idPack}")
    @ResponseBody
    public Pack cancel_Reservation(@PathVariable("idPack") Long idPack){
        return packService.cancelReservation(idPack);
    }



}
