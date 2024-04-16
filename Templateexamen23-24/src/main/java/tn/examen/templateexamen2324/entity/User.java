package tn.examen.templateexamen2324.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "User")
public class User implements Serializable {
    @Id
    @Column(name = "id")
    private String id;
    public String username;
    public String password;
    public String email;
    public boolean approve;
    public boolean activate;
    private String competence;
    @ManyToMany(cascade = CascadeType.ALL,mappedBy = "User")
    @JsonIgnore
    Set<Forum> Forum = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "User")
    @JsonIgnore
    Set<Reclamation> Reclamation = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "User")
    @JsonIgnore
    Set<Sponsors> Sponsors = new HashSet<>();
    @JsonIgnore
    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL,mappedBy="individu")
    private Set<Candidature> candidatures = new HashSet<>();
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<OfferFavoris> favoriteOffers = new HashSet<>();
}
