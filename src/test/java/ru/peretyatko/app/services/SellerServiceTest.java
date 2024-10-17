package ru.peretyatko.app.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.peretyatko.app.models.Seller;
import ru.peretyatko.app.models.Transaction;
import ru.peretyatko.app.repositories.SellerRepository;
import ru.peretyatko.app.util.SellerNotFoundException;
import ru.peretyatko.app.util.Period;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SellerServiceTest {

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private SellerService sellerService;

    @Test
    public void add_ReturnsSeller() throws Exception {
        Seller seller = new Seller("Ilya", "+78005553535", LocalDateTime.now());
        when(sellerRepository.save(any())).thenReturn(seller);
        Seller result = sellerService.add(seller);
        assertEquals(seller.getId(), result.getId());
        assertEquals(seller.getName(), result.getName());
        assertEquals(seller.getContactInfo(), result.getContactInfo());
        assertEquals(seller.getRegistrationDate(), result.getRegistrationDate());
    }

    @Test
    public void findAll_ReturnsSellers() throws Exception {
        List<Seller> sellers = List.of(new Seller("Ilya", "+78005553535", LocalDateTime.now()),
                new Seller("Igor", "igor@mail.ru", LocalDateTime.now()));
        when(sellerRepository.findAll()).thenReturn(sellers);
        List<Seller> result = sellerService.findAll();
        assertEquals(sellers.getFirst().getName(), result.getFirst().getName());
        assertEquals(sellers.getLast().getName(), result.getLast().getName());
        assertEquals(sellers.getFirst().getContactInfo(), result.getFirst().getContactInfo());
        assertEquals(sellers.getLast().getContactInfo(), result.getLast().getContactInfo());
        assertEquals(sellers.getFirst().getRegistrationDate(), result.getFirst().getRegistrationDate());
        assertEquals(sellers.getLast().getRegistrationDate(), result.getLast().getRegistrationDate());
    }

    @Test
    public void findById_ReturnsSeller() throws Exception {
        Seller seller = new Seller("Ilya", "+78005553535", LocalDateTime.now());
        seller.setId(1L);
        when(sellerRepository.findById(eq(1L))).thenReturn(Optional.of(seller));
        Seller result = sellerService.findById(1L);
        assertEquals(seller.getId(), result.getId());
        assertEquals(seller.getName(), result.getName());
        assertEquals(seller.getContactInfo(), result.getContactInfo());
        assertEquals(seller.getRegistrationDate(), result.getRegistrationDate());
    }

    @Test
    public void findById_ReturnsError() throws Exception {
        when(sellerRepository.findById(eq(1L))).thenReturn(Optional.empty());
        Exception exception = assertThrows(SellerNotFoundException.class, () -> {
            sellerService.findById(1L);
        });
    }

    @Test
    public void findTransactionsBySeller_ReturnsTransactions() throws Exception {
        Seller seller = new Seller("Ilya", "+78005553535", LocalDateTime.now());
        seller.setId(1L);
        List<Transaction> transactions = List.of(new Transaction(seller, 1000, "CARD", LocalDateTime.now()),
                new Transaction(seller, 2000, "CARD", LocalDateTime.now()));
        seller.setTransactions(transactions);
        when(sellerRepository.findById(eq(1L))).thenReturn(Optional.of(seller));
        List<Transaction> result = sellerService.findTransactionsBySeller(seller.getId());
        assertEquals(seller.getId(), result.getFirst().getSeller().getId());
        assertEquals(seller.getTransactions().getFirst().getAmount(), result.getFirst().getAmount());
        assertEquals(seller.getTransactions().getFirst().getPaymentType(), result.getFirst().getPaymentType());
        assertEquals(seller.getTransactions().getFirst().getTransactionDate(), result.getFirst().getTransactionDate());
        assertEquals(seller.getId(), result.getLast().getSeller().getId());
        assertEquals(seller.getTransactions().getLast().getAmount(), result.getLast().getAmount());
        assertEquals(seller.getTransactions().getLast().getPaymentType(), result.getLast().getPaymentType());
        assertEquals(seller.getTransactions().getLast().getTransactionDate(), result.getLast().getTransactionDate());
    }

    @Test
    public void findTransactionsBySeller_ReturnsError() throws Exception {
        when(sellerRepository.findById(eq(1L))).thenReturn(Optional.empty());
        Exception exception = assertThrows(SellerNotFoundException.class, () -> {
            sellerService.findById(1L);
        });
    }

    @Test
    public void update_ReturnsSeller() throws Exception {
        Seller seller = new Seller("Ilya", "+78005553535", LocalDateTime.now());
        seller.setId(1L);
        when(sellerRepository.findById(eq(1L))).thenReturn(Optional.of(seller));
        when(sellerRepository.save(any())).thenReturn(seller);
        Seller result = sellerService.update(1L, seller);
        assertEquals(seller.getId(), result.getId());
        assertEquals(seller.getName(), result.getName());
        assertEquals(seller.getContactInfo(), result.getContactInfo());
        assertEquals(seller.getRegistrationDate(), result.getRegistrationDate());
    }

    @Test
    public void update_ReturnsError() throws Exception {
        when(sellerRepository.findById(eq(1L))).thenReturn(Optional.empty());
        Exception exception = assertThrows(SellerNotFoundException.class, () -> {
            sellerService.findById(1L);
        });
    }

    @Test
    public void delete_ReturnsSuccess() throws Exception {
        when(sellerRepository.existsById(eq(1L))).thenReturn(true);
        sellerService.delete(1L);
    }

    @Test
    public void delete_ReturnsError() throws Exception {
        when(sellerRepository.existsById(eq(1L))).thenReturn(false);
        Exception exception = assertThrows(SellerNotFoundException.class, () -> {
            sellerService.delete(1L);
        });
    }

//    @Test
//    public void findBestSeller_ReturnsBestSeller() throws Exception {
//        Seller seller = new Seller("Ilya", "+78005553535", LocalDateTime.now());
//        seller.setId(1L);
//        Period period = new Period(LocalDateTime.now().minusMonths(12), LocalDateTime.now());
//        String sql = """
//                        SELECT * \
//                        FROM sellers\
//                        WHERE id = ( \
//                            SELECT seller_id \
//                            FROM transactions \
//                            WHERE transaction_date > :start AND transaction_date < :end \
//                            GROUP BY seller_id \
//                            ORDER BY COALESCE(SUM(amount), 0) DESC \
//                            LIMIT 1 \
//                        )
//                """;
//        Query query = mock(Query.class);
//        when(entityManager.createNativeQuery(eq(sql), eq(Seller.class))).thenReturn(query);
//        when(query.setParameter(eq("start"), any(LocalDateTime.class))).thenReturn(query);
//        when(query.setParameter(eq("end"), any(LocalDateTime.class))).thenReturn(query);
//        when(query.getResultList()).thenReturn(List.of(seller));
//        Seller result = sellerService.findBestSeller(period);
//        assertEquals(seller.getId(), result.getId());
//        assertEquals(seller.getName(), result.getName());
//        assertEquals(seller.getContactInfo(), result.getContactInfo());
//        assertEquals(seller.getRegistrationDate(), result.getRegistrationDate());
//    }




}