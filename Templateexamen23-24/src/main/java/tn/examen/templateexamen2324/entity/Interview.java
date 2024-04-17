package tn.examen.templateexamen2324.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
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
    @Enumerated(EnumType.STRING)
    private EtatInterview etatInterview;
    @Enumerated(EnumType.STRING)
    private InterviewType interviewType;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime date;
    private String Lien;
    private String titre;
    // @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL,mappedBy="Interview")
    private Candidature Candidature;
    @JsonBackReference
    @OneToMany(cascade = CascadeType.ALL,mappedBy="Interview")
    private Set<Room> Room = new HashSet<>();
}