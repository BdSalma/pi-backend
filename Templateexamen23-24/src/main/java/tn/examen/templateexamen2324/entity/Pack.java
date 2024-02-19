package tn.examen.templateexamen2324.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Pack {
    @Id
    private Long id;
    TypePack typePack;
    float prix;
    Boolean statut;

    @ManyToOne(cascade = CascadeType.ALL)
    Forum Forum;
    @OneToOne(cascade = CascadeType.ALL)
    Stand Stand;
}

