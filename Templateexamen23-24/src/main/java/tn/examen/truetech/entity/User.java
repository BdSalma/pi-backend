package tn.examen.truetech.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private String password;
    private Role role;
    private int number;

    @OneToMany(cascade = CascadeType.ALL,mappedBy="user")
    private Set<Phone> phones = new HashSet<>();

}
