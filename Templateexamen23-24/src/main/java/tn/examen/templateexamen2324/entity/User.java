package tn.examen.templateexamen2324.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany(cascade = CascadeType.ALL,mappedBy = "User")
    Set<Forum> Forum = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "User")
    Set<Reclamation> Reclamation = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "User")
    Set<Sponsors> Sponsors = new HashSet<>();
}
