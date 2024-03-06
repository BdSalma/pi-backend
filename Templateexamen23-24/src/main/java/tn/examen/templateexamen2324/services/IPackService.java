package tn.examen.templateexamen2324.services;

import tn.examen.templateexamen2324.entity.Pack;

import java.util.List;

public interface IPackService {
     Pack addPack(Pack pack);
     List<Pack> retrieveAllPacks();
     Pack retrievePackById(long id);
     void deletePack(long id);
     Pack updatePack(long id,Pack stand);

     Pack assignStandToPack(long idStand , Pack pack);

}
