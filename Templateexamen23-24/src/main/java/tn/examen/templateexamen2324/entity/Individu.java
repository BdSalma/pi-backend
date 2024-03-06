package tn.examen.templateexamen2324.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Individu")
public class Individu extends User implements Serializable {
    private String identity;
    public String firstName;
    public String lastName;
    @Column(name = "individu_role")
    @Enumerated(EnumType.STRING)
    private IndividuRole role;


    @ManyToMany(cascade=CascadeType.ALL)
    private Set<Offer> offers = new HashSet<>();
    @OneToMany(mappedBy="Individu", cascade = CascadeType.ALL)
    private Set<RequestSupply> RequestSupply=new HashSet<>();

    public Individu(Map<String, String> userData) {
        this.username = userData.get("username");
        this.password = userData.get("password");
        this.email = userData.get("email");
        this.identity = userData.get("identity");
        this.firstName = userData.get("firstName");
        this.lastName = userData.get("lastName");
        this.role = IndividuRole.valueOf(userData.get("role"));
    }
}
