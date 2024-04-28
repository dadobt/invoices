package org.dadobt.casestudy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.dadobt.casestudy.controller.dto.InvoiceRequest;
import org.dadobt.casestudy.controller.dto.InvoiceResponse;
import org.dadobt.casestudy.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/invoice")
public class InvoiceController {


    final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }


    @Operation(summary = "Create an invoice")
    @ApiResponse(responseCode = "201")
    @ApiResponse(responseCode = "403")
    @ApiResponse(responseCode = "500")
    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> create(@RequestPart("invoice") @Valid InvoiceRequest invoiceRequest,
                                    @RequestPart("file") @Valid MultipartFile file, Principal principal) {


        try {
            Optional<InvoiceResponse> invoice = invoiceService.createInvoice(invoiceRequest, file);
            if (invoice.isPresent()) {
                return new ResponseEntity<>(invoice, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            //happens when it cannot write to disc for some reason
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @Operation(summary = "Create an invoice")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "403")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "500")
    @RequestMapping(value = "/duedays/{days}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getInvoicesDueDays(@PathVariable Long days) {

        Optional<List<InvoiceResponse>> invoicesBySpecificSupplier = invoiceService.getInvoicesThatAreDueInXdays(days);
        if (invoicesBySpecificSupplier.isPresent()) {
            return new ResponseEntity<>(invoicesBySpecificSupplier, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "retrieves invoices associated with a specific supplier")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "403")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "500")
    @RequestMapping(value = "/supplier/{id}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getInvoicesBySpecificSupplier(@PathVariable Long id) {

        Optional<List<InvoiceResponse>> invoicesBySpecificSupplier = invoiceService.findInvoicesBySpecificSupplier(id);
        if (invoicesBySpecificSupplier.isPresent()) {
            return new ResponseEntity<>(invoicesBySpecificSupplier, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @Operation(summary = "the total sum of invoices for a given supplier")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "500")
    @RequestMapping(value = "/supplier/{id}/total", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTotal(@PathVariable Long id) {
        BigDecimal bigDecimal = invoiceService.theTotalSumOfInvoicesForGivenSupplier(id);
        return new ResponseEntity<>(bigDecimal, HttpStatus.OK);
    }
}
