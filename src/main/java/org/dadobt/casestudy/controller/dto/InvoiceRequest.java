package org.dadobt.casestudy.controller.dto;

import lombok.Data;


import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class InvoiceRequest{

        //Date the invoice is sent
        @Schema(description = "billingDate" )
        @NotNull(message = "billingDate cannot be blank")
        LocalDateTime billingDate;

        @Schema(description = "customerAddress")
        @NotBlank(message = "customerAddress cannot be blank")
        String customerAddress;

        @Schema(description = "deliveryDate")
        @NotNull(message = "deliveryDate cannot be blank")
        LocalDateTime deliveryDate;

        @Schema(description = "dueDate")
        @NotNull(message = "dueDate cannot be blank")
        LocalDateTime dueDate;

        @Schema(description = "specification")
        @NotBlank(message = "specification cannot be blank")
        String specification;

        @Schema(description = "price")
        Long price;

        //The applicable VAT rate(s) and the amount of VAT to be paid
        @Schema(description = "VATRate")
        BigDecimal vatRate;


        @Schema(description = "dueDate")
        @NotNull (message = "dueDate cannot be blank")
        String contactDetails;

        //VAT number is your VAT registration number
        @Schema(description = "dueDate")
        @NotBlank(message = "dueDate cannot be blank")
        String vatnumber;

        //Bankgiro/plusgiro number to which payment is made
        @Schema(description = "BankgiroNumber")
        @NotBlank(message = "BankgiroNumber cannot be blank")
        String bankgiroNumber;

        @Schema(description = "Supplier")
        @NotNull(message = "Supplier cannot be null")
        Long supplierId;
}
