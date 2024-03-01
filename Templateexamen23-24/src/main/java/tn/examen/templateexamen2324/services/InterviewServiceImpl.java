package tn.examen.templateexamen2324.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.dao.CandidatureRepo;
import tn.examen.templateexamen2324.dao.InterviewRepo;
import tn.examen.templateexamen2324.dao.RoomRepo;
import tn.examen.templateexamen2324.entity.Candidature;
import tn.examen.templateexamen2324.entity.Interview;
import tn.examen.templateexamen2324.entity.Room;

import java.util.List;

@Slf4j
@Service
public class InterviewServiceImpl implements IinterviewService {
    @Autowired
    InterviewRepo irepo;
    @Autowired
    CandidatureRepo crepo;
    @Autowired
    RoomRepo roomRepo;
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public Interview addInter(Interview i, Long candidatureId, int roomNum) {
        Candidature candidature = crepo.findById(candidatureId)
                .orElseThrow(() -> new IllegalArgumentException("Candidature not found"));
        i.setCandidature(candidature);
        if (candidature.getInterview() == null) {
            candidature.setInterview(i);
        }

            Room room = roomRepo.findRoomByNum(roomNum);
            i.getRoom().add(room);
            room.setInterview(i);


        return irepo.save(i);
    }


    @Override
    public List<Interview> getInterviews() {
        return (List<Interview>)irepo.findAll();
    }
    @Override
    public void deleteById(Long id) {
        irepo.deleteById(id);
    }

}