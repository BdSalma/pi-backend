package tn.examen.templateexamen2324.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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
    private  long idRequestSupply;
    public int quantity;
    public String category;
    public  String description;
    public Date date;
    public int validity;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "RequestSupply")
    private Set<Devis> Devis = new HashSet<>();
    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private Invoice Invoice;
    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private Individu Individu;
}