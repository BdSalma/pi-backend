package tn.examen.templateexamen2324.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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
    @Enumerated(EnumType.STRING)
    private roomStatus status;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    Interview Interview;
}