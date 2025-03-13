package ru.peretyatko.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.peretyatko.app.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
