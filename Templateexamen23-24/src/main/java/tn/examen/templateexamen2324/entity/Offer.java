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

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Offer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOffre;
    @Temporal(TemporalType.DATE)
    private Date dateEmission;
    private String offerName;
    @Enumerated(EnumType.STRING)
    private Category OffreCategory;
    private int Candidatnumber;
    private String candidatProfil;
    private String duree;
    private String description;
    @OneToMany(cascade = CascadeType.ALL,mappedBy="offer")
    private Set<Candidature> candidatures = new HashSet<>();
    @ManyToOne(cascade = CascadeType.ALL)
    private Society Society;
    @ManyToMany(cascade=CascadeType.ALL, mappedBy="offers")
    private Set<Individu> Individus = new HashSet<>();

}