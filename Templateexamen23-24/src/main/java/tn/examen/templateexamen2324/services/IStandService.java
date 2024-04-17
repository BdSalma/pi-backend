package tn.examen.templateexamen2324.services;

import tn.examen.templateexamen2324.entity.Stand;

import java.util.List;

public interface IStandService {
     Stand addStand(Stand stand);
     List<Stand> retrieveAllStand();
     Stand retrieveStandById(long id);
     void deleteStand(long id);
     Stand updateStand(long id,Stand stand);

     List<Stand> findStandByStatut(Boolean statut);

     Stand  findStandById(Long idStand);
}