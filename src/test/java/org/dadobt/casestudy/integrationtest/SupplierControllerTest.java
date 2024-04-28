package org.dadobt.casestudy.integrationtest;

import org.dadobt.casestudy.controller.dto.ContractRequest;
import org.dadobt.casestudy.controller.dto.ContractResponce;
import org.dadobt.casestudy.controller.dto.SupplierRequest;
import org.dadobt.casestudy.controller.dto.SupplierResponse;
import org.dadobt.casestudy.models.Contract;
import org.dadobt.casestudy.models.Supplier;
import org.dadobt.casestudy.integrationtest.config.IntegrationTest;
import org.dadobt.casestudy.models.Invoice;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SupplierControllerTest extends IntegrationTest {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private static final String SUPPLIER_ENDPOINT = "/api/supplier";

    String url;

    String jwt;

    @Before
    public void setUp() throws Exception {

        url = getBaseUrl();
        jwt=authenticateUser();
        deleteAllInvoices();
        deleteAllContracts();
        deleteAllSuppliers();
        deleteAllDocuments();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void createSupplier() {

        SupplierRequest supplierRequest = createSupplierRequest();
        StringBuilder sb = new StringBuilder();
        sb.append("Bearer ").append(jwt);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", sb.toString());
        headers.set("Content-Type", "application/json");
        HttpEntity<SupplierRequest> postRequest = new HttpEntity<>(supplierRequest, headers);
        ResponseEntity<SupplierResponse> responseEntity = restTemplate.postForEntity(url + SUPPLIER_ENDPOINT +"/create" , postRequest, SupplierResponse.class);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
        SupplierResponse body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals(body.getEmail(), supplierRequest.getEmail());
        assertEquals(body.getName(), supplierRequest.getName());
        assertEquals(body.getAddress(), supplierRequest.getAddress());
        assertEquals(body.getPhone(),supplierRequest.getPhone());
    }


    @Test
    public void getSupplier() {
        Supplier supplierInDB = createSupplierInDB();

        StringBuilder sb = new StringBuilder();
        sb.append("Bearer ").append(jwt);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", sb.toString());
        headers.set("Content-Type", "application/json");

        HttpEntity<SupplierResponse> request = new HttpEntity<>(headers);
        ResponseEntity<SupplierResponse> exchange = restTemplate.exchange(url + SUPPLIER_ENDPOINT + "/"+supplierInDB.getId(), HttpMethod.GET, request, SupplierResponse.class);
        SupplierResponse body = exchange.getBody();
        assertNotNull(body);
        assertEquals(body.getEmail(), supplierInDB.getEmail());
        assertEquals(body.getName(), supplierInDB.getName());
        assertEquals(body.getAddress(), supplierInDB.getAddress());
        assertEquals(body.getPhone(),supplierInDB.getPhone());


    }


    @Test
    public void getSupplierByIdNotFound() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bearer ").append(jwt);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", sb.toString());
        headers.set("Content-Type", "application/json");
        HttpEntity<SupplierResponse> request = new HttpEntity<>(headers);
        ResponseEntity<SupplierResponse> exchange = restTemplate.exchange(url + SUPPLIER_ENDPOINT + "/1", HttpMethod.GET, request, SupplierResponse.class);
        assertEquals(exchange.getStatusCode(), HttpStatus.NOT_FOUND);
    }



    @Test
    public void createContractForGivenSupplier() {
        SupplierRequest supplierRequest = createSupplierRequest();
        StringBuilder sb = new StringBuilder();
        sb.append("Bearer ").append(jwt);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", sb.toString());
        headers.set("Content-Type", "application/json");
        HttpEntity<SupplierRequest> postRequest = new HttpEntity<>(supplierRequest, headers);
        ResponseEntity<SupplierResponse> responseEntity = restTemplate.postForEntity(url + SUPPLIER_ENDPOINT +"/create" , postRequest, SupplierResponse.class);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
        SupplierResponse body = responseEntity.getBody();
        Long id = body.getId();

        ContractRequest contractRequest = createContractRequest(id);
        HttpEntity<ContractRequest> postRequestForContract = new HttpEntity<>(contractRequest, headers);
        ResponseEntity<ContractResponce> exchange = restTemplate.postForEntity(url + SUPPLIER_ENDPOINT + "/"+id+"/create/contract", postRequestForContract, ContractResponce.class);
        ContractResponce contractBody = exchange.getBody();
        assertEquals(contractBody.getName(),contractRequest.getName());
        assertEquals(contractBody.getDescription(),contractRequest.getDescription());
        assertEquals(contractBody.getExpires(),contractRequest.getExpires());
        assertEquals(contractBody.getSupplierResponse().getId(),id);

    }



    @Test
    public void getContractsForGivenSupplier() {
        // create supplier in db
        Supplier supplierInDB = createSupplierInDB();
        //create contracts in db
        Contract contractInDB = createContractInDB(supplierInDB);
        Contract contract2InDB = createContract2InDB(supplierInDB);

        StringBuilder sb = new StringBuilder();
        sb.append("Bearer ").append(jwt);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", sb.toString());
        headers.set("Content-Type", "application/json");
        HttpEntity<ContractResponce> request = new HttpEntity<>(headers);
        ResponseEntity<List<ContractResponce>> exchange = restTemplate.exchange(url + SUPPLIER_ENDPOINT + "/"+supplierInDB.getId()+"/get/contract", HttpMethod.GET, request, new ParameterizedTypeReference<List<ContractResponce>>() {});
        List<ContractResponce>contractResponceList = exchange.getBody();
        assertNotNull(contractResponceList);
        assertEquals(contractResponceList.size(),2);
        ContractResponce contractResponce1 = contractResponceList.get(0);
        ContractResponce contractResponce2 = contractResponceList.get(1);
        assertEquals(contractResponce1.getName(),contractInDB.getName());
        assertEquals(contractResponce2.getName(),contract2InDB.getName());
        assertEquals(contractResponce1.getDescription(),contractInDB.getDescription());
        assertEquals(contractResponce2.getDescription(),contract2InDB.getDescription());




    }

    @Test
    public void getContractsForGivenSupplierNotFound() {
        // create supplier in db
        Supplier supplierInDB = createSupplierInDB();
        //create contracts in db
        Contract contractInDB = createContractInDB(supplierInDB);
        Contract contract2InDB = createContract2InDB(supplierInDB);

        StringBuilder sb = new StringBuilder();
        sb.append("Bearer ").append(jwt);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", sb.toString());
        headers.set("Content-Type", "application/json");
        HttpEntity<ContractResponce> request = new HttpEntity<>(headers);
        ResponseEntity<List<ContractResponce>> exchange = restTemplate.exchange(url + SUPPLIER_ENDPOINT + "/1000000/get/contract", HttpMethod.GET, request, new ParameterizedTypeReference<List<ContractResponce>>() {});
        assertEquals(exchange.getStatusCode(), HttpStatus.NOT_FOUND);

    }

    @Test
    public void getInvoicesNumber() {

        Supplier supplierInDB = createSupplierInDB();
        Invoice invoiceInDB = createInvoiceInDB(supplierInDB);
        Invoice invoice2InDB = createInvoice2InDB(supplierInDB);


        StringBuilder sb = new StringBuilder();
        sb.append("Bearer ").append(jwt);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", sb.toString());
        headers.set("Content-Type", "application/json");
        HttpEntity<ContractResponce> request = new HttpEntity<>(headers);
        ResponseEntity<Long> exchange = restTemplate.exchange(url + SUPPLIER_ENDPOINT + "/"+supplierInDB.getId()+"/get/invoicesNumber", HttpMethod.GET, request, Long.class);
        assertEquals(exchange.getStatusCode(),HttpStatus.OK);
        assertEquals(exchange.getBody(),2L);
    }
}