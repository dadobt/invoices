package org.dadobt.casestudy.models;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "invoice")
public class Invoice {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long IDInvoiceNumber;
    Timestamp billingDate;
    String customerAddress;
    Timestamp deliveryDate;
    Timestamp dueDate;
    String specification;
    BigDecimal price;
    BigDecimal vatRate;
    BigDecimal total;
    String contactDetails;
    String vatNumber;
    String BankgiroNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    private Supplier supplier;
}