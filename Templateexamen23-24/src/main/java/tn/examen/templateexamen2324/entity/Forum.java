package tn.examen.templateexamen2324.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Forum  implements Serializable{
    @Id
    private int id ;
    LocalDate dateDebut ;
    String localisation ;
    String description ;

    @ManyToMany(cascade = CascadeType.ALL)
    Set<User> User = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL,mappedBy="Forum")
    Set<Pack> Pack = new HashSet<>();
}
