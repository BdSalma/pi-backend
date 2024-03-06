package tn.examen.templateexamen2324.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Society")
public class Society extends User implements Serializable {
    @Id
    @Column(name = "id")
    private String id;
    private int matricule;
    public String logo;
    public String adresse;
    private String owner;
    private String sector;
    public String sitFin;
    @Enumerated(EnumType.STRING)
    private SocietyRole role;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "society")
    private Set<Devis> devis = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "Society")
    private Set<Offer> Offer = new HashSet<>();
}
