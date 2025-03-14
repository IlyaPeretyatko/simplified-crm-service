package ru.peretyatko.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import ru.peretyatko.app.dto.seller.SellerPatchRequest;
import ru.peretyatko.app.dto.seller.SellerPostRequest;
import ru.peretyatko.app.dto.seller.SellerResponse;
import ru.peretyatko.app.dto.transaction.TransactionResponse;
import ru.peretyatko.app.error.exception.ServiceException;
import ru.peretyatko.app.model.PaymentType;
import ru.peretyatko.app.model.Seller;
import ru.peretyatko.app.model.Transaction;
import ru.peretyatko.app.service.SellerService;
import ru.peretyatko.app.dto.RangeDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SellerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private SellerService sellerService;

    @Test
    public void getSellers_ReturnsAllSellers() throws Exception {
        List<SellerResponse> sellers = List.of(new SellerResponse(1L,"Ivan", "+79833338712", LocalDateTime.parse("2023-10-01T15:30:00")),
                new SellerResponse(2L, "Igor", "igor@mail.ru", LocalDateTime.parse("2023-10-01T15:30:00")),
                new SellerResponse(3L, "Ilya", "ilya@mail.ru", LocalDateTime.parse("2023-10-01T15:30:00")));
        when(sellerService.getSellers()).thenReturn(sellers);
        mockMvc.perform(get("/api/sellers").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value("Ivan"))
                .andExpect(jsonPath("$[1].name").value("Igor"))
                .andExpect(jsonPath("$[2].name").value("Ilya"));
    }

    @Test
    public void getSellers_ReturnsEmptyJson() throws Exception {
        List<SellerResponse> sellers = new ArrayList<>();
        when(sellerService.getSellers()).thenReturn(sellers);
        mockMvc.perform(get("/api/sellers").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void getSeller_ReturnsSeller() throws Exception {
        SellerResponse sellerResponse  = new SellerResponse(1L, "Ivan", "+79833338712", LocalDateTime.parse("2023-10-01T15:30:00"));
        when(sellerService.getSeller(eq(1L))).thenReturn(sellerResponse);
        mockMvc.perform(get("/api/sellers/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ivan"));
    }

    @Test
    public void getSeller_ReturnsError() throws Exception {
        when(sellerService.getSeller(eq(1L))).thenThrow(new ServiceException(HttpStatus.NOT_FOUND, "Seller wasn't found."));
        mockMvc.perform(get("/api/sellers/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Seller wasn't found."));
    }

    @Test
    public void postSeller_ReturnsSeller() throws Exception {
        SellerResponse sellerResponse = new SellerResponse(1L, "Dmitriy", "+78005553535", LocalDateTime.parse("2023-10-01T15:30:00"));
        sellerResponse.setId(1L);
        when(sellerService.createSeller(any(SellerPostRequest.class))).thenReturn(sellerResponse);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc.perform(post("/api/sellers").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(sellerResponse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Dmitriy"))
                .andExpect(jsonPath("$.contactInfo").value("+78005553535"));
    }

    @Test
    public void patchSeller_ReturnsSeller() throws Exception {
        SellerResponse updatedSeller = new SellerResponse(1L, "Dmitriy", "+78005553535", LocalDateTime.parse("2023-10-01T15:30:00"));
        updatedSeller.setId(1L);
        when(sellerService.updateSeller(eq(1L), any(SellerPatchRequest.class))).thenReturn(updatedSeller);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc.perform(patch("/api/sellers/1").contentType(MediaType.APPLICATION_JSON).content("{\"name\": \"Dmitriy\", \"contactInfo\": \"78005553535\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Dmitriy"))
                .andExpect(jsonPath("$.contactInfo").value("+78005553535"));
    }

    @Test
    public void patchSeller_ReturnsError() throws Exception {
        when(sellerService.updateSeller(eq(1L), any(SellerPatchRequest.class))).thenThrow(new ServiceException(HttpStatus.NOT_FOUND, "Seller wasn't found."));
        mockMvc.perform(patch("/api/sellers/1").contentType(MediaType.APPLICATION_JSON).content("{\"name\" : \"Jason\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Seller wasn't found."));
    }

    @Test
    public void deleteSeller_ReturnsSuccess() throws Exception {
        doNothing().when(sellerService).deleteSeller(eq(1L));
        mockMvc.perform(delete("/api/sellers/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteSeller_ReturnsError() throws Exception {
        doThrow(new ServiceException(HttpStatus.NOT_FOUND, "Seller wasn't found.")).when(sellerService).deleteSeller(eq(1L));
        mockMvc.perform(delete("/api/sellers/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Seller wasn't found."));
    }

    @Test
    public void getTransactions_ReturnsTransactions() throws Exception {
        Seller seller = new Seller(1L,"Ilya", "+78005553535", LocalDateTime.parse("2023-10-01T15:30:00"), null);
        seller.setId(1L);
        List<TransactionResponse> transactions = List.of(new TransactionResponse(1L, 1L, 1500, PaymentType.CASH, LocalDateTime.parse("2024-10-01T15:30:00")),
                new TransactionResponse(2L, 1L, 2000, PaymentType.CASH, LocalDateTime.parse("2024-10-01T15:30:00")),
                new TransactionResponse(3L, 1L, 3000, PaymentType.CASH, LocalDateTime.parse("2024-10-01T15:30:00")));
        when(sellerService.getTransactionsOfSeller(eq(1L))).thenReturn(transactions);
        mockMvc.perform(get("/api/sellers/1/transactions").contentType(MediaType.APPLICATION_JSON))
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
        when(sellerService.getTransactionsOfSeller(eq(1L))).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/api/sellers/1/transactions").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void getTransactions_ReturnsError() throws Exception {
        when(sellerService.getTransactionsOfSeller(eq(1L))).thenThrow(new ServiceException(HttpStatus.NOT_FOUND, "Seller wasn't found."));
        mockMvc.perform(get("/api/sellers/1/transactions").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Seller wasn't found."));
    }

    @Test
    public void getBestSeller_ReturnsBestSeller() throws Exception {
        SellerResponse seller = new SellerResponse(1L,"Ilya", "+78005553535", LocalDateTime.parse("2023-10-01T15:30:00"));
        when(sellerService.getBestSeller(any(RangeDate.class))).thenReturn(seller);
        RangeDate rangeDate = new RangeDate(LocalDateTime.parse("2023-10-01T15:30:00"), LocalDateTime.parse("2024-10-01T15:30:00"));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc.perform(get("/api/sellers/best").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(rangeDate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ilya"))
                .andExpect(jsonPath("$.contactInfo").value("+78005553535"));
    }

    @Test
    public void getBestSeller_ReturnsError() throws Exception {
        when(sellerService.getBestSeller(any(RangeDate.class))).thenThrow(new ServiceException(HttpStatus.NOT_FOUND, "Seller wasn't found."));
        RangeDate rangeDate = new RangeDate(LocalDateTime.parse("2023-10-01T15:30:00"), LocalDateTime.parse("2024-10-01T15:30:00"));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc.perform(get("/api/sellers/best").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(rangeDate)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Seller wasn't found."));
    }

    @Test
    public void getSellersSumLessThen_ReturnsSellers() throws Exception {
        List<SellerResponse> sellers = List.of(new SellerResponse(1L, "Ilya", "+78005553535", LocalDateTime.parse("2023-10-01T15:30:00")),
                new SellerResponse(2L, "Igor", "igor@mail.ru", LocalDateTime.parse("2023-10-01T15:30:00")));
        when(sellerService.getSellersSumLessThen(eq(100), any(RangeDate.class))).thenReturn(sellers);
        mockMvc.perform(get("/api/sellers/sumLessThen/100").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Ilya"))
                .andExpect(jsonPath("$[1].name").value("Igor"));
    }

    @Test
    public void getSellersSumLessThen_ReturnsEmptyJson() throws Exception {
        when(sellerService.getSellersSumLessThen(eq(100), any(RangeDate.class))).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/api/sellers/sumLessThen/100").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

}