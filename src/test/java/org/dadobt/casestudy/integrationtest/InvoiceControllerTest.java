package org.dadobt.casestudy.integrationtest;

import org.dadobt.casestudy.controller.dto.InvoiceRequest;
import org.dadobt.casestudy.controller.dto.InvoiceResponse;
import org.dadobt.casestudy.models.Supplier;
import org.dadobt.casestudy.controller.dto.ContractResponce;
import org.dadobt.casestudy.integrationtest.config.IntegrationTest;
import org.dadobt.casestudy.models.Invoice;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InvoiceControllerTest extends IntegrationTest {


    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private static final String INVOICE_ENDPOINT = "/api/invoice";

    String url;

    String jwt;

    @Before
    public void setUp() throws Exception {

        url = getBaseUrl();
        jwt=authenticateUser();
        deleteAllDocuments();
        deleteAllInvoices();
        deleteAllContracts();
        deleteAllSuppliers();
    }


    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void create() throws IOException {
        Supplier supplierInDB = createSupplierInDB();
        InvoiceRequest invoiceRequest = createInvoiceRequest(supplierInDB.getId());
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("file",getTestFile());
        body.add("invoice",invoiceRequest);


        StringBuilder sb = new StringBuilder();
        sb.append("Bearer ").append(jwt);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", sb.toString());
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body, headers);

        ResponseEntity<InvoiceResponse> responseEntity = restTemplate.postForEntity(url + INVOICE_ENDPOINT +"/create" , requestEntity, InvoiceResponse.class);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
        InvoiceResponse responseEntityBody = responseEntity.getBody();
        assertEquals(responseEntityBody.getSupplierId(), supplierInDB.getId());
        assertEquals(responseEntityBody.getBillingDate(), invoiceRequest.getBillingDate());
        assertEquals(responseEntityBody.getBankgiroNumber(), invoiceRequest.getBankgiroNumber());
        assertEquals(responseEntityBody.getContactDetails(),invoiceRequest.getContactDetails());
        assertEquals(responseEntityBody.getCustomerAddress(),invoiceRequest.getCustomerAddress());
    }


    @Test
    public void getInvoicesDueDays() {
        Supplier supplierInDB = createSupplierInDB();
        createInvoice2InDB(supplierInDB);
        StringBuilder sb = new StringBuilder();
        sb.append("Bearer ").append(jwt);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", sb.toString());
        headers.set("Content-Type", "application/json");
        HttpEntity<ContractResponce> request = new HttpEntity<>(headers);
        ResponseEntity<List<InvoiceResponse>> exchange = restTemplate.exchange(url + INVOICE_ENDPOINT+ "/duedays/2",HttpMethod.GET, request, new ParameterizedTypeReference<List<InvoiceResponse>>() {});
        assertEquals(HttpStatus.OK,exchange.getStatusCode());
        assertEquals(exchange.getBody().size(),1l);
    }

    @Test
    public void getInvoicesBySpecificSupplier() {
        Supplier supplierInDB = createSupplierInDB();
        Invoice invoiceInDB = createInvoiceInDB(supplierInDB);
        Invoice invoice2InDB = createInvoice2InDB(supplierInDB);


        StringBuilder sb = new StringBuilder();
        sb.append("Bearer ").append(jwt);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", sb.toString());
        headers.set("Content-Type", "application/json");
        HttpEntity<ContractResponce> request = new HttpEntity<>(headers);
        ResponseEntity<List<InvoiceResponse>> exchange = restTemplate.exchange(url + INVOICE_ENDPOINT+ "/supplier/"+supplierInDB.getId(), HttpMethod.GET, request, new ParameterizedTypeReference<List<InvoiceResponse>>() {});
        assertEquals(exchange.getStatusCode(),HttpStatus.OK);
        List<InvoiceResponse> body = exchange.getBody();
        assertNotNull(body);
        assertEquals(body.size(),2);
        InvoiceResponse invoiceResponse = body.get(0);
        InvoiceResponse invoiceResponse1 = body.get(1);

        assertEquals(invoiceResponse.getCustomerAddress(),invoiceInDB.getCustomerAddress());
        assertEquals(invoiceResponse1.getCustomerAddress(),invoice2InDB.getCustomerAddress());
        assertEquals(invoiceResponse.getSpecification(),invoiceInDB.getSpecification());
        assertEquals(invoiceResponse1.getSpecification(),invoice2InDB.getSpecification());
        assertEquals(invoiceResponse.getSupplierId(),invoiceInDB.getSupplier().getId());
        assertEquals(invoiceResponse1.getSupplierId(),invoice2InDB.getSupplier().getId());
        assertEquals(invoiceResponse.getBankgiroNumber(),invoiceInDB.getBankgiroNumber());
        assertEquals(invoiceResponse1.getBankgiroNumber(),invoice2InDB.getBankgiroNumber());



    }

    @Test
    public void getTotal() {
        Supplier supplierInDB = createSupplierInDB();
        Invoice invoiceInDB = createInvoiceInDB(supplierInDB);
        Invoice invoice2InDB = createInvoice2InDB(supplierInDB);

        StringBuilder sb = new StringBuilder();
        sb.append("Bearer ").append(jwt);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", sb.toString());
        headers.set("Content-Type", "application/json");
        HttpEntity<ContractResponce> request = new HttpEntity<>(headers);
        ResponseEntity<String> exchange = restTemplate.exchange(url + INVOICE_ENDPOINT + "/supplier/"+ supplierInDB.getId() +"/total" , HttpMethod.GET, request, String.class);
        assertEquals(HttpStatus.OK,exchange.getStatusCode());
        assertEquals("1540.00",exchange.getBody());
    }

    public static Resource getTestFile() throws IOException {
        Path testFile = Files.createTempFile("test-file", ".txt");
        Files.write(testFile, "Invoice invoice 123.".getBytes());
        return new FileSystemResource(testFile.toFile());
    }
}