package tn.examen.templateexamen2324.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sponsors implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSponsor;
    private String name;
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private String website;
    private String logoUrl;
    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    private User User;
}
