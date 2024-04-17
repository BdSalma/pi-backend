package tn.examen.templateexamen2324.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bouncycastle.tsp.ers.SortedHashList;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private String theme;
    private String description ;
    @JsonIgnore
    private String affiche;
    private ForumStatus forumStatus = ForumStatus.In_Progress;

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    Set<User> User = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL,mappedBy="Forum")
    @JsonIgnore
    List<Pack> Pack = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,mappedBy="Forum")
    @JsonIgnore
    List<Stand> Stand = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,mappedBy="forum")
    @JsonIgnore
    Set<Offer> offres = new HashSet<>();
}

