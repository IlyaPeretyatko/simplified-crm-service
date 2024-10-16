package ru.peretyatko.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.peretyatko.app.models.Seller;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
}
