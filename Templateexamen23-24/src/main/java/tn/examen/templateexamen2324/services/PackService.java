package tn.examen.templateexamen2324.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.entity.Pack;
import tn.examen.templateexamen2324.entity.Stand;
import tn.examen.templateexamen2324.repository.PackRepo;
import tn.examen.templateexamen2324.repository.StandRepo;

import java.util.List;

@Service
public class PackService implements IPackService{

    @Autowired
    PackRepo packRepo;

    @Autowired
    StandRepo standRepo;

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
    public Pack assignStandToPack(long idStand, Pack pack) {
       Stand s = this.standRepo.findById(idStand).get();
       pack.setStand(s);
       return  this.packRepo.save(pack);
    }


}
