package org.dadobt.casestudy.models;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contract")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;
    String name;
    String description;
    Timestamp signed;
    Timestamp expires;

    @ManyToOne(fetch = FetchType.LAZY)
    private Supplier supplier;
}
