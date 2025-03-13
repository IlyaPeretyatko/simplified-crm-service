package ru.peretyatko.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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


    private final SellerRepository sellerRepository;

    private final SellerMapper sellerMapper;

    private final TransactionMapper transactionMapper;

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
        Seller createdSeller = sellerRepository.save(seller);
        createdSeller.setRegistrationDate(LocalDateTime.now());
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
        if (sellerRepository.existsById(id)) {
            throw new ServiceException(HttpStatus.NOT_FOUND, "Seller wasn't found.");
        }
        sellerRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsOfUser(long id) {
        Seller seller = sellerRepository.findById(id).orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Seller wasn't found."));
        return seller.getTransactions().stream().map(transactionMapper::toTransactionResponse).collect(Collectors.toList());
    }

}
