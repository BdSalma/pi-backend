package tn.examen.templateexamen2324.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.entity.Pack;
import tn.examen.templateexamen2324.entity.ReservationStatus;
import tn.examen.templateexamen2324.entity.Stand;
import tn.examen.templateexamen2324.entity.User;
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
    StandRepo standRepo;

    @Autowired
    UserRepository userRepository;

    @Override
    public Pack addPack(Pack stand) {

        return packRepo.save(stand);
    }

    @Override
    public List<Pack> retrieveAllPacks() {
        return packRepo.findAll();
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
        return packRepo.save(pack);
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


}
