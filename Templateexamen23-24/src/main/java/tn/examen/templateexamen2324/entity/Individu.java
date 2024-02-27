package tn.examen.templateexamen2324.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Individu")
public class Individu extends User implements Serializable {
    @Id
    @Column(name = "id")
    private String id;
    private String identity;
    public String firstName;
    public String lastName;
    @Enumerated(EnumType.STRING)
    private IndividuRole role;



    @OneToMany(mappedBy="Individu", cascade = CascadeType.ALL)
    private Set<RequestSupply> RequestSupply=new HashSet<>();
}
