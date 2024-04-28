package org.dadobt.casestudy.repository;

import org.dadobt.casestudy.models.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {


    //why JPA WHY !
    Optional<List<Invoice>> findBySupplier_Id(@NonNull Long id);

    Optional<Long> countBySupplier_Id(Long id);

    Optional<List<Invoice>> findByDueDateBefore(Timestamp dueDate);

    Optional<List<Invoice>> findByDueDateBetween(Timestamp dueDateStart, Timestamp dueDateEnd);
}
