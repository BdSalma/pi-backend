package tn.examen.truetech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.examen.truetech.entity.Phone;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {
}
