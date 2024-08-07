package tn.examen.truetech.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Phone implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPhone;
    private String name;
    private String image;

    @OneToMany(cascade = CascadeType.ALL,mappedBy="phone")
    private Set<Model> models = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private User  user;

}