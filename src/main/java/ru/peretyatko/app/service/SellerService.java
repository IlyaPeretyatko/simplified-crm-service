package ru.peretyatko.app.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.peretyatko.app.dto.RangeDate;
import ru.peretyatko.app.dto.seller.SellerPatchRequest;
import ru.peretyatko.app.dto.seller.SellerPostRequest;
import ru.peretyatko.app.dto.seller.SellerResponse;
import ru.peretyatko.app.dto.transaction.TransactionResponse;
import ru.peretyatko.app.error.exception.ServiceException;
import ru.peretyatko.app.mapper.SellerMapper;
import ru.peretyatko.app.mapper.TransactionMapper;
import ru.peretyatko.app.model.Seller;
import ru.peretyatko.app.repository.SellerRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellerService {

    public final static String SQL_BEST_SELLER =
            """
            SELECT * 
            FROM sellers
            WHERE id = (
                SELECT seller_id
                FROM transactions
                WHERE transaction_date > :start AND transaction_date < :end
                GROUP BY seller_id
                ORDER BY COALESCE(SUM(amount), 0) DESC
                LIMIT 1
            )
            """;

    public final static String SQL_SUM_LESS_THEN =
            """
            SELECT sellers.id, sellers.name, sellers.contact_info, sellers.registration_date 
            FROM sellers LEFT JOIN transactions 
            ON sellers.id = transactions.seller_id 
            WHERE transactions.transaction_date BETWEEN :start AND :end 
            GROUP BY sellers.id 
            HAVING COALESCE(SUM(transactions.amount), 0) < :maxSum 
            ORDER BY SUM(amount) DESC 
            """;


    private final SellerRepository sellerRepository;

    private final SellerMapper sellerMapper;

    private final TransactionMapper transactionMapper;

    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public SellerResponse getSeller(long id) {
        Seller seller = sellerRepository.findById(id).orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Seller wasn't found."));
        return sellerMapper.toSellerResponse(seller);
    }

    @Transactional(readOnly = true)
    public List<SellerResponse> getSellers() {
        return sellerRepository.findAll().stream().map(sellerMapper::toSellerResponse).collect(Collectors.toList());
    }

    @Transactional
    public SellerResponse createSeller(SellerPostRequest sellerPostRequest) {
        Seller seller = sellerMapper.toSeller(sellerPostRequest);
        seller.setRegistrationDate(LocalDateTime.now());
        Seller createdSeller = sellerRepository.save(seller);
        return sellerMapper.toSellerResponse(createdSeller);
    }

    @Transactional
    public SellerResponse updateSeller(long id, SellerPatchRequest sellerPatchRequest) {
        Seller seller = sellerRepository.findById(id).orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Seller wasn't found."));
        sellerMapper.updateSeller(sellerPatchRequest, seller);
        return sellerMapper.toSellerResponse(sellerRepository.save(seller));
    }

    @Transactional
    public void deleteSeller(long id) {
        if (!sellerRepository.existsById(id)) {
            throw new ServiceException(HttpStatus.NOT_FOUND, "Seller wasn't found.");
        }
        sellerRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsOfSeller(long id) {
        Seller seller = sellerRepository.findById(id).orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Seller wasn't found."));
        return seller.getTransactions().stream().map(transactionMapper::toTransactionResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SellerResponse getBestSeller(RangeDate rangeDate) {
        Query query = entityManager.createNativeQuery(SQL_BEST_SELLER, Seller.class ).setParameter("start", rangeDate.getStart()).setParameter("end", rangeDate.getEnd());
        List list = query.getResultList();
        if (list.isEmpty()) {
            throw new ServiceException(HttpStatus.NOT_FOUND, "Seller wasn't found.");
        }
        return sellerMapper.toSellerResponse((Seller) list.getFirst());
    }

    @Transactional(readOnly = true)
    public List<SellerResponse> getSellersSumLessThen(int sum, RangeDate rangeDate) {
        Query query = entityManager.createNativeQuery(SQL_SUM_LESS_THEN, Seller.class).setParameter("maxSum", sum).setParameter("start", rangeDate.getStart()).setParameter("end", rangeDate.getEnd());
        return (List<SellerResponse>) query.getResultList().stream()
                .map(seller -> sellerMapper.toSellerResponse((Seller) seller))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    protected Seller findById(long id) {
        return sellerRepository.findById(id).orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Seller wasn't found."));
    }


}
