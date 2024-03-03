package tn.examen.templateexamen2324.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Society")
public class Society extends User implements Serializable {
    private int matricule;
    public String logo;
    public String adresse;
    private String representative;
    private String sector;
    public String sitFin;
    @Column(name = "society_role")
    @Enumerated(EnumType.STRING)
    private SocietyRole societyRole;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "Society")
    private Set<Devis> Devis = new HashSet<>();
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "society")
    private Set<Offer> Offers = new HashSet<>();

    public Society(Map<String, String> userData) {
        this.username = userData.get("username");
        this.password = userData.get("password");
        this.email = userData.get("email");
        this.matricule = Integer.parseInt(userData.get("matricule"));
        this.logo = userData.get("logo");
        this.adresse = userData.get("adresse");
        this.representative = userData.get("representative");
        this.sector = userData.get("sector");
        this.sitFin = userData.get("sitFin");
        this.societyRole = SocietyRole.valueOf(userData.get("role"));
    }
}