package ru.peretyatko.app.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.peretyatko.app.model.Seller;
import ru.peretyatko.app.model.Transaction;
import ru.peretyatko.app.service.TransactionService;
import ru.peretyatko.app.util.SellerNotFoundException;
import ru.peretyatko.app.util.TransactionNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Test
    public void getTransactions_ReturnsAllTransactions() throws Exception {
        Seller seller = new Seller("Ilya", "+78005553535", LocalDateTime.parse("2023-10-01T15:30:00"));
        List<Transaction> transactions = List.of(new Transaction(seller, 1500, "CASH", LocalDateTime.parse("2024-10-01T15:30:00")),
                new Transaction(seller, 2000, "CASH", LocalDateTime.parse("2024-10-01T15:30:00")),
                new Transaction(seller, 3000, "CASH", LocalDateTime.parse("2024-10-01T15:30:00")));
        when(transactionService.findAll()).thenReturn(transactions);
        mockMvc.perform(get("/api/transactions").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].amount").value(1500))
                .andExpect(jsonPath("$[0].paymentType").value("CASH"))
                .andExpect(jsonPath("$[1].amount").value(2000))
                .andExpect(jsonPath("$[1].paymentType").value("CASH"))
                .andExpect(jsonPath("$[2].amount").value(3000))
                .andExpect(jsonPath("$[2].paymentType").value("CASH"));
    }

    @Test
    public void getTransactions_ReturnsEmptyJson() throws Exception {
        when(transactionService.findAll()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/api/transactions").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void getTransaction_ReturnsTransaction() throws Exception {
        Seller seller  = new Seller("Ivan", "+79833338712", LocalDateTime.parse("2023-10-01T15:30:00"));
        Transaction transaction = new Transaction(seller, 1500, "CASH", LocalDateTime.parse("2024-10-01T15:30:00"));
        when(transactionService.findById(eq(1L))).thenReturn(transaction);
        mockMvc.perform(get("/api/transactions/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(1500))
                .andExpect(jsonPath("$.paymentType").value("CASH"));
    }

    @Test
    public void getTransaction_ReturnsError() throws Exception {
        when(transactionService.findById(eq(1L))).thenThrow(new TransactionNotFoundException());
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
        when(transactionService.add(any(Transaction.class))).thenReturn(transaction);
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
        when(transactionService.add(any(Transaction.class))).thenThrow(new SellerNotFoundException());
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
        doNothing().when(transactionService).delete(eq(1L));
        mockMvc.perform(delete("/api/transactions/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteTransaction_ReturnsError() throws Exception {
        doThrow(new TransactionNotFoundException()).when(transactionService).delete(eq(1L));
        mockMvc.perform(delete("/api/transactions/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Transaction wasn't found."));
    }

    @Test
    public void patchTransaction_ReturnsTransaction() throws Exception {
        Seller seller = new Seller("Dmitriy", "+78005553535", LocalDateTime.parse("2023-10-01T15:30:00"));
        seller.setId(1L);
        Transaction updatedTransaction = new Transaction(seller, 1500, "CARD", LocalDateTime.parse("2024-10-01T15:30:00"));
        updatedTransaction.setId(1L);
        when(transactionService.update(eq(1L), any(Transaction.class))).thenReturn(updatedTransaction);
        mockMvc.perform(patch("/api/transactions/1").contentType(MediaType.APPLICATION_JSON).content("{\"amount\" : 1500, \"paymentType\" : \"CARD\", \"transactionDate\" : \"2024-10-01T15:30:00\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(1500))
                .andExpect(jsonPath("$.paymentType").value("CARD"));
    }

    @Test
    public void patchTransaction_ReturnsError() throws Exception {
        when(transactionService.update(eq(1L), any(Transaction.class))).thenThrow(TransactionNotFoundException.class);
        mockMvc.perform(patch("/api/transactions/1").contentType(MediaType.APPLICATION_JSON).content("{\"amount\" : 1500}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Transaction wasn't found."));
    }

}