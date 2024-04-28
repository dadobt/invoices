package org.dadobt.casestudy.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class InvoiceResponse {

        @Schema(description = "id of invoice" )
        Long id;
        //Date the invoice is sent
        @Schema(description = "billingDate" )
        LocalDateTime billingDate;

        @Schema(description = "customerAddress")
          String customerAddress;

        @Schema(description = "deliveryDate")
        LocalDateTime deliveryDate;

        @Schema(description = "dueDate")
        LocalDateTime dueDate;

        @Schema(description = "specification")
        String specification;

        @Schema(description = "price")
        BigDecimal price;

        //The applicable VAT rate(s) and the amount of VAT to be paid
        @Schema(description = "VATRate")
        BigDecimal vatRate;

        @Schema(description = "total")
        BigDecimal total;

        @Schema(description = "dueDate")
        String contactDetails;

        //VAT number is your VAT registration number
        @Schema(description = "dueDate")
        String vatnumber;

        //Bankgiro/plusgiro number to which payment is made
        @Schema(description = "BankgiroNumber")
        String bankgiroNumber;

        @Schema(description = "supplierId")
        private Long supplierId;

}
