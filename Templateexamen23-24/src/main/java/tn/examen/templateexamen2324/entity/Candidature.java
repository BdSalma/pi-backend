package tn.examen.templateexamen2324.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Candidature implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCandidature;
    @Temporal(TemporalType.DATE)
    private Date date;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String cv;
    private String lettre;
    @OneToOne(cascade = CascadeType.ALL)
    private Interview Interview;
    @ManyToOne(cascade = CascadeType.ALL)
    private Offer offer;

}