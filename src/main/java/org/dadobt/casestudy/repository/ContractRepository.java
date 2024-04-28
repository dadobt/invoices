package org.dadobt.casestudy.repository;

import org.dadobt.casestudy.models.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    Optional<List<Contract>> findAllBySupplier_Id(Long id);
}
