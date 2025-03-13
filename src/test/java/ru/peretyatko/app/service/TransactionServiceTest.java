package ru.peretyatko.app.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.peretyatko.app.model.Seller;
import ru.peretyatko.app.model.Transaction;
import ru.peretyatko.app.repository.SellerRepository;
import ru.peretyatko.app.repository.TransactionRepository;
import ru.peretyatko.app.util.SellerNotFoundException;
import ru.peretyatko.app.util.TransactionNotFoundException;

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
        Seller seller = new Seller("Ilya", "+78005553535", LocalDateTime.now().minusDays(10));
        Transaction transaction = new Transaction(seller, 10000, "CASH", LocalDateTime.now());
        when(sellerRepository.existsById(seller.getId())).thenReturn(true);
        when(transactionRepository.save(any())).thenReturn(transaction);
        Transaction result = transactionService.add(transaction);
        assertEquals(transaction.getId(), result.getId());
        assertEquals(transaction.getAmount(), result.getAmount());
        assertEquals(transaction.getPaymentType(), result.getPaymentType());
        assertEquals(transaction.getTransactionDate(), result.getTransactionDate());
    }

    @Test
    public void add_ReturnsError() {
        Seller seller = new Seller("Ilya", "+78005553535", LocalDateTime.now().minusDays(10));
        Transaction transaction = new Transaction(seller, 10000, "CASH", LocalDateTime.now());
        when(sellerRepository.existsById(seller.getId())).thenReturn(false);
        Exception exception = assertThrows(SellerNotFoundException.class, () -> {
            transactionService.add(transaction);
        });
    }

    @Test
    public void findAll_ReturnsAllTransactions() {
        Seller seller = new Seller("Ilya", "+78005553535", LocalDateTime.now().minusDays(10));
        Transaction transaction1 = new Transaction(seller, 10000, "CASH", LocalDateTime.now());
        Transaction transaction2 = new Transaction(seller, 20000, "CASH", LocalDateTime.now());
        Transaction transaction3 = new Transaction(seller, 30000, "CASH", LocalDateTime.now());
        List<Transaction> transactions = List.of(transaction1, transaction2, transaction3);
        when(transactionRepository.findAll()).thenReturn(transactions);
        List<Transaction> result = transactionService.findAll();
        for (int i = 0; i < 3; ++i) {
            assertEquals(transactions.get(i).getId(), result.get(i).getId());
            assertEquals(transactions.get(i).getAmount(), result.get(i).getAmount());
            assertEquals(transactions.get(i).getPaymentType(), result.get(i).getPaymentType());
            assertEquals(transactions.get(i).getTransactionDate(), result.get(i).getTransactionDate());
        }
    }

    @Test
    public void findById_ReturnsTransaction() {
        Seller seller = new Seller("Ilya", "+78005553535", LocalDateTime.now().minusDays(10));
        Transaction transaction = new Transaction(seller, 10000, "CASH", LocalDateTime.now());
        when(transactionRepository.findById(eq(1L))).thenReturn(Optional.of(transaction));
        Transaction result = transactionService.findById(1L);
        assertEquals(transaction.getId(), result.getId());
        assertEquals(transaction.getAmount(), result.getAmount());
        assertEquals(transaction.getPaymentType(), result.getPaymentType());
        assertEquals(transaction.getTransactionDate(), result.getTransactionDate());
    }

    @Test
    public void findById_ReturnsError() {
        when(transactionRepository.findById(eq(1L))).thenReturn(Optional.empty());
        Exception exception = assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.findById(1L);
        });
    }

    @Test
    public void update_ReturnsTransaction() {
        Seller seller = new Seller("Ilya", "+78005553535", LocalDateTime.now().minusDays(10));
        Transaction transaction = new Transaction(seller, 10000, "CASH", LocalDateTime.now());
        when(transactionRepository.findById(eq(1L))).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any())).thenReturn(transaction);
        Transaction result = transactionService.update(1L, transaction);
        assertEquals(transaction.getId(), result.getId());
        assertEquals(transaction.getAmount(), result.getAmount());
        assertEquals(transaction.getPaymentType(), result.getPaymentType());
        assertEquals(transaction.getTransactionDate(), result.getTransactionDate());
    }

    @Test
    public void update_ReturnsError() {
        Seller seller = new Seller("Ilya", "+78005553535", LocalDateTime.now().minusDays(10));
        Transaction transaction = new Transaction(seller, 10000, "CASH", LocalDateTime.now());
        when(transactionRepository.findById(eq(1L))).thenReturn(Optional.empty());
        Exception exception = assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.update(1L, transaction);
        });
    }

    @Test
    public void delete_ReturnsSuccess() {
        when(transactionRepository.existsById(eq(1L))).thenReturn(true);
        transactionService.delete(1L);
    }

    @Test
    public void delete_ReturnsError() {
        when(transactionRepository.existsById(eq(1L))).thenReturn(false);
        Exception exception = assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.delete(1L);
        });
    }

}