package tn.examen.templateexamen2324.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idInvoice;
    public  String description;
    public  String file;
    @Enumerated(EnumType.STRING)
    private InvoiceStatus status = InvoiceStatus.In_progress;
    public  String comment;

    @OneToOne(cascade = CascadeType.ALL,mappedBy = "invoice")
    @JsonIgnore
    private RequestSupply requestSupply;
}
