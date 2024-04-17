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
    private String matricule;
    public String adresse;
    private String representative;
    private String sector;
    @Column(name = "society_role")
    @Enumerated(EnumType.STRING)
    private SocietyRole role;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "societyDevis")
    private Set<Devis> devis = new HashSet<>();
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "society")
    private Set<Offer> Offer = new HashSet<>();

    public Society(Map<String, String> userData) {
        this.username = userData.get("username");
        this.password = userData.get("password");
        this.email = userData.get("email");
        this.matricule = userData.get("matricule");
        this.image = userData.get("image");
        this.adresse = userData.get("adresse");
        this.representative = userData.get("representative");
        this.sector = userData.get("sector");
        this.role = SocietyRole.valueOf(userData.get("role"));
    }
}
