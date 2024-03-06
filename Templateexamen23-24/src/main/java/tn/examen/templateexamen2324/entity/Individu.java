package tn.examen.templateexamen2324.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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


    @OneToMany(mappedBy="individu", cascade = CascadeType.ALL)
    private Set<RequestSupply> requestSupplies=new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL,mappedBy="individu")
    private Set<Candidature> candidatures = new HashSet<>();
}
