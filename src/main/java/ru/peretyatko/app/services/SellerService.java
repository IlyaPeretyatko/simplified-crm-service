package ru.peretyatko.app.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.peretyatko.app.models.Seller;
import ru.peretyatko.app.models.Transaction;
import ru.peretyatko.app.repositories.SellerRepository;
import ru.peretyatko.app.util.Period;
import ru.peretyatko.app.util.SellerNotFoundException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SellerService {

    private final SellerRepository sellerRepository;

    @Autowired
    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    @PersistenceContext
    private EntityManager entityManager;

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
        if (updatedSeller.getRegistrationDate() != null) {
            seller.setRegistrationDate(updatedSeller.getRegistrationDate());
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
        String sql = """
                        SELECT * \
                        FROM sellers\
                        WHERE id = ( \
                            SELECT seller_id \
                            FROM transactions \
                            WHERE transaction_date > :start AND transaction_date < :end \
                            GROUP BY seller_id \
                            ORDER BY COALESCE(SUM(amount), 0) DESC \
                            LIMIT 1 \
                        )
                """;
        Query query = entityManager.createNativeQuery(sql, Seller.class).setParameter("start", period.getStart()).setParameter("end", period.getEnd());
        try {
            return (Seller) query.getSingleResult();
        } catch (NoResultException e) {
            throw new SellerNotFoundException();
        }
    }

    public List<Seller> findSellersSumLessThen(int maxSum) {
        String sql = """
                        SELECT sellers.id, sellers.name, sellers.contact_info, sellers.registration_date \
                        FROM sellers LEFT JOIN transactions \
                        ON sellers.id = transactions.seller_id \
                        GROUP BY sellers.id \
                        HAVING COALESCE(SUM(transactions.amount), 0) < :maxSum \
                        ORDER BY SUM(amount) DESC \
                """;
        Query query = entityManager.createNativeQuery(sql, Seller.class).setParameter("maxSum", maxSum)    ;
        List<Seller> sellers = query.getResultList();
        return sellers;
    }

}
