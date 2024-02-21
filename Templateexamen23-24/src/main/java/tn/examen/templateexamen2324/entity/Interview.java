package tn.examen.templateexamen2324.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties("candidature")
public class Interview implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInterview;
    @Temporal(TemporalType.DATE)
    private Date date;
   // @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL,mappedBy="Interview")
    private Candidature Candidature;
    @OneToMany(cascade = CascadeType.ALL,mappedBy="Interview")
    private Set<Room> Room = new HashSet<>();
}
