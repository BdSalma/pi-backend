package tn.examen.templateexamen2324.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Pack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private TypePack typePack;
    private float prix;
    private Boolean statut;
    @ManyToOne(cascade = CascadeType.ALL)
    private Forum Forum;
    @OneToOne(cascade = CascadeType.ALL)
    private Stand Stand;
    @ManyToOne(cascade = CascadeType.ALL)
    private User reserver;
    private LocalDate reservationDate;
    private LocalDate validationDate;
    private ReservationStatus reservationStatus = ReservationStatus.Not_Reserved;
    private int numberOfOffers;
    private int numberOfBadges;
    private int numberOfFlyers;
    private boolean displayLogo;
    private boolean insertFlyer;

}
