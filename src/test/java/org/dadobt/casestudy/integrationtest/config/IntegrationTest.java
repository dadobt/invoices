package org.dadobt.casestudy.integrationtest.config;

import org.dadobt.casestudy.Main;
import org.dadobt.casestudy.controller.dto.*;
import org.dadobt.casestudy.models.Contract;
import org.dadobt.casestudy.models.Invoice;
import org.dadobt.casestudy.models.Supplier;
import org.dadobt.casestudy.repository.ContractRepository;
import org.dadobt.casestudy.repository.InvoiceRepository;
import org.dadobt.casestudy.repository.SupplierRepository;

import org.dadobt.casestudy.repository.DocumentRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(profiles = "integration")
@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class IntegrationTest {

    private static final String HTTP_LOCALHOST = "http://localhost:";

    @Value("${server.port}")
    private int port;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private DocumentRepository documentRepository;

    protected String getBaseUrl() {
        return HTTP_LOCALHOST + port;
    }

    @Autowired
    public TestRestTemplate restTemplate;

    public AuthenticationRequest createAuthenticateRequest() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("user");
        authenticationRequest.setPassword("password");
        return authenticationRequest;
    }

    public String authenticateUser() {
        AuthenticationRequest applicationRequest = createAuthenticateRequest();
        ResponseEntity<AuthenticationResponse> responseEntity = restTemplate.postForEntity(getBaseUrl() + "/authenticate", applicationRequest, AuthenticationResponse.class);
        AuthenticationResponse response = responseEntity.getBody();
        return response.getJwt();
    }

    public static InvoiceRequest createInvoiceRequest(Long id) {
        InvoiceRequest invoiceRequest = new InvoiceRequest();
        invoiceRequest.setBankgiroNumber("test");
        invoiceRequest.setBillingDate(Timestamp.from(Instant.now().plus(1,ChronoUnit.DAYS)).toLocalDateTime());
        invoiceRequest.setCustomerAddress("test address");
        invoiceRequest.setDeliveryDate(Timestamp.from(Instant.now()).toLocalDateTime());
        invoiceRequest.setDueDate(Timestamp.from(Instant.now().plus(2,ChronoUnit.DAYS)).toLocalDateTime());
        invoiceRequest.setSpecification("specification");

        invoiceRequest.setPrice(123L);
        invoiceRequest.setVatRate(BigDecimal.valueOf(12));
        invoiceRequest.setContactDetails("contact details");
        invoiceRequest.setVatnumber("vat number ");
        invoiceRequest.setBankgiroNumber("some string bankgiro number");
       invoiceRequest.setSupplierId(id);
       return invoiceRequest;
    }

    public static SupplierRequest createSupplierRequest() {
        SupplierRequest request = new SupplierRequest();
        request.setName("test");
        request.setEmail("test@gmail.com");
        request.setAddress("test adress");
        request.setPhone("46709999911");
        return request;
    }

    public static ContractRequest createContractRequest(Long id) {
        ContractRequest contractRequest = new ContractRequest();
        contractRequest.setSupplierID(id);
        contractRequest.setName("Test Contract");
        contractRequest.setDescription("Test Description");
        contractRequest.setExpires(Timestamp.from(Instant.now().plus(5, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS)));
        contractRequest.setSigned(Timestamp.from(Instant.now().plus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS)));

        return contractRequest;
    }

    public Invoice createInvoiceInDB(Supplier supplier){
        Invoice invoice = new Invoice();
        invoice.setSupplier(supplier);
        invoice.setBillingDate(Timestamp.from(Instant.now().truncatedTo(ChronoUnit.DAYS)));
        invoice.setDeliveryDate(Timestamp.from(Instant.now().plus(1,ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS)));
        invoice.setDueDate(Timestamp.from(Instant.now().plus(2,ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS)));
        invoice.setCustomerAddress("address");
        invoice.setContactDetails("details");
        invoice.setBankgiroNumber("testtest");
        invoice.setPrice(BigDecimal.valueOf(125));
        invoice.setVatRate(BigDecimal.valueOf(12));
        invoice.setTotal(BigDecimal.valueOf(140));
        return invoiceRepository.save(invoice);
    }

    public Invoice createInvoice2InDB(Supplier supplier){
        Invoice invoice = new Invoice();
        invoice.setSupplier(supplier);
        invoice.setBillingDate(Timestamp.from(Instant.now().truncatedTo(ChronoUnit.DAYS)));
        invoice.setDeliveryDate(Timestamp.from(Instant.now().plus(1,ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS)));
        invoice.setDueDate(Timestamp.from(Instant.now().plus(2,ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS)));
        invoice.setCustomerAddress("address 2");
        invoice.setContactDetails("details 2");
        invoice.setBankgiroNumber("testtest 2");
        invoice.setPrice(BigDecimal.valueOf(1250));
        invoice.setVatRate(BigDecimal.valueOf(12));
        invoice.setTotal(BigDecimal.valueOf(1400));
        return invoiceRepository.save(invoice);
    }

    public Supplier createSupplierInDB() {
        Supplier supplier = new Supplier();
        supplier.setName("Test Supplier");
        supplier.setEmail("test@gmail.com");
        supplier.setAddress("test adress");
        supplier.setPhone("46709999911");
        return supplierRepository.save(supplier);
    }

    public Contract createContractInDB(Supplier supplier) {
        Contract contract = new Contract();
        contract.setSupplier(supplier);
        contract.setName("Test Contract");
        contract.setDescription("Test Description");
        contract.setExpires(Timestamp.from(Instant.now().plus(5, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS)));
        return contractRepository.save(contract);
    }


    public Contract createContract2InDB(Supplier supplier) {
        Contract contract = new Contract();
        contract.setSupplier(supplier);
        contract.setName("Test Contract2 ");
        contract.setDescription("Test Description2");
        contract.setExpires(Timestamp.from(Instant.now().plus(3, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS)));
        return contractRepository.save(contract);
    }


    public void deleteAllInvoices() {
        invoiceRepository.deleteAll();
    }

    public void deleteAllSuppliers() {
        supplierRepository.deleteAll();
    }

    public void deleteAllContracts() {
        contractRepository.deleteAll();
    }

    public void deleteAllDocuments() {
        documentRepository.deleteAll();
    }

}
