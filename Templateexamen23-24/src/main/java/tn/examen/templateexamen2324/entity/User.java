package tn.examen.templateexamen2324.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String password;
    private String email;

    @ManyToMany(cascade = CascadeType.ALL,mappedBy = "User")
    Set<Forum> Forum = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "User")
    Set<Reclamation> Reclamation = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "User")
    Set<Sponsors> Sponsors = new HashSet<>();
}
