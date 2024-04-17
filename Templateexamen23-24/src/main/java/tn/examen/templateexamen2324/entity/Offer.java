package tn.examen.templateexamen2324.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
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
    private Date dateEmission = new Date();
    private String offerName;
    @Enumerated(EnumType.STRING)
    private Category offreCategory;
    private int Candidatnumber;
    private String candidatProfil;
    private String duree;
    private EtatOffer etatOffer;
    private String description;
    private Integer favoris;
    private String file;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime date1;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime date2;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime date3;
    @JsonBackReference
    @OneToMany(cascade = CascadeType.ALL,mappedBy="offer")
    private Set<Candidature> candidatures = new HashSet<>();
    @ManyToOne
    private Society society;
    @ManyToMany(cascade=CascadeType.ALL, mappedBy="offers")
    private Set<Individu> Individus = new HashSet<>();
    @ManyToOne
    private Forum forum;
    @JsonIgnore
    @OneToMany(cascade=CascadeType.ALL,mappedBy = "offer")
    private Set<OfferFavoris> favoriteOffers = new HashSet<>();
}