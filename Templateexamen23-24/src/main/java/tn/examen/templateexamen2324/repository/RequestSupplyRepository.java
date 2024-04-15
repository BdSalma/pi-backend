package tn.examen.templateexamen2324.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.examen.templateexamen2324.entity.Forum;
import tn.examen.templateexamen2324.entity.RequestSupply;

import java.util.List;

@Repository
public interface RequestSupplyRepository extends JpaRepository<RequestSupply, Integer> {

    List<RequestSupply> findByForum(Forum forum);
}
