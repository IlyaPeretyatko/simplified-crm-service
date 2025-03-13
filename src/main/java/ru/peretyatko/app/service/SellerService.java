package ru.peretyatko.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.peretyatko.app.dto.seller.SellerRequest;
import ru.peretyatko.app.dto.seller.SellerResponse;
import ru.peretyatko.app.dto.transaction.TransactionResponse;
import ru.peretyatko.app.mapper.SellerMapper;
import ru.peretyatko.app.repository.SellerRepository;

import java.util.List;

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

    @Transactional(readOnly = true)
    public SellerResponse getSeller(long id) {
        return null;
    }

    @Transactional(readOnly = true)
    public List<SellerResponse> getSellers() { return null; }

    @Transactional
    public SellerResponse createSeller(SellerRequest sellerRequest) { return null; }

    @Transactional
    public SellerResponse updateSeller(long id, SellerRequest sellerRequest) { return null; }

    @Transactional
    public void deleteSeller(long id) {}

    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsOfUser(long id) { return null; }

}
