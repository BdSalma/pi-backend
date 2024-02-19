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
public class Room implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRoom;
    @Temporal(TemporalType.DATE)
    private int num;
    private String status;

    @ManyToOne(cascade = CascadeType.ALL)
    Interview Interview;
}
