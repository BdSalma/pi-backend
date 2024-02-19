package tn.examen.templateexamen2324.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Stand implements Serializable {
    @Id
    private Long id;
    TypeStand zone;
    Boolean statut;

    @OneToOne(mappedBy = "Stand")
    Pack Pack;
}
