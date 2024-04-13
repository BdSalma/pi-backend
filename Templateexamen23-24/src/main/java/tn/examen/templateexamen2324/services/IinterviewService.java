package tn.examen.templateexamen2324.services;

import tn.examen.templateexamen2324.entity.Candidature;
import tn.examen.templateexamen2324.entity.Interview;

import java.util.List;

public interface IinterviewService {
    Interview FindInterviewById(Long id);
    Interview addInter(Interview i, Long id,int roomNum);
    Interview addInterEnligne(Interview i, Long id);
    Interview updateInterview(Long id, Interview updatedInterv);
     Interview updateInterviewR(Long id, Interview updatedInterv,int roomNum);
    List<Interview>  getInterviews();
    Interview addInterview(Interview i, Long candidatureId);
    void deleteById(Long id);
}
