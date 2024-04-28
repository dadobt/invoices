package org.dadobt.casestudy.controller.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;


@Data
public class ContractResponce implements Serializable {
    Long id ;
    String name;
    String description;
    Timestamp signed;
    Timestamp expires;
    SupplierResponse supplierResponse;
}