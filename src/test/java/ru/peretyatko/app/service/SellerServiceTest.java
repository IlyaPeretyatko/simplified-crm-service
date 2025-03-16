package ru.peretyatko.app.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.peretyatko.app.dto.seller.SellerPatchRequest;
import ru.peretyatko.app.dto.seller.SellerPostRequest;
import ru.peretyatko.app.dto.seller.SellerResponse;
import ru.peretyatko.app.dto.transaction.TransactionResponse;
import ru.peretyatko.app.error.exception.ServiceException;
import ru.peretyatko.app.mapper.SellerMapper;
import ru.peretyatko.app.model.PaymentType;
import ru.peretyatko.app.model.Seller;
import ru.peretyatko.app.model.Transaction;
import ru.peretyatko.app.repository.SellerRepository;
import ru.peretyatko.app.dto.RangeDate;

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
    public void add_ReturnsSeller() {
        SellerResponse seller = new SellerResponse(1L,"Ilya", "+78005553535", LocalDateTime.now());
        SellerPostRequest sellerPostRequest = new SellerPostRequest("Ilya", "+78005553535");
        when(sellerRepository.save(any())).thenReturn(seller);
        SellerResponse result = sellerService.createSeller(sellerPostRequest);
        assertEquals(seller.getId(), result.getId());
        assertEquals(seller.getName(), result.getName());
        assertEquals(seller.getContactInfo(), result.getContactInfo());
        assertEquals(seller.getRegistrationDate(), result.getRegistrationDate());
    }

    @Test
    public void findAll_ReturnsSellers() {
        List<Seller> sellers = List.of(new Seller(1L, "Ilya", "+78005553535", LocalDateTime.now(), null),
                new Seller(2L, "Igor", "igor@mail.ru", LocalDateTime.now(), null));
        when(sellerRepository.findAll()).thenReturn(sellers);
        List<SellerResponse> result = sellerService.getSellers();
        assertEquals(sellers.getFirst().getName(), result.getFirst().getName());
        assertEquals(sellers.getLast().getName(), result.getLast().getName());
        assertEquals(sellers.getFirst().getContactInfo(), result.getFirst().getContactInfo());
        assertEquals(sellers.getLast().getContactInfo(), result.getLast().getContactInfo());
        assertEquals(sellers.getFirst().getRegistrationDate(), result.getFirst().getRegistrationDate());
        assertEquals(sellers.getLast().getRegistrationDate(), result.getLast().getRegistrationDate());
    }

    @Test
    public void findById_ReturnsSeller() {
        Seller seller = new Seller(1L, "Ilya", "+78005553535", LocalDateTime.now(), null);
        seller.setId(1L);
        when(sellerRepository.findById(eq(1L))).thenReturn(Optional.of(seller));
        Seller result = sellerService.findById(1L);
        assertEquals(seller.getId(), result.getId());
        assertEquals(seller.getName(), result.getName());
        assertEquals(seller.getContactInfo(), result.getContactInfo());
        assertEquals(seller.getRegistrationDate(), result.getRegistrationDate());
    }

    @Test
    public void findById_ReturnsError() {
        when(sellerRepository.findById(eq(1L))).thenReturn(Optional.empty());
        Exception exception = assertThrows(ServiceException.class, () -> {
            sellerService.findById(1L);
        });
    }

    @Test
    public void findTransactionsBySeller_ReturnsTransactions() {
        Seller seller = new Seller(1L, "Ilya", "+78005553535", LocalDateTime.now(), null);
        seller.setId(1L);
        List<Transaction> transactions = List.of(new Transaction(1L, seller, 1000, PaymentType.CARD, LocalDateTime.now()),
                new Transaction(2L, seller, 2000, PaymentType.CARD, LocalDateTime.now()));
        seller.setTransactions(transactions);
        when(sellerRepository.findById(eq(1L))).thenReturn(Optional.of(seller));
        List<TransactionResponse> result = sellerService.getTransactionsOfSeller(seller.getId());
        assertEquals(seller.getTransactions().getFirst().getAmount(), result.getFirst().getAmount());
        assertEquals(seller.getTransactions().getFirst().getPaymentType(), result.getFirst().getPaymentType());
        assertEquals(seller.getTransactions().getFirst().getTransactionDate(), result.getFirst().getTransactionDate());
        assertEquals(seller.getTransactions().getLast().getAmount(), result.getLast().getAmount());
        assertEquals(seller.getTransactions().getLast().getPaymentType(), result.getLast().getPaymentType());
        assertEquals(seller.getTransactions().getLast().getTransactionDate(), result.getLast().getTransactionDate());
    }

    @Test
    public void findTransactionsBySeller_ReturnsError() {
        when(sellerRepository.findById(eq(1L))).thenReturn(Optional.empty());
        Exception exception = assertThrows(ServiceException.class, () -> {
            sellerService.findById(1L);
        });
    }

    @Test
    public void update_ReturnsSeller() {
        SellerPatchRequest seller = new SellerPatchRequest("Ilya", "+78005553535");
        when(sellerRepository.save(any())).thenReturn(seller);
        SellerResponse result = sellerService.updateSeller(1L, seller);
        assertEquals(seller.getName(), result.getName());
        assertEquals(seller.getContactInfo(), result.getContactInfo());
    }

    @Test
    public void update_ReturnsError() {
        when(sellerRepository.findById(eq(1L))).thenReturn(Optional.empty());
        Exception exception = assertThrows(ServiceException.class, () -> {
            sellerService.findById(1L);
        });
    }

    @Test
    public void delete_ReturnsSuccess() {
        when(sellerRepository.existsById(eq(1L))).thenReturn(true);
        sellerService.deleteSeller(1L);
    }

    @Test
    public void delete_ReturnsError() {
        when(sellerRepository.existsById(eq(1L))).thenReturn(false);
        Exception exception = assertThrows(ServiceException.class, () -> {
            sellerService.deleteSeller(1L);
        });
    }

    @Test
    public void findBestSeller_ReturnsBestSeller() {
        Seller seller = new Seller(1L,"Ilya", "+78005553535", LocalDateTime.now(), null);
        seller.setId(1L);
        RangeDate rangeDate = new RangeDate(LocalDateTime.now().minusMonths(12), LocalDateTime.now());
        Query query = mock(Query.class);
        when(entityManager.createNativeQuery(eq(SellerService.SQL_BEST_SELLER), eq(Seller.class))).thenReturn(query);
        when(query.setParameter(eq("start"), any(LocalDateTime.class))).thenReturn(query);
        when(query.setParameter(eq("end"), any(LocalDateTime.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(seller));
        SellerResponse result = sellerService.getBestSeller(rangeDate);
        assertEquals(seller.getId(), result.getId());
        assertEquals(seller.getName(), result.getName());
        assertEquals(seller.getContactInfo(), result.getContactInfo());
        assertEquals(seller.getRegistrationDate(), result.getRegistrationDate());
    }

    @Test
    public void findBestSeller_ReturnsError() {
        RangeDate rangeDate = new RangeDate(LocalDateTime.now().minusMonths(12), LocalDateTime.now());
        Query query = mock(Query.class);
        when(entityManager.createNativeQuery(eq(SellerService.SQL_BEST_SELLER), eq(Seller.class))).thenReturn(query);
        when(query.setParameter(eq("start"), any(LocalDateTime.class))).thenReturn(query);
        when(query.setParameter(eq("end"), any(LocalDateTime.class))).thenReturn(query);
        when(query.getResultList()).thenThrow(new NoResultException());
        Exception exception = assertThrows(ServiceException.class, () -> {
            sellerService.getBestSeller(rangeDate);
        });
    }

    @Test
    public void findSellersSumLessThen_ReturnsBestSeller() {
        Seller seller1 = new Seller(1L, "Ilya", "+78005553535", LocalDateTime.now(), null);
        seller1.setId(1L);
        Seller seller2 = new Seller(1L, "Igor", "+78007773535", LocalDateTime.now(), null);
        seller2.setId(1L);
        List<Seller> sellers = List.of(seller1, seller2);
        Query query = mock(Query.class);
        when(entityManager.createNativeQuery(eq(SellerService.SQL_SUM_LESS_THEN), eq(Seller.class))).thenReturn(query);
        when(query.setParameter(eq("maxSum"), eq(100))).thenReturn(query);
        when(query.getResultList()).thenReturn(sellers);
        List<SellerResponse> result = sellerService.getSellersSumLessThen(100, any(RangeDate.class));
        assertEquals(sellers.getFirst().getId(), result.getFirst().getId());
        assertEquals(sellers.getLast().getId(), result.getLast().getId());
        assertEquals(sellers.getFirst().getName(), result.getFirst().getName());
        assertEquals(sellers.getLast().getName(), result.getLast().getName());
        assertEquals(sellers.getFirst().getContactInfo(), result.getFirst().getContactInfo());
        assertEquals(sellers.getLast().getContactInfo(), result.getLast().getContactInfo());
        assertEquals(sellers.getFirst().getRegistrationDate(), result.getFirst().getRegistrationDate());
        assertEquals(sellers.getLast().getRegistrationDate(), result.getLast().getRegistrationDate());
    }




}