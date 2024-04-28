package org.dadobt.casestudy.service;

import org.dadobt.casestudy.models.Contract;
import org.dadobt.casestudy.models.Supplier;
import org.dadobt.casestudy.repository.ContractRepository;
import org.dadobt.casestudy.repository.InvoiceRepository;
import org.dadobt.casestudy.repository.SupplierRepository;
import org.dadobt.casestudy.controller.dto.ContractRequest;
import org.dadobt.casestudy.controller.dto.ContractResponce;
import org.dadobt.casestudy.controller.dto.SupplierRequest;
import org.dadobt.casestudy.controller.dto.SupplierResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final ContractRepository contractRepository;
    private final InvoiceRepository invoiceRepository;

    public SupplierService(SupplierRepository supplierRepository, ContractRepository contractRepository, InvoiceRepository invoiceRepository) {
        this.supplierRepository = supplierRepository;
        this.contractRepository = contractRepository;
        this.invoiceRepository = invoiceRepository;
    }

    /**
     * @param supplierRequest
     * @return
     */
    public Optional<SupplierResponse> createSupplier(SupplierRequest supplierRequest) {
        Supplier supplier = createSupplierEntity(supplierRequest);
        Supplier savedSuppier = supplierRepository.save(supplier);
        return Optional.of(createResponseDTO(savedSuppier));
    }


    /**
     * @param id
     * @return
     */
    public Optional<SupplierResponse> getSupplier(Long id) {
        Optional<SupplierResponse> result;
        Optional<Supplier> supplierOptional = supplierRepository.findById(id);
        if (supplierOptional.isPresent()) {
            SupplierResponse responseDTO = createResponseDTO(supplierOptional.get());
            result = Optional.of(responseDTO);
        } else {
            result = Optional.empty();
        }
        return result;
    }

    /**
     * @param contractRequest
     * @param id
     * @return
     */
    public Optional<ContractResponce> createContract(ContractRequest contractRequest, Long id) {
        Optional<Supplier> supplierOptional = supplierRepository.findById(id);
        if (supplierOptional.isPresent()) {
            Contract contract = new Contract();
            contract.setName(contractRequest.getName());
            contract.setExpires(contractRequest.getExpires());
            contract.setDescription(contractRequest.getDescription());
            contract.setSigned(contract.getExpires());
            contract.setSupplier(supplierOptional.get());
            Contract savedContract = contractRepository.save(contract);
            return Optional.of(createContractDTO(savedContract));
        } else {
            return Optional.empty();
        }

    }


    /**
     * @param savedContract
     * @return
     */
    private ContractResponce createContractDTO(Contract savedContract) {
        ContractResponce contractResponce = new ContractResponce();
        contractResponce.setId(savedContract.getId());
        contractResponce.setName(savedContract.getName());
        contractResponce.setExpires(savedContract.getExpires());
        contractResponce.setDescription(savedContract.getDescription());
        contractResponce.setSigned(savedContract.getExpires());
        contractResponce.setSupplierResponse(createResponseDTO(savedContract.getSupplier()));
        return contractResponce;
    }

    /**
     * @param supplierRequest
     * @return
     */
    private static Supplier createSupplierEntity(SupplierRequest supplierRequest) {
        Supplier supplier = new Supplier();
        supplier.setName(supplierRequest.getName());
        supplier.setAddress(supplierRequest.getAddress());
        supplier.setPhone(supplierRequest.getPhone());
        supplier.setEmail(supplierRequest.getEmail());
        return supplier;
    }


    /**
     * @param savedSuppier
     * @return
     */
    private SupplierResponse createResponseDTO(Supplier savedSuppier) {
        SupplierResponse response = new SupplierResponse();
        response.setId(savedSuppier.getId());
        response.setName(savedSuppier.getName());
        response.setAddress(savedSuppier.getAddress());
        response.setEmail(savedSuppier.getEmail());
        response.setPhone(savedSuppier.getPhone());
        return response;
    }


    /**
     * @param id
     * @return
     */
    public Optional<ArrayList<ContractResponce>> getAllContractsForGivenSupplier(Long id) {
        Optional<List<Contract>> bySupplierId = contractRepository.findAllBySupplier_Id(id);
        if (bySupplierId.isPresent()) {
            ArrayList<ContractResponce> contracts = new ArrayList<>();
            for (Contract contract : bySupplierId.get()) {
                ContractResponce contractDTO = createContractDTO(contract);
                contracts.add(contractDTO);
            }
            return Optional.of(contracts);
        } else {
            return Optional.empty();
        }
    }


    public Optional<Long> countAllInvoicesForAGivenSupplier(Long id) {
        return invoiceRepository.countBySupplier_Id(id);

    }
}
