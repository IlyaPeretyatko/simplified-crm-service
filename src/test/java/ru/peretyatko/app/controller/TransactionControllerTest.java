package ru.peretyatko.app.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.peretyatko.app.dto.transaction.TransactionPatchRequest;
import ru.peretyatko.app.dto.transaction.TransactionPostRequest;
import ru.peretyatko.app.dto.transaction.TransactionResponse;
import ru.peretyatko.app.error.exception.ServiceException;
import ru.peretyatko.app.model.PaymentType;
import ru.peretyatko.app.model.Seller;
import ru.peretyatko.app.service.TransactionService;

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

    @Mock
    private TransactionService transactionService;

    @Test
    public void getTransactions_ReturnsAllTransactions() throws Exception {
        Seller seller = new Seller(1L, "Ilya", "+78005553535", LocalDateTime.parse("2023-10-01T15:30:00"), null);
        List<TransactionResponse> transactions = List.of(new TransactionResponse(1L, 1L, 1500, PaymentType.CASH, LocalDateTime.parse("2024-10-01T15:30:00")),
                new TransactionResponse(2L, 1L, 2000, PaymentType.CASH, LocalDateTime.parse("2024-10-01T15:30:00")),
                new TransactionResponse(3l, 1L, 3000, PaymentType.CASH, LocalDateTime.parse("2024-10-01T15:30:00")));
        when(transactionService.getTransactions()).thenReturn(transactions);
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
        when(transactionService.getTransactions()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/api/transactions").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void getTransaction_ReturnsTransaction() throws Exception {
        Seller seller  = new Seller(1L, "Ivan", "+79833338712", LocalDateTime.parse("2023-10-01T15:30:00"), null);
        TransactionResponse transaction = new TransactionResponse(1L, 1L, 1500, PaymentType.CASH, LocalDateTime.parse("2024-10-01T15:30:00"));
        when(transactionService.getTransaction(eq(1L))).thenReturn(transaction);
        mockMvc.perform(get("/api/transactions/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(1500))
                .andExpect(jsonPath("$.paymentType").value("CASH"));
    }

    @Test
    public void getTransaction_ReturnsError() throws Exception {
        when(transactionService.getTransaction(eq(1L))).thenThrow(new ServiceException(HttpStatus.NOT_FOUND,"Transaction wasn't found."));
        mockMvc.perform(get("/api/transactions/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Transaction wasn't found."));
    }

    @Test
    public void postTransaction_ReturnsTransaction() throws Exception {
        Seller seller = new Seller(1L,"Dmitriy", "+78005553535", LocalDateTime.parse("2023-10-01T15:30:00"), null);
        seller.setId(1L);
        TransactionResponse transaction = new TransactionResponse(1L, 1L, 1500, PaymentType.CASH, LocalDateTime.parse("2024-10-01T15:30:00"));
        transaction.setId(1L);
        when(transactionService.createTransaction(any(TransactionPostRequest.class))).thenReturn(transaction);
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
        when(transactionService.createTransaction(any(TransactionPostRequest.class))).thenThrow(new ServiceException(HttpStatus.NOT_FOUND,"Seller wasn't found."));
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
    public void deleteTransactionTransaction_ReturnsSuccess() throws Exception {
        doNothing().when(transactionService).deleteTransaction(eq(1L));
        mockMvc.perform(delete("/api/transactions/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteTransactionTransaction_ReturnsError() throws Exception {
        doThrow(new ServiceException(HttpStatus.NOT_FOUND,"Transaction wasn't found.")).when(transactionService).deleteTransaction(eq(1L));
        mockMvc.perform(delete("/api/transactions/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Transaction wasn't found."));
    }

    @Test
    public void patchTransaction_ReturnsTransaction() throws Exception {
        Seller seller = new Seller(1L, "Dmitriy", "+78005553535", LocalDateTime.parse("2023-10-01T15:30:00"), null);
        seller.setId(1L);
        TransactionResponse updateTransaction = new TransactionResponse(1l, 1L, 1500, PaymentType.CARD, LocalDateTime.parse("2024-10-01T15:30:00"));
        updateTransaction.setId(1L);
        when(transactionService.updateTransaction(eq(1L), any(TransactionPatchRequest.class))).thenReturn(updateTransaction);
        mockMvc.perform(patch("/api/transactions/1").contentType(MediaType.APPLICATION_JSON).content("{\"amount\" : 1500, \"paymentType\" : \"CARD\", \"transactionDate\" : \"2024-10-01T15:30:00\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(1500))
                .andExpect(jsonPath("$.paymentType").value("CARD"));
    }

    @Test
    public void patchTransaction_ReturnsError() throws Exception {
        when(transactionService.updateTransaction(eq(1L), any(TransactionPatchRequest.class))).thenThrow(new ServiceException(HttpStatus.NOT_FOUND,"Transaction wasn't found."));
        mockMvc.perform(patch("/api/transactions/1").contentType(MediaType.APPLICATION_JSON).content("{\"amount\" : 1500}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Transaction wasn't found."));
    }

}