package org.dadobt.casestudy.service;

import org.dadobt.casestudy.controller.dto.ContractRequest;
import org.dadobt.casestudy.controller.dto.ContractResponce;
import org.dadobt.casestudy.controller.dto.SupplierRequest;
import org.dadobt.casestudy.controller.dto.SupplierResponse;
import org.dadobt.casestudy.models.Supplier;
import org.dadobt.casestudy.repository.ContractRepository;
import org.dadobt.casestudy.repository.InvoiceRepository;
import org.dadobt.casestudy.repository.SupplierRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SupplierServiceTest {

    private SupplierService supplierService;
    private SupplierRepository supplierRepository;
    private ContractRepository contractRepository;
    private InvoiceRepository invoiceRepository;

    @Before
    public void setUp() {
        supplierRepository = mock(SupplierRepository.class);
        contractRepository = mock(ContractRepository.class);
        invoiceRepository = mock(InvoiceRepository.class);
        supplierService = new SupplierService(supplierRepository, contractRepository, invoiceRepository);
    }

    @Test
    public void testCreateSupplier() {
        SupplierRequest request = new SupplierRequest();
        Supplier supplier = new Supplier();
        SupplierResponse expectedResponse = new SupplierResponse();
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);

        Optional<SupplierResponse> response = supplierService.createSupplier(request);
        assertTrue(response.isPresent());
        assertThat(response.get()).isEqualToComparingFieldByField(expectedResponse);


    }

    @Test
    public void testGetSupplierWhenFound() {
        Supplier savedSupplier = new Supplier();
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(savedSupplier));

        Optional<SupplierResponse> result = supplierService.getSupplier(1L);
        assertTrue(result.isPresent());
    }

    @Test
    public void testGetSupplierWhenNotFound() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<SupplierResponse> result = supplierService.getSupplier(1L);
        assertFalse(result.isPresent());
    }



    @Test
    public void testCreateContractWhenSupplierDoesNotExist() {
        ContractRequest request = new ContractRequest();
        when(supplierRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<ContractResponce> result = supplierService.createContract(request, 1L);
       assertFalse(result.isPresent());
    }


    @Test
    public void testGetAllContractsForGivenSupplierWhenNoContracts() {
        when(contractRepository.findAllBySupplier_Id(1L)).thenReturn(Optional.empty());

        Optional<ArrayList<ContractResponce>> result = supplierService.getAllContractsForGivenSupplier(1L);
        assertFalse(result.isPresent());
    }

    @Test
    public void countAllInvoicesForAGivenSupplier() {
        when(invoiceRepository.countBySupplier_Id(any())).thenReturn(Optional.of(5L));

        Optional<Long> result = supplierService.countAllInvoicesForAGivenSupplier(1L);
        assertTrue(result.isPresent());
        assertEquals(Optional.of(5L), Optional.of(result.get()));
    }
}