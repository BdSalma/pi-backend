package tn.examen.truetech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.examen.truetech.entity.Option;

import java.util.List;

@Repository
public interface OptionRepository  extends JpaRepository<Option, Long>  {
    List<Option> findByModelIdModel(Long modelId);
}
