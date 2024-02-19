package tn.examen.templateexamen2324.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

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

    @ManyToOne(cascade = CascadeType.ALL)
    private User User;
}