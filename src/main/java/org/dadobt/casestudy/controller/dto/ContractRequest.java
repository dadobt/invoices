package org.dadobt.casestudy.controller.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;



@Data
public class ContractRequest implements Serializable {
    String name;
    String description;
    Timestamp signed;
    Timestamp expires;
    Long supplierID;
}