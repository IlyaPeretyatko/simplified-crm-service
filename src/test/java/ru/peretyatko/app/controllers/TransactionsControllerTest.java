package ru.peretyatko.app.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.peretyatko.app.models.Seller;
import ru.peretyatko.app.models.Transaction;
import ru.peretyatko.app.repositories.SellerRepository;
import ru.peretyatko.app.repositories.TransactionRepository;
import ru.peretyatko.app.util.SellerNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private SellerRepository sellerRepository;

    @Test
    public void getTransactions_ReturnsAllTransactions() throws Exception {
        Seller seller = new Seller("Ilya", "+78005553535", LocalDateTime.parse("2023-10-01T15:30:00"));
        List<Transaction> transactions = List.of(new Transaction(seller, 1500, "CASH", LocalDateTime.parse("2024-10-01T15:30:00")),
                new Transaction(seller, 2000, "CASH", LocalDateTime.parse("2024-10-01T15:30:00")),
                new Transaction(seller, 3000, "CASH", LocalDateTime.parse("2024-10-01T15:30:00")));
        when(transactionRepository.findAll()).thenReturn(transactions);
        mockMvc.perform(get("/api/transactions").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(1500))
                .andExpect(jsonPath("$[0].paymentType").value("CASH"))
                .andExpect(jsonPath("$[1].amount").value(2000))
                .andExpect(jsonPath("$[1].paymentType").value("CASH"))
                .andExpect(jsonPath("$[2].amount").value(3000))
                .andExpect(jsonPath("$[2].paymentType").value("CASH"));
    }

    @Test
    public void getTransactions_ReturnsEmptyJson() throws Exception {
        Seller seller = new Seller("Ilya", "+78005553535", LocalDateTime.parse("2023-10-01T15:30:00"));
        List<Transaction> transactions = new ArrayList<>();
        when(transactionRepository.findAll()).thenReturn(transactions);
        mockMvc.perform(get("/api/transactions").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void getTransaction_ReturnsTransaction() throws Exception {
        Seller seller  = new Seller("Ivan", "+79833338712", LocalDateTime.parse("2023-10-01T15:30:00"));
        Transaction transaction = new Transaction(seller, 1500, "CASH", LocalDateTime.parse("2024-10-01T15:30:00"));
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        mockMvc.perform(get("/api/transactions/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(1500))
                .andExpect(jsonPath("$.paymentType").value("CASH"));
    }

    @Test
    public void getTransaction_ReturnsError() throws Exception {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/transactions/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Transaction wasn't found."));
    }

    @Test
    public void postTransaction_ReturnsTransaction() throws Exception {
        Seller seller = new Seller("Dmitriy", "+78005553535", LocalDateTime.parse("2023-10-01T15:30:00"));
        seller.setId(1L);
        Transaction transaction = new Transaction(seller, 1500, "CASH", LocalDateTime.parse("2024-10-01T15:30:00"));
        transaction.setId(1L);
        when(sellerRepository.existsById(seller.getId())).thenReturn(true);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        mockMvc.perform(post("/api/transactions").contentType(MediaType.APPLICATION_JSON).content("""
                        {
                            "seller" : {"id" : 1},
                            "amount" : 1500,
                            "paymentType" : "CASH",
                            "transactionDate" : "2024-10-16T23:01:00"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(1500))
                .andExpect(jsonPath("$.paymentType").value("CASH"));
    }

    @Test
    public void postTransaction_ReturnsError() throws Exception {
        Seller seller = new Seller("Dmitriy", "+78005553535", LocalDateTime.parse("2023-10-01T15:30:00"));
        seller.setId(1L);
        Transaction transaction = new Transaction(seller, 1500, "CASH", LocalDateTime.parse("2024-10-01T15:30:00"));
        transaction.setId(1L);
        when(sellerRepository.existsById(seller.getId())).thenReturn(false);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        mockMvc.perform(post("/api/transactions").contentType(MediaType.APPLICATION_JSON).content("""
                        {
                            "seller" : {"id" : 1},
                            "amount" : 1500,
                            "paymentType" : "CASH",
                            "transactionDate" : "2024-10-16T23:01:00"
                        }
                        """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Seller wasn't found."));
    }

    @Test
    public void deleteTransaction_ReturnsSuccess() throws Exception {
        when(transactionRepository.existsById(1L)).thenReturn(true);
        mockMvc.perform(delete("/api/transactions/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteTransaction_ReturnsError() throws Exception {
        when(transactionRepository.existsById(1L)).thenReturn(false);
        mockMvc.perform(delete("/api/transactions/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Transaction wasn't found."));
    }

    @Test
    public void updateTransaction_ReturnsTransaction() throws Exception {
        Seller seller = new Seller("Dmitriy", "+78005553535", LocalDateTime.parse("2023-10-01T15:30:00"));
        seller.setId(1L);
        Transaction transaction = new Transaction(seller, 1500, "CASH", LocalDateTime.parse("2024-10-01T15:30:00"));
        transaction.setId(1L);
        Transaction updatedTransaction = new Transaction(seller, 2000, "CARD", LocalDateTime.parse("2024-10-01T15:30:00"));
        updatedTransaction.setId(1L);
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(updatedTransaction);
        mockMvc.perform(patch("/api/transactions/1").contentType(MediaType.APPLICATION_JSON).content("{\"amount\" : 1500, \"paymentType\" : \"CARD\", \"transactionDate\" : \"2024-10-01T15:30:00\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void updateTransaction_ReturnsError() throws Exception {
        when(sellerRepository.findById(1L)).thenThrow(SellerNotFoundException.class);
        mockMvc.perform(patch("/api/transactions/1").contentType(MediaType.APPLICATION_JSON).content("{\"amount\" : 1500}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Transaction wasn't found."));
    }

}