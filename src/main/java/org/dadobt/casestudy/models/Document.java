package org.dadobt.casestudy.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "size")
    private long size = 0;

    @Column(name = "hash", nullable = false, unique = true)
    private String hash;

    @ManyToOne
    @JoinColumn(name = "IDInvoiceNumber",referencedColumnName = "IDInvoiceNumber")
    private Invoice invoice;
}