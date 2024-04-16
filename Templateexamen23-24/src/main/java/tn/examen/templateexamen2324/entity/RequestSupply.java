package tn.examen.templateexamen2324.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RequestSupply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int idRequestSupply;
    public int quantity;
    public String category;
    public  String description;
    public LocalDate date;
    public int validity;
    @Enumerated(EnumType.STRING)
    private RequestSupplyStatus status = RequestSupplyStatus.Running;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "requestSupply")
    @JsonIgnore
    private Set<Devis> devis = new HashSet<>();
    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private Invoice invoice;
    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private Individu individu;
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    //@JsonIgnore
    private Forum forum;
}