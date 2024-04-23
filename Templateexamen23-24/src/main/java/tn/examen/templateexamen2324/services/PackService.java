package tn.examen.templateexamen2324.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.entity.*;
import tn.examen.templateexamen2324.repository.ForumRepo;
import tn.examen.templateexamen2324.repository.PackRepo;
import tn.examen.templateexamen2324.repository.StandRepo;
import tn.examen.templateexamen2324.repository.UserRepository;

import java.time.LocalDate;
import tn.examen.templateexamen2324.entity.Pack;
import tn.examen.templateexamen2324.entity.Stand;
import tn.examen.templateexamen2324.repository.PackRepo;
import tn.examen.templateexamen2324.repository.StandRepo;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class PackService implements IPackService{

    @Autowired
    PackRepo packRepo;

    @Autowired
    ForumRepo forumRepo;

    @Autowired
    StandRepo standRepo;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    UserRepository userRepository;

    @Override
    public Pack addPack(Pack pack) {
        Forum forum = this.forumRepo.findForumByForumStatus(ForumStatus.In_Progress);
        forum.getPack().add(pack);
        forumRepo.save(forum);
        pack.setForum(forum);
        if (pack.getTypePack() == TypePack.Diamond || pack.getTypePack() == TypePack.Platinum){
            pack.setDisplayLogo(true);
        }

        return packRepo.save(pack);
    }

    @Override
    public List<Pack> retrieveAllPacks() {
        Forum f = forumRepo.findForumByForumStatus(ForumStatus.In_Progress);
        if(f != null){
            return (List<Pack>) f.getPack();
        }
        return packRepo.findAll();
    }

    @Override
    public List<Pack> retrieveAllPacksByForum(Forum forum) {
        return packRepo.findPackByForum(forum.getId());
    }

    @Override
    public Pack retrievePackById(long id) {
        return packRepo.findById(id).get();
    }

    @Override
    public void deletePack(long id) {
        packRepo.deleteById(id);
    }

    @Override
    public Pack updatePack(long id, Pack pack) {
        Pack p = packRepo.findById(id).get();
        p.setPrix(pack.getPrix());
        p.setTypePack(pack.getTypePack());
        return packRepo.save(p);
    }

    @Override
    public Pack getPackById(long id) {
        return  packRepo.findById(id).get();
    }

    @Override
    public Pack createPackAndAssignToStand(long idStand, Pack pack) {
        Stand stand = this.standRepo.findById(idStand).get();
        if(stand.getReserved() == false){
            stand.setReserved(true);
            Forum f = forumRepo.findForumByForumStatus(ForumStatus.In_Progress);
            f.getPack().add(pack);
            pack.setForum(f);
            standRepo.save(stand);
            pack.setStand(stand);
        }
        return  this.packRepo.save(pack);
    }

    @Override
    public Pack unassignStandfromPack(Long idPack) {
        Pack pack = this.packRepo.findById(idPack).get();
        pack.getStand().setReserved(false);
        standRepo.save( pack.getStand());
        pack.setStand(null);

        return this.packRepo.save(pack);
    }

    @Override
    public   List<Pack> findPackByStatut(Boolean statut) {
        return packRepo.findPackByStatut(statut);
    }


    @Override
    public Pack bookPack(String userId, Long packId) {
        Pack pack = this.packRepo.findById(packId).get();
        if(pack.getReservationStatus() == ReservationStatus.Not_Reserved){
            User user = this.userRepository.findById(userId).get();
            pack.setReserver(user);
            LocalDate date = LocalDate.now();
            pack.setReservationDate(date);
            pack.setReservationStatus(ReservationStatus.On_Hold);
            return this.packRepo.save(pack);
        }
        return this.packRepo.save(pack);
    }

    @Override
    public Pack validateReservation(Long packId) {
        Pack pack = this.packRepo.findById(packId).get();
        pack.setReservationStatus(ReservationStatus.Reserved);
        LocalDate date = LocalDate.now();
        pack.setValidationDate(date);
       // this.sendSimpleEmail(pack.getReserver().getEmail(), "Validation de reservation", " La reservation que vous avez effectué est validée");
        return this.packRepo.save(pack);

    }

    @Override
    public Pack cancelReservation(Long packId) {
        Pack pack = this.packRepo.findById(packId).get();
        pack.setReservationStatus(ReservationStatus.Not_Reserved);
        this.sendSimpleEmail(pack.getReserver().getEmail(), "Annulation de reservation", " La reservation que vous avez effectué est annulée");
        pack.setReservationDate(null);
        pack.setReserver(null);

        return  this.packRepo.save(pack);
    }

    @Override
    public void reservationNotice() {

    }

    @Override
    public List<Pack> findPackByTypePackAndReservationStatus(TypePack typePack, ReservationStatus reservationStatus) {
        return this.packRepo.findPackByTypePackAndReservationStatus(typePack,reservationStatus);
    }


    @Override
    public Pack assignStandToPack(long idStand, Pack pack) {
        Stand s = this.standRepo.findById(idStand).get();
        pack.setStand(s);
        return  this.packRepo.save(pack);
    }

    @Override
    public Pack createPersonlizedPackPrice(Pack pack, String UserId, Long standId) {
        User u = this.userRepository.findById(UserId).get();
        Forum f = forumRepo.findForumByForumStatus(ForumStatus.In_Progress);
        Stand s = this.standRepo.findById(standId).get();
            pack.setTypePack(TypePack.Personalized);
            pack.setStand(s);
            pack.setDisplayLogo(true);
            pack.setInsertFlyer(true);
            pack.setReserver(u);
            pack.setForum(f);
            LocalDate date = LocalDate.now();
            pack.setReservationDate(date);
            pack.setReservationStatus(ReservationStatus.On_Hold);
            s.setPack(pack);
            s.setReserved(true);

            float price =0;
            if(s.getZone()== TypeStand.Zone1){
                price = price +500;
            } else if (s.getZone()== TypeStand.Zone2) {
                price = price +1000;
            }else{
                price = price+1500;
            }
            if(pack.isDisplayLogo()){
                price = price + 200;
            }
            if (pack.getNumberOfFlyers()>0){
                if (pack.isInsertFlyer()) {
                    price = price + 300;
                }
            }
            if (pack.getNumberOfBadges() > 0) {
                price += pack.getNumberOfBadges() * 25;
            }
            if (pack.getNumberOfFlyers() > 0) {
                price += pack.getNumberOfFlyers() * 30;
            }
            if (pack.getNumberOfOffers() > 0) {
                price += pack.getNumberOfOffers() * 90;
            }
            pack.setPrix(price);
            this.packRepo.save(pack);
            this.standRepo.save(s);
            f.getPack().add(pack);
            this.forumRepo.save(f);
            this.sendSimpleEmail(u.getEmail(), "Prix de votre stand personalisé", " le prix de votre stand est de l'ordre de "+price);
            return pack;

    }

    @Override
    public List<User> getListOfParticipants() {
        Forum currentForum = this.forumRepo.findForumByForumStatus(ForumStatus.In_Progress);
        ArrayList<User> listofParticipants = new ArrayList<>();
        if(currentForum != null){
            for (Pack p: currentForum.getPack()) {
                if(p.getReservationStatus() == ReservationStatus.Reserved && p.isDisplayLogo()){
                    listofParticipants.add(p.getReserver());
                }
            }
        }else {
            List<Forum> forums = this.forumRepo.findAll();
            for (Forum f : forums) {
                for (Pack p: f.getPack()) {
                    if (p.getReserver() != null && p.isDisplayLogo()){
                        if(!listofParticipants.contains(p.getReserver())){
                            listofParticipants.add(p.getReserver());
                        }
                    }
                }
            }
        }
        return listofParticipants;
    }

    @Override
    public HashMap<String, HashMap<String, Float>> getPackStatistics() {
        HashMap<String, HashMap<String, Float>> statistics = new HashMap<>();
        List<Pack> packs = this.packRepo.findAll();
        List<Pack> packsDiamond = new ArrayList<>();
        List<Pack> packsPersonalized = new ArrayList<>();
        List<Pack> packsSilver = new ArrayList<>();
        List<Pack> packsGold = new ArrayList<>();
        List<Pack> packsPlatinum = new ArrayList<>();

        for (Pack p : packs) {
            switch (p.getTypePack()) {
                case Diamond:
                    packsDiamond.add(p);
                    break;
                case Platinum:
                    packsPlatinum.add(p);
                    break;
                case Silver:
                    packsSilver.add(p);
                    break;
                case Personalized:
                    packsPersonalized.add(p);
                    break;
                case Gold:
                    packsGold.add(p);
                    break;
            }
        }

        // Calculate revenue and percentages
        int totalPacks = packs.size();

        // Gold Pack Calculation
        HashMap<String, Float> goldStats = new HashMap<>();
        float goldRevenue = calculateRevenue(packsGold);
        float goldPercentage = ((float) packsGold.size() / totalPacks) * 100;
        goldStats.put("Revenue", goldRevenue);
        goldStats.put("Percentage", goldPercentage);
        statistics.put("Gold", goldStats);

        // Silver Pack Calculation
        HashMap<String, Float> silverStats = new HashMap<>();
        float silverRevenue = calculateRevenue(packsSilver);
        float silverPercentage = ((float) packsSilver.size() / totalPacks) * 100;
        silverStats.put("Revenue", silverRevenue);
        silverStats.put("Percentage", silverPercentage);
        statistics.put("Silver", silverStats);

        // Platinum Pack Calculation
        HashMap<String, Float> platinumStats = new HashMap<>();
        float platinumRevenue = calculateRevenue(packsPlatinum);
        float platinumPercentage = ((float) packsPlatinum.size() / totalPacks) * 100;
        platinumStats.put("Revenue", platinumRevenue);
        platinumStats.put("Percentage", platinumPercentage);
        statistics.put("Platinum", platinumStats);

        // Diamond Pack Calculation
        HashMap<String, Float> diamondStats = new HashMap<>();
        float diamondRevenue = calculateRevenue(packsDiamond);
        float diamondPercentage = ((float) packsDiamond.size() / totalPacks) * 100;
        diamondStats.put("Revenue", diamondRevenue);
        diamondStats.put("Percentage", diamondPercentage);
        statistics.put("Diamond", diamondStats);

        // Personalized Pack Calculation
        HashMap<String, Float> personalizedStats = new HashMap<>();
        float personalizedRevenue = calculateRevenue(packsPersonalized);
        float personalizedPercentage = ((float) packsPersonalized.size() / totalPacks) * 100;
        personalizedStats.put("Revenue", personalizedRevenue);
        personalizedStats.put("Percentage", personalizedPercentage);
        statistics.put("Personalized", personalizedStats);

        return statistics;
    }


    private float calculateRevenue(List<Pack> packs) {
        float revenue = 0;
        for (Pack p : packs) {
            revenue += p.getPrix();
        }
        return revenue;
    }

    //@Scheduled(cron = "0 0 8 * * *")
    @Scheduled(fixedRate = 30000)// Run at 8:00 AM every day
    public void notificationManagment() {
        Forum forumInProgress = forumRepo.findForumByForumStatus(ForumStatus.In_Progress);
        if (forumInProgress != null) { // Check if there is a forum in progress
            LocalDate currentDate = LocalDate.now();
            long daysLeftUntilForum = ChronoUnit.DAYS.between(currentDate, forumInProgress.getDate());
            if (daysLeftUntilForum >= 1) {
                List<Pack> packs = packRepo.findPackByForum(forumInProgress.getId());
                for (Pack pack : packs) {
                    if (pack.getReservationStatus() == ReservationStatus.On_Hold && pack.getReservationDate() != null) {
                        long daysDifference = ChronoUnit.DAYS.between(pack.getReservationDate(), currentDate);
                        if (daysDifference <= 3) {
                            // sendSimpleEmail(pack.getReserver().getEmail(), "Rappel de reservation", "Merci de valider votre reservation");
                            log.info("Email Sent to rappel");
                        } else if (daysDifference >= 7) {
                            cancelReservation(pack.getId());
                            log.info("reservation cacelled to cancel");
                        }
                    }
                }
            } else if (daysLeftUntilForum <= 0) {
                List<Pack> packs = packRepo.findPackByForum(forumInProgress.getId());
                for (Pack p: packs) {
                    p.setReservationStatus(ReservationStatus.Archived);
                }
                forumInProgress.setForumStatus(ForumStatus.Done);

            }
        }
    }

    public void sendSimpleEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("walahamdi0@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        mailSender.send(message);
        System.out.println("Mail Send...");
    }
}