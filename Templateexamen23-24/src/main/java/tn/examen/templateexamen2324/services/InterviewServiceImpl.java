package tn.examen.templateexamen2324.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.examen.templateexamen2324.dao.CandidatureRepo;
import tn.examen.templateexamen2324.dao.InterviewRepo;
import tn.examen.templateexamen2324.entity.Candidature;
import tn.examen.templateexamen2324.entity.Interview;

@Slf4j
@Service
public class InterviewServiceImpl implements IinterviewService {
    @Autowired
    InterviewRepo irepo;
    @Autowired
    CandidatureRepo crepo;
    @Override
    public Interview addInter(Interview i,Long id)
    {
        Candidature candidature = crepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Candidature not found"));
        i.setCandidature(candidature);
        return irepo.save(i);
    }

}