package org.dadobt.casestudy.service;

import org.dadobt.casestudy.controller.dto.InvoiceRequest;
import org.dadobt.casestudy.controller.dto.InvoiceResponse;
import org.dadobt.casestudy.models.Invoice;
import org.dadobt.casestudy.models.Supplier;
import org.dadobt.casestudy.repository.InvoiceRepository;
import org.dadobt.casestudy.repository.SupplierRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");


    private final InvoiceRepository invoiceRepository;
    private final SupplierRepository supplierRepository;
    private final DocumentService documentService;

    public InvoiceService(InvoiceRepository invoiceRepository, SupplierRepository supplierRepository, DocumentService documentService) {
        this.invoiceRepository = invoiceRepository;
        this.supplierRepository = supplierRepository;
        this.documentService = documentService;
    }


    public Optional<InvoiceResponse> createInvoice(InvoiceRequest invoiceRequest, MultipartFile file) throws NoSuchAlgorithmException, IOException {

        Optional<Supplier> supplierOptional = supplierRepository.findById(invoiceRequest.getSupplierId());
        if (supplierOptional.isPresent()) {
            Invoice invoice = new Invoice();
            invoice.setBankgiroNumber(invoiceRequest.getBankgiroNumber());
            invoice.setBillingDate(Timestamp.valueOf(invoiceRequest.getBillingDate()));
            invoice.setCustomerAddress(invoiceRequest.getCustomerAddress());
            invoice.setDeliveryDate(Timestamp.valueOf(invoiceRequest.getDeliveryDate()));
            invoice.setDueDate(Timestamp.valueOf(invoiceRequest.getDueDate()));
            invoice.setSpecification(invoiceRequest.getSpecification());

            invoice.setPrice(BigDecimal.valueOf(invoiceRequest.getPrice()));
            invoice.setVatRate(invoiceRequest.getVatRate());
            invoice.setContactDetails(invoiceRequest.getContactDetails());
            BigDecimal totalWithTax = calculateTotalWithTax(invoiceRequest.getPrice(), invoiceRequest.getVatRate());
            invoice.setTotal(totalWithTax);
            invoice.setVatNumber(invoiceRequest.getVatnumber());
            invoice.setBankgiroNumber(invoiceRequest.getBankgiroNumber());
            invoice.setSupplier(supplierOptional.get());
            Invoice savedInvoice = invoiceRepository.save(invoice);
            InvoiceResponse invoiceResponse = createInvoiceResponse(savedInvoice);
            if (file != null) {
                documentService.create(file, savedInvoice);
            }
            return Optional.of(invoiceResponse);

        } else {
            return Optional.empty();
        }

    }

    private BigDecimal calculateTotalWithTax(Long price, BigDecimal vatRate) {
        BigDecimal base = BigDecimal.valueOf(price);
        BigDecimal taxable = base;
        return base.add(taxable.multiply(vatRate).divide(ONE_HUNDRED, RoundingMode.HALF_UP));
    }

    public Optional<List<InvoiceResponse>> findInvoicesBySpecificSupplier(Long id) {
        Optional<List<Invoice>> allBySupplier = invoiceRepository.findBySupplier_Id(id);
        if (allBySupplier.isPresent() && !allBySupplier.get().isEmpty()) {
            List<InvoiceResponse> invoiceResponseList = new ArrayList<>();
            for (Invoice invoice : allBySupplier.get()) {
                InvoiceResponse invoiceResponse = createInvoiceResponse(invoice);
                invoiceResponseList.add(invoiceResponse);
            }
            return Optional.of(invoiceResponseList);
        } else {
            return Optional.empty();
        }
    }

    protected static @NotNull InvoiceResponse createInvoiceResponse(Invoice invoice) {
        InvoiceResponse invoiceResponse = new InvoiceResponse();
        invoiceResponse.setId(invoice.getIDInvoiceNumber());
        invoiceResponse.setBillingDate(invoice.getBillingDate().toLocalDateTime());
        invoiceResponse.setCustomerAddress(invoice.getCustomerAddress());
        invoiceResponse.setDeliveryDate(invoice.getDeliveryDate().toLocalDateTime());
        invoiceResponse.setDueDate(invoice.getDueDate().toLocalDateTime());
        invoiceResponse.setSpecification(invoice.getSpecification());
        invoiceResponse.setPrice(invoice.getPrice());
        invoiceResponse.setVatRate(invoice.getVatRate());
        invoiceResponse.setContactDetails(invoice.getContactDetails());
        invoiceResponse.setTotal(invoice.getTotal());
        invoiceResponse.setContactDetails(invoice.getContactDetails());
        invoiceResponse.setVatnumber(invoice.getVatNumber());
        invoiceResponse.setBankgiroNumber(invoice.getBankgiroNumber());
        invoiceResponse.setSupplierId(invoice.getSupplier().getId());
        return invoiceResponse;
    }

    public Optional<List<InvoiceResponse>> getInvoicesThatAreDueInXdays(Long days) {

        Instant instant = Instant.now();
        Instant xDays = instant.atOffset(ZoneOffset.UTC)
                .with(LocalTime.of(23, 59, 59, 0)).plusDays(days)
                .toInstant();
        Timestamp xDaysTimestamp = Timestamp.from(xDays);
        Optional<List<Invoice>> byDueDateBefore = invoiceRepository.findByDueDateBefore(xDaysTimestamp);
        List<InvoiceResponse> invoiceResponseList = new ArrayList<>();
        if (byDueDateBefore.isPresent()) {
            for (Invoice invoice : byDueDateBefore.get()) {
                InvoiceResponse invoiceResponse = createInvoiceResponse(invoice);
                invoiceResponseList.add(invoiceResponse);
            }
        }
        return Optional.of(invoiceResponseList);
    }

    public BigDecimal theTotalSumOfInvoicesForGivenSupplier(Long id) {
        Optional<List<Invoice>> bySupplierId = invoiceRepository.findBySupplier_Id(id);
        BigDecimal total = BigDecimal.ZERO;
        if (bySupplierId.isPresent()) {
            total = bySupplierId.get().stream().map(Invoice::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return total;
    }
}