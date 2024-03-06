package tn.examen.templateexamen2324.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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


    @OneToMany(mappedBy="individu", cascade = CascadeType.ALL)
    private Set<RequestSupply> requestSupplies=new HashSet<>();

    @ManyToMany(cascade=CascadeType.ALL)
    private Set<Offer> offers = new HashSet<>();


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
