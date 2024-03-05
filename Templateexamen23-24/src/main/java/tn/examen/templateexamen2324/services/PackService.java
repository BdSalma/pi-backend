package tn.examen.templateexamen2324.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.entity.*;
import tn.examen.templateexamen2324.repository.ForumRepo;
import tn.examen.templateexamen2324.repository.PackRepo;
import tn.examen.templateexamen2324.repository.StandRepo;
import tn.examen.templateexamen2324.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Service
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
        Forum forum  = this.forumRepo.findForumByForumStatus(ForumStatus.In_Progress);
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
    public Pack bookPack(Long userId, Long packId) {

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
        pack.setReservationDate(date);
        return  this.packRepo.save(pack);
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


}
