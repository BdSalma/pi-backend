package tn.examen.templateexamen2324.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.examen.templateexamen2324.entity.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    //@Query("SELECT u FROM User u JOIN u. b JOIN b.Foyer f JOIN f.Universite u WHERE u.nomUniversite = :nomUniversite")
    //public List<Chambre> findChambresParNomUniversite(@Param("nomUniversite") String nomUniversite);


}
