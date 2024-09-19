package tn.examen.templateexamen2324.controller;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import tn.examen.templateexamen2324.entity.*;
import tn.examen.templateexamen2324.entity.Pack;
import tn.examen.templateexamen2324.entity.Stand;
import tn.examen.templateexamen2324.repository.ForumRepo;
import tn.examen.templateexamen2324.repository.PackRepo;
import tn.examen.templateexamen2324.repository.StandRepo;
import tn.examen.templateexamen2324.services.IPackService;
import tn.examen.templateexamen2324.services.IStandService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/pack")
@CrossOrigin(origins="http://localhost:4200")
public class PackController {

    @Autowired
    IPackService packService;

    @Autowired
    StandRepo standRepo;

    @Autowired
    ForumRepo forumRepo;

    @Autowired
    PackRepo packRepo;

    @GetMapping("/find-all-packs")
    @ResponseBody
    public List<Pack> getPacks() {
        List<Pack> listPack = packService.retrieveAllPacks();
        return listPack;
    }

    @GetMapping("/getPacksStatistics")
    @ResponseBody
    public HashMap<String,HashMap<String,Float>> getPacksStatistics() {
        return packService.getPackStatistics();
    }

    @GetMapping("/getListOfParticipants")
    @ResponseBody
    public List<User> getListOfParticipants() {
        List<User> listParticipants = packService.getListOfParticipants();
        return listParticipants;
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

    @PostMapping("/add-barcha-pack/{forumId}")
    @ResponseBody
    public void addbarchapacks(@PathVariable("forumId") long forumId) {
        Forum f = this.forumRepo.findById(forumId).get();
        List<Stand> stands = f.getStand();
        LocalDate d = f.getDate();
        Random random = new Random();

        // Create a copy of the pack list to avoid ConcurrentModificationException
        List<Pack> packsCopy = new ArrayList<>(f.getPack());

        for (Stand s : stands) {
            Pack p = new Pack();
            p.setForum(f);
            p.setStand(s);
            float price = 0;

            // Calculate price based on stand zone
            switch(s.getZone()) {
                case Zone1:
                    price += 500;
                    break;
                case Zone2:
                    price += 1000;
                    break;
                case Zone3:
                    price += 1500;
                    break;
            }

            // Set forum, type pack, and other attributes
            int randomIndex = random.nextInt(TypePack.values().length);
            TypePack randomType = TypePack.values()[randomIndex];
            p.setTypePack(randomType);

            // Update price based on type pack and other attributes
            switch(randomType) {
                case Diamond:
                    price += 2500;
                    p.setDisplayLogo(true);
                    p.setInsertFlyer(true);
                    p.setNumberOfBadges(5);
                    p.setNumberOfOffers(8);
                    break;
                case Platinum:
                    price += 3500;
                    p.setDisplayLogo(true);
                    p.setInsertFlyer(true);
                    p.setNumberOfBadges(5);
                    p.setNumberOfOffers(10);
                    break;
                case Silver:
                    price += 1000;
                    break;
                case Gold:
                    price += 2000;
                    break;
                case Personalized:
                    p.setNumberOfOffers(random.nextInt(16) + 5); // Random number between 5 and 20
                    p.setNumberOfFlyers(random.nextInt(171) + 30); // Random number between 30 and 200
                    p.setNumberOfBadges(random.nextInt(8) + 3); // Random number between 3 and 10
                    p.setInsertFlyer(random.nextBoolean()); // Randomly pick true or false
                    p.setDisplayLogo(random.nextBoolean()); // Randomly pick true or false

                    // Calculate personalized price
                    price += calculatePersonalizedPrice(p);
                    break;
            }

            // Set the calculated price
            p.setPrix(price);

            // Save the pack
            this.packRepo.save(p);

            // Add the pack to the copied list
            packsCopy.add(p);
        }

        // Update the forum with the new pack list
        f.setPack(packsCopy);
        this.forumRepo.save(f);
    }

    // Method to calculate personalized price
    private float calculatePersonalizedPrice(Pack pack) {
        float personalizedPrice = 0;
        if (pack.getNumberOfBadges() > 0) {
            personalizedPrice += pack.getNumberOfBadges() * 25;
        }
        if (pack.getNumberOfFlyers() > 0) {
            personalizedPrice += pack.getNumberOfFlyers() * 30;
        }
        if (pack.getNumberOfOffers() > 0) {
            personalizedPrice += pack.getNumberOfOffers() * 90;
        }
        return personalizedPrice;
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
    @PostMapping("/createPersonalizedPackPrice/{standId}")
    public Pack createPersonalizedPackPrice(@RequestBody Pack pack, Authentication authentication, @PathVariable("standId") Long standId) {
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        return packService.createPersonlizedPackPrice(pack, userId, standId);
    }

    @PutMapping("/book_Pack/{idPack}")
    @ResponseBody
    public Pack bookPack(@PathVariable("idPack") Long idPack, Authentication authentication){
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        String userId = jwtToken.getClaim("sub");
        return packService.bookPack(userId,idPack);
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

    @DeleteMapping("/delete-pack/{id}")
    @ResponseBody
    public void deletePack(@PathVariable("id") int packId) {
        packService.deletePack(packId);
    }

    @PutMapping("/update-pack/{id}")
    @ResponseBody
    public Pack updateBloc(@PathVariable("id") int packId, @RequestBody Pack pack) {
        return packService.updatePack(packId, pack);

    }}