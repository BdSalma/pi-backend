package tn.examen.templateexamen2324.dao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tn.examen.templateexamen2324.entity.Offer;

@Repository
public interface OfferRepo extends CrudRepository<Offer,Long> {
}
