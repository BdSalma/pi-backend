package tn.examen.templateexamen2324.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.entity.Stand;
import tn.examen.templateexamen2324.repository.StandRepo;

import java.util.List;

@Service
public class StandService implements IStandService{

    @Autowired
    StandRepo standRepo;

    @Override
    public Stand addStand(Stand stand) {
        return standRepo.save(stand);
    }

    @Override
    public List<Stand> retrieveAllStand() {
        return standRepo.findAll();
    }

    @Override
    public Stand retrieveStandById(long id) {
        return standRepo.findById(id).get();
    }

    @Override
    public void deleteStand(long id) {
         standRepo.deleteById(id);
    }

    @Override
    public Stand updateStand(long id, Stand stand) {
        return standRepo.save(stand);
    }



}
