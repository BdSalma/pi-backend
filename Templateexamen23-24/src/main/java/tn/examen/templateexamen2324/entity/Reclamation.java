package tn.examen.templateexamen2324.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.apache.james.mime4j.dom.datetime.DateTime;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Entity
@Table( name = "Reclamation")
@Getter()
@Setter()
@NoArgsConstructor()
@AllArgsConstructor()
public class Reclamation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String description;
    TypeReclamation typeReclamation;
    boolean review;
    String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));



    //@JsonIgnore
    //@ToString.Include
    @ManyToOne(cascade = CascadeType.ALL)
    //@JsonIgnoreProperties
    private User User;


    @OneToMany(cascade = CascadeType.ALL,mappedBy = "reclamation", orphanRemoval = true)
   // @JsonIgnore
    private Set<Favorite> favorites = new HashSet<>();

    // Getter and setter methods for ratings
    @Getter
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "reclamation", orphanRemoval = true)
    // @JsonIgnore
    private Set<Rating> ratings = new HashSet<>();

}