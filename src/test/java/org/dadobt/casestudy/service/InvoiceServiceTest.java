package org.dadobt.casestudy.service;

import org.dadobt.casestudy.controller.dto.InvoiceRequest;
import org.dadobt.casestudy.controller.dto.InvoiceResponse;
import org.dadobt.casestudy.repository.InvoiceRepository;
import org.dadobt.casestudy.models.Invoice;
import org.dadobt.casestudy.models.Supplier;
import org.dadobt.casestudy.repository.SupplierRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class InvoiceServiceTest {

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private DocumentService documentService;

    @InjectMocks
    private InvoiceService invoiceService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateInvoiceSuccess() throws NoSuchAlgorithmException, IOException {


        InvoiceRequest request = new InvoiceRequest();
        request.setBillingDate(LocalDateTime.now());
        request.setCustomerAddress("home");
        request.setDeliveryDate(LocalDateTime.now());
        request.setDueDate(LocalDateTime.now());
        request.setSpecification("A test specification");
        request.setPrice(125L);
        request.setVatRate(BigDecimal.valueOf(12));
        request.setContactDetails("contact details");
        request.setVatnumber("some vat number ");
        request.setBankgiroNumber("somebankgironubmer");
        request.setSupplierId(Long.valueOf("123"));

        Supplier supplier = new Supplier();
        supplier.setId(1L);
        supplier.setName("Test Supplier");
        supplier.setEmail("test@gmail.com");
        supplier.setAddress("test adress");
        supplier.setPhone("46709999911");

        Invoice savedInvoice = new Invoice();
        savedInvoice.setBillingDate(Timestamp.from(Instant.now()));
        savedInvoice.setCustomerAddress("home");
        savedInvoice.setDeliveryDate(Timestamp.from(Instant.now()));
        savedInvoice.setDueDate(Timestamp.from(Instant.now()));
        savedInvoice.setSpecification("A test specification");
        savedInvoice.setPrice(BigDecimal.valueOf(125L));
        savedInvoice.setVatRate(BigDecimal.valueOf(12));
        savedInvoice.setTotal(BigDecimal.valueOf(140));
        savedInvoice.setContactDetails("contact details");
        savedInvoice.setBankgiroNumber("somebankgironubmer");
        savedInvoice.setSupplier(supplier);

        MultipartFile file = mock(MultipartFile.class);

        when(supplierRepository.findById(any())).thenReturn(Optional.of(supplier));
        when(invoiceRepository.save(any())).thenReturn(savedInvoice);
        doNothing().when(documentService).create(file, savedInvoice);


        Optional<InvoiceResponse> response = invoiceService.createInvoice(request, file);

        assertTrue(response.isPresent());
        assertEquals(supplier.getId(), response.get().getSupplierId());
    }

    @Test
    public void testCreateInvoiceWithNoSupplier() throws NoSuchAlgorithmException, IOException {
        InvoiceRequest request = new InvoiceRequest();
        MultipartFile file = mock(MultipartFile.class);

        when(supplierRepository.findById(request.getSupplierId())).thenReturn(Optional.empty());

        Optional<InvoiceResponse> response = invoiceService.createInvoice(request, file);

        assertFalse(response.isPresent());
    }



  }