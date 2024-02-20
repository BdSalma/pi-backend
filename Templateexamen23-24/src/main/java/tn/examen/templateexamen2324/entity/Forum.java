package tn.examen.templateexamen2324.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Forum  implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private LocalDate date ;
    private String localisation ;
    private String description ;
    private ForumStatus forumStatus = ForumStatus.In_Progress;

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    Set<User> User = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL,mappedBy="Forum")
    @JsonIgnore
    Set<Pack> Pack = new HashSet<>();
}
