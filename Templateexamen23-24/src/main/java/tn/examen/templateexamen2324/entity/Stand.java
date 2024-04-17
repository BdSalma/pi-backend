package tn.examen.templateexamen2324.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private TypeStand zone;
    private int number;
    private Boolean reserved = false;
    @OneToOne(mappedBy = "Stand")
    @JsonIgnore
    private Pack Pack;
    @ManyToOne(cascade = CascadeType.ALL)
    private Forum Forum;
}