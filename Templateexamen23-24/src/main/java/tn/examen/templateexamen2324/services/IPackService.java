package tn.examen.templateexamen2324.services;

import tn.examen.templateexamen2324.entity.Pack;
import tn.examen.templateexamen2324.entity.User;

import java.util.List;

public interface IPackService {
     Pack addPack(Pack pack);
     List<Pack> retrieveAllPacks();
     Pack retrievePackById(long id);
     void deletePack(long id);
     Pack updatePack(long id,Pack stand);

     Pack createPackAndAssignToStand(long idStand , Pack pack);
     Pack unassignStandfromPack(Long idPack);

     Pack bookPack(Long userId, Long packId);

     Pack validateReservation(Long packId);

}
