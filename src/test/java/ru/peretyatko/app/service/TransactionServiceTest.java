package ru.peretyatko.app.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.peretyatko.app.dto.transaction.TransactionPatchRequest;
import ru.peretyatko.app.dto.transaction.TransactionPostRequest;
import ru.peretyatko.app.dto.transaction.TransactionResponse;
import ru.peretyatko.app.error.exception.ServiceException;
import ru.peretyatko.app.model.PaymentType;
import ru.peretyatko.app.model.Seller;
import ru.peretyatko.app.model.Transaction;
import ru.peretyatko.app.repository.SellerRepository;
import ru.peretyatko.app.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private SellerRepository sellerRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    public void add_ReturnsTransaction() {
        Seller seller = new Seller(1L, "Ilya", "+78005553535", LocalDateTime.now().minusDays(10), null);
        Transaction transaction = new Transaction(1L, seller, 10000, PaymentType.CASH, LocalDateTime.now());
        when(sellerRepository.existsById(seller.getId())).thenReturn(true);
        when(transactionRepository.save(any())).thenReturn(transaction);
        TransactionResponse result = transactionService.createTransaction(any(TransactionPostRequest.class));
        assertEquals(transaction.getId(), result.getId());
        assertEquals(transaction.getAmount(), result.getAmount());
        assertEquals(transaction.getPaymentType(), result.getPaymentType());
        assertEquals(transaction.getTransactionDate(), result.getTransactionDate());
    }

    @Test
    public void add_ReturnsError() {
        Seller seller = new Seller(1L, "Ilya", "+78005553535", LocalDateTime.now(), null);
        TransactionPostRequest transaction = new TransactionPostRequest(1L, 10000, PaymentType.CASH);
        when(sellerRepository.existsById(seller.getId())).thenReturn(false);
        Exception exception = assertThrows(ServiceException.class, () -> {
            transactionService.createTransaction(transaction);
        });
    }

    @Test
    public void findAll_ReturnsAllTransactions() {
        Seller seller = new Seller(1L, "Ilya", "+78005553535", LocalDateTime.now().minusDays(10), null);
        Transaction transaction1 = new Transaction(1L, seller, 10000, PaymentType.CASH, LocalDateTime.now());
        Transaction transaction2 = new Transaction(2L, seller, 20000, PaymentType.CASH, LocalDateTime.now());
        Transaction transaction3 = new Transaction(3L, seller, 30000, PaymentType.CASH, LocalDateTime.now());
        List<Transaction> transactions = List.of(transaction1, transaction2, transaction3);
        when(transactionRepository.findAll()).thenReturn(transactions);
        List<TransactionResponse> result = transactionService.getTransactions();
        for (int i = 0; i < 3; ++i) {
            assertEquals(transactions.get(i).getId(), result.get(i).getId());
            assertEquals(transactions.get(i).getAmount(), result.get(i).getAmount());
            assertEquals(transactions.get(i).getPaymentType(), result.get(i).getPaymentType());
            assertEquals(transactions.get(i).getTransactionDate(), result.get(i).getTransactionDate());
        }
    }

    @Test
    public void findById_ReturnsTransaction() {
        Seller seller = new Seller(1L, "Ilya", "+78005553535", LocalDateTime.now().minusDays(10), null);
        Transaction transaction = new Transaction(1L, seller, 10000, PaymentType.CASH, LocalDateTime.now());
        when(transactionRepository.findById(eq(1L))).thenReturn(Optional.of(transaction));
        TransactionResponse result = transactionService.getTransaction(1L);
        assertEquals(transaction.getId(), result.getId());
        assertEquals(transaction.getAmount(), result.getAmount());
        assertEquals(transaction.getPaymentType(), result.getPaymentType());
        assertEquals(transaction.getTransactionDate(), result.getTransactionDate());
    }

    @Test
    public void findById_ReturnsError() {
        when(transactionRepository.findById(eq(1L))).thenReturn(Optional.empty());
        Exception exception = assertThrows(ServiceException.class, () -> {
            transactionService.getTransaction(1L);
        });
    }

    @Test
    public void update_ReturnsTransaction() {
        Seller seller = new Seller(1L, "Ilya", "+78005553535", LocalDateTime.now().minusDays(10), null);
        Transaction transaction = new Transaction(1L, seller, 10000, PaymentType.CASH, LocalDateTime.now());
        TransactionPatchRequest transactionPatchRequest = new TransactionPatchRequest(10000, PaymentType.CASH);
        when(transactionRepository.findById(eq(1L))).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any())).thenReturn(transaction);
        TransactionResponse result = transactionService.updateTransaction(1L, transactionPatchRequest);
        assertEquals(transactionPatchRequest.getAmount(), result.getAmount());
        assertEquals(transactionPatchRequest.getPaymentType(), result.getPaymentType());
    }

    @Test
    public void update_ReturnsError() {
        TransactionPatchRequest transaction = new TransactionPatchRequest(10000, PaymentType.CASH);
        when(transactionRepository.findById(eq(1L))).thenReturn(Optional.empty());
        Exception exception = assertThrows(ServiceException.class, () -> {
            transactionService.updateTransaction(1L, transaction);
        });
    }

    @Test
    public void delete_ReturnsSuccess() {
        when(transactionRepository.existsById(eq(1L))).thenReturn(true);
        transactionService.deleteTransaction(1L);
    }

    @Test
    public void delete_ReturnsError() {
        when(transactionRepository.existsById(eq(1L))).thenReturn(false);
        Exception exception = assertThrows(ServiceException.class, () -> {
            transactionService.deleteTransaction(1L);
        });
    }

}