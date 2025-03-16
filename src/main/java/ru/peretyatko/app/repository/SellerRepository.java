package ru.peretyatko.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.peretyatko.app.model.Seller;

import java.time.LocalDateTime;

public interface SellerRepository extends JpaRepository<Seller, Long> {
}
