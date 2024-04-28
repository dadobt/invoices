package org.dadobt.casestudy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.dadobt.casestudy.controller.dto.ContractRequest;
import org.dadobt.casestudy.controller.dto.ContractResponce;
import org.dadobt.casestudy.controller.dto.SupplierResponse;
import org.dadobt.casestudy.service.SupplierService;
import org.dadobt.casestudy.controller.dto.SupplierRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/supplier")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @Operation(summary = "Create an supplier")
    @ApiResponse(responseCode = "201")
    @ApiResponse(responseCode = "403")
    @ApiResponse(responseCode = "409")
    @ApiResponse(responseCode = "500")
    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createSupplier(@RequestBody SupplierRequest supplierRequest, Principal principal) {

        Optional<SupplierResponse> supplier = supplierService.createSupplier(supplierRequest);
        if (supplier.isPresent()) {
            return new ResponseEntity<>(supplier, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

    }


    @Operation(summary = "Get a supplier")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "403")
    @ApiResponse(responseCode = "500")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getSupplier(@PathVariable("id") Long id, Principal principal) {
        Optional<SupplierResponse> supplier = supplierService.getSupplier(id);
        if (supplier.isPresent()) {
            return new ResponseEntity<>(supplier, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }


    @Operation(summary = "Create an contract")
    @ApiResponse(responseCode = "201")
    @ApiResponse(responseCode = "403")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "409")
    @ApiResponse(responseCode = "500")
    @RequestMapping(value = "/{id}/create/contract", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createContractForGivenSupplier(@PathVariable("id") Long id, @RequestBody ContractRequest contractRequest, Principal principal) {
        Optional<ContractResponce> contract = supplierService.createContract(contractRequest, id);
        if (contract.isPresent()) {
            return new ResponseEntity<>(contract, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

    }

    @Operation(summary = "Get Contracts For Given Supplier")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "403")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "500")
    @RequestMapping(value = "/{id}/get/contract", method = RequestMethod.GET, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getContractsForGivenSupplier(@PathVariable("id") Long id, Principal principal) {
        Optional<ArrayList<ContractResponce>> allContractsForGivenSupplier = supplierService.getAllContractsForGivenSupplier(id);
        if (allContractsForGivenSupplier.isPresent()) {
            return new ResponseEntity<>(allContractsForGivenSupplier, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get Number  For invoices Supplier")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "403")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "500")
    @RequestMapping(value = "/{id}/get/invoicesNumber", method = RequestMethod.GET, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getInvoicesNumber(@PathVariable("id") Long id, Principal principal) {
        Optional<Long> count = supplierService.countAllInvoicesForAGivenSupplier(id);
        if (count.isPresent()) {
            return new ResponseEntity<>(count, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
