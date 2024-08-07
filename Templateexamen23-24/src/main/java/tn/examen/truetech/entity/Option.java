package tn.examen.truetech.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Option implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOption;
    private String title;
    private String image;
    private String description;
    private Long clientPrice;
    private Long supplierPrice;
    private int quantity;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Model model;
}
