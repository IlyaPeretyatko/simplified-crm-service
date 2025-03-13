package ru.peretyatko.app.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.peretyatko.app.model.Seller;
import ru.peretyatko.app.model.Transaction;
import ru.peretyatko.app.repository.SellerRepository;
import ru.peretyatko.app.util.Period;
import ru.peretyatko.app.util.SellerNotFoundException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SellerService {

    public final static String SQL_BEST_SELLER =
            """
            SELECT * \
            FROM sellers \
            WHERE id = ( \
                SELECT seller_id \
                FROM transactions \
                WHERE transaction_date > :start AND transaction_date < :end \
                GROUP BY seller_id \
                ORDER BY COALESCE(SUM(amount), 0) DESC \
                LIMIT 1 \
            )
            """;

    public final static String SQL_SUM_LESS_THEN =
            """
            SELECT sellers.id, sellers.name, sellers.contact_info, sellers.registration_date \
            FROM sellers LEFT JOIN transactions \
            ON sellers.id = transactions.seller_id \
            GROUP BY sellers.id \
            HAVING COALESCE(SUM(transactions.amount), 0) < :maxSum \
            ORDER BY SUM(amount) DESC \
            """;


    @PersistenceContext
    private final EntityManager entityManager;

    private final SellerRepository sellerRepository;

    @Autowired
    public SellerService(EntityManager entityManager, SellerRepository sellerRepository) {
        this.entityManager = entityManager;
        this.sellerRepository = sellerRepository;
    }

    @Transactional
    public Seller add(Seller seller) {
        return sellerRepository.save(seller);
    }

    public List<Seller> findAll() {
        return sellerRepository.findAll();
    }

    public Seller findById(long id) {
        return sellerRepository.findById(id).orElseThrow(SellerNotFoundException::new);
    }

    public List<Transaction> findTransactionsBySeller(long id) {
        return sellerRepository.findById(id).orElseThrow(SellerNotFoundException::new).getTransactions();
    }

    @Transactional
    public Seller update(long id, Seller updatedSeller) {
        Seller seller = findById(id);
        if (updatedSeller.getName() != null) {
            seller.setName(updatedSeller.getName());
        }
        if (updatedSeller.getContactInfo() != null) {
            seller.setContactInfo(updatedSeller.getContactInfo());
        }
        return sellerRepository.save(seller);
    }

    @Transactional
    public void delete(long id) {
        if (sellerRepository.existsById(id)) {
            sellerRepository.deleteById(id);
        } else {
            throw new SellerNotFoundException();
        }
    }

    public Seller findBestSeller(Period period) {
        Query query = entityManager.createNativeQuery(SQL_BEST_SELLER, Seller.class ).setParameter("start", period.getStart()).setParameter("end", period.getEnd());
        try {
            return (Seller) query.getResultList().getFirst();
        } catch (NoResultException e) {
            throw new SellerNotFoundException();
        }
    }

    public List<Seller> findSellersSumLessThen(int maxSum) {
        Query query = entityManager.createNativeQuery(SQL_SUM_LESS_THEN, Seller.class).setParameter("maxSum", maxSum);
        return query.getResultList();
    }

}
