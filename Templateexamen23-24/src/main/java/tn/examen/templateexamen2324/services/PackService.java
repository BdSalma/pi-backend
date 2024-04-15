package tn.examen.templateexamen2324.services;


import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.ArrayList;
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
    UserRepository userRepository;

    @Override
    public Pack addPack(Pack pack) {
        Forum forum = this.forumRepo.findForumByForumStatus(ForumStatus.In_Progress);
        forum.getPack().add(pack);
        forumRepo.save(forum);
        pack.setForum(forum);
        return packRepo.save(pack);
    }

    @Override
    public List<Pack> retrieveAllPacks() {
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
        return this.packRepo.save(pack);

    }

    @Override
    public Pack cancelReservation(Long packId) {
        Pack pack = this.packRepo.findById(packId).get();
        pack.setReservationStatus(ReservationStatus.Not_Reserved);
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
        //if(this.packRepo.countPackByReserverAndForum(u,f) ==0){
            if(pack.getTypePack()== TypePack.Personalized){
                pack.setStand(s);
                pack.setReserver(u);
                LocalDate date = LocalDate.now();
                pack.setReservationDate(date);
                pack.setReservationStatus(ReservationStatus.On_Hold);
                s.setPack(pack);
                s.setReserved(true);

                float price =0;
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
                return pack;
            }
      //  }
       return null;
    }


    @Scheduled(cron = "0 0 8 * * *") // Run at 8:00 AM every day
    public void NotifyReservationDDL() {
        Forum f = this.forumRepo.findForumByForumStatus(ForumStatus.In_Progress);
        LocalDate d = LocalDate.now();
        LocalDate reservationDDL = f.getDate().minusDays(4); // Set reservation deadline to four days before the forum date
        List<Pack> packs = this.packRepo.findPackByForum(f.getId());
        for (Pack p : packs) {
            if (p.getStatut() == false && p.getReservationStatus() == ReservationStatus.On_Hold) {
                if (p.getReservationDate().isEqual(reservationDDL) && p.getReservationDate().isBefore(f.getDate().minusDays(3))) {
                    log.info(p.getReserver().getEmail());
                }
            }
        }
    }


}
