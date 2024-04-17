package tn.examen.templateexamen2324.services;

import tn.examen.templateexamen2324.entity.*;
import tn.examen.templateexamen2324.entity.Pack;

import java.util.HashMap;
import java.util.List;

public interface IPackService {
     Pack addPack(Pack pack);
     List<Pack> retrieveAllPacks();
     Pack retrievePackById(long id);
     void deletePack(long id);
     Pack updatePack(long id,Pack stand);
     Pack getPackById(long id);
     Pack createPackAndAssignToStand(long idStand , Pack pack);
     Pack unassignStandfromPack(Long idPack);

     List<Pack> findPackByStatut(Boolean statut);
     Pack bookPack(String userId, Long packId);

     List<Pack> retrieveAllPacksByForum(Forum forum);
     Pack validateReservation(Long packId);
     Pack cancelReservation(Long packId);
     void reservationNotice();

     List<Pack> findPackByTypePackAndReservationStatus(TypePack typePack, ReservationStatus reservationStatus);

     Pack assignStandToPack(long idStand , Pack pack);

     Pack createPersonlizedPackPrice(Pack pack,String UserId, Long standId);

     List<User> getListOfParticipants();

     HashMap<String,HashMap<String,Float>>getPackStatistics();
}