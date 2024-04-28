package org.dadobt.casestudy.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierRequest implements Serializable {
    @Schema(description = "name")
    String name;
    @Schema(description = "address")
    String address;
    @Schema(description = "phone")
    String phone;
    @Schema(description = "email")
    String email;

}