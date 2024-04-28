package org.dadobt.casestudy.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierResponse implements Serializable {

    Long id;
    @Schema(description = "name")
    String name;
    @Schema(description = "address")
    String address;
    @Schema(description = "phone")
    String phone;
    @Schema(description = "email")
    String email;

}