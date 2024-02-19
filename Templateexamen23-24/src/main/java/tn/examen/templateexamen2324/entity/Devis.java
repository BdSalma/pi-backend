package tn.examen.templateexamen2324.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Devis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private float price;
    private int quantity;
    private String description;
    private String file;
    private boolean status;

    @ManyToOne(cascade = CascadeType.ALL)
    private RequestSupply RequestSupply;
    @ManyToOne(cascade = CascadeType.ALL)
    private Society Society;
}