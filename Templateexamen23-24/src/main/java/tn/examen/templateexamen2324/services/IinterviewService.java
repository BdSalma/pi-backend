package tn.examen.templateexamen2324.services;

import tn.examen.templateexamen2324.entity.Interview;

import java.util.List;

public interface IinterviewService {
    Interview addInter(Interview i, Long id,int roomNum);
    Interview addInterEnligne(Interview i, Long id);

    List<Interview>  getInterviews();
    void deleteById(Long id);
}
