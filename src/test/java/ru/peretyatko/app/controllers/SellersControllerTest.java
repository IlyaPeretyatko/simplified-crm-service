package ru.peretyatko.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import ru.peretyatko.app.models.Seller;
import ru.peretyatko.app.models.Transaction;
import ru.peretyatko.app.repositories.SellerRepository;
import ru.peretyatko.app.repositories.TransactionRepository;
import ru.peretyatko.app.util.Period;
import ru.peretyatko.app.util.SellerNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SellersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SellerRepository sellerRepository;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private EntityManager entityManager;

    @MockBean
    private Query query;

    @Test
    public void getSellers_ReturnsAllSellers() throws Exception {
        List<Seller> sellers = List.of(new Seller("Ivan", "+79833338712", LocalDateTime.parse("2023-10-01T15:30:00")),
                new Seller("Igor", "igor@mail.ru", LocalDateTime.parse("2023-10-01T15:30:00")),
                new Seller("Ilya", "ilya@mail.ru", LocalDateTime.parse("2023-10-01T15:30:00")));
        when(sellerRepository.findAll()).thenReturn(sellers);
        mockMvc.perform(get("/api/sellers").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Ivan"))
                .andExpect(jsonPath("$[1].name").value("Igor"))
                .andExpect(jsonPath("$[2].name").value("Ilya"));
    }

    @Test
    public void getSellers_ReturnsEmptyJson() throws Exception {
        List<Seller> sellers = new ArrayList<>();
        when(sellerRepository.findAll()).thenReturn(sellers);
        mockMvc.perform(get("/api/sellers").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void getSeller_ReturnsSeller() throws Exception {
        Seller seller  = new Seller("Ivan", "+79833338712", LocalDateTime.parse("2023-10-01T15:30:00"));
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
        mockMvc.perform(get("/api/sellers/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ivan"));
    }

    @Test
    public void getSeller_ReturnsError() throws Exception {
        when(sellerRepository.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/sellers/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Seller wasn't found."));
    }

    @Test
    public void postSeller_ReturnsSeller() throws Exception {
        Seller seller = new Seller("Dmitriy", "+78005553535", LocalDateTime.parse("2023-10-01T15:30:00"));
        seller.setId(1L);
        when(sellerRepository.save(any(Seller.class))).thenAnswer(invocation -> {
            Seller saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc.perform(post("/api/sellers").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(seller)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Dmitriy"))
                .andExpect(jsonPath("$.contactInfo").value("+78005553535"));
    }

    @Test
    public void patchSeller_ReturnsSeller() throws Exception {
        Seller beforeUpdateSeller = new Seller("Vladimir", "+78005553535", LocalDateTime.parse("2023-10-01T15:30:00"));
        Seller seller = new Seller("Dmitriy", "+78005553535", LocalDateTime.parse("2023-10-01T15:30:00"));
        seller.setId(1L);
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(beforeUpdateSeller));
        when(sellerRepository.save(any(Seller.class))).thenAnswer(invocation -> {
            Seller saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc.perform(patch("/api/sellers/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(seller)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Dmitriy"))
                .andExpect(jsonPath("$.contactInfo").value("+78005553535"));
    }

    @Test
    public void patchSeller_ReturnsError() throws Exception {
        when(sellerRepository.findById(1L)).thenThrow(SellerNotFoundException.class);
        mockMvc.perform(patch("/api/sellers/1").contentType(MediaType.APPLICATION_JSON).content("{\"name\" : \"Jason\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Seller wasn't found."));
    }

    @Test
    public void deleteSeller_ReturnsSuccess() throws Exception {
        when(sellerRepository.existsById(1L)).thenReturn(true);
        mockMvc.perform(delete("/api/sellers/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteSeller_ReturnsError() throws Exception {
        when(sellerRepository.existsById(1L)).thenReturn(false);
        mockMvc.perform(delete("/api/sellers/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Seller wasn't found."));
    }

    @Test
    public void getTransactions_ReturnsTransactions() throws Exception {
        Seller seller = new Seller("Ilya", "+78005553535", LocalDateTime.parse("2023-10-01T15:30:00"));
        seller.setId(1L);
        List<Transaction> transactions = List.of(new Transaction(seller, 1500, "CASH", LocalDateTime.parse("2024-10-01T15:30:00")),
                new Transaction(seller, 2000, "CASH", LocalDateTime.parse("2024-10-01T15:30:00")),
                new Transaction(seller, 3000, "CASH", LocalDateTime.parse("2024-10-01T15:30:00")));
        seller.setTransactions(transactions);
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
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
        Seller seller = new Seller("Ilya", "+78005553535", LocalDateTime.parse("2023-10-01T15:30:00"));
        seller.setId(1L);
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
        mockMvc.perform(get("/api/sellers/1/transactions").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void getTransactions_ReturnsError() throws Exception {
        when(sellerRepository.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/sellers/1/transactions").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Seller wasn't found."));
    }

    @Test
    public void getBestSeller_ReturnsBestSeller() throws Exception {
        Period period = new Period(LocalDateTime.parse("2023-10-01T15:30:00"), LocalDateTime.parse("2024-10-01T15:30:00"));
        Seller seller = new Seller("Ilya", "ilya@mail.ru", LocalDateTime.parse("2021-10-01T15:30:00"));
        seller.setId(1);
        String sql = """
                        SELECT * \
                        FROM sellers\
                        WHERE id = ( \
                            SELECT seller_id \
                            FROM transactions \
                            WHERE transaction_date > :start AND transaction_date < :end \
                            GROUP BY seller_id \
                            ORDER BY COALESCE(SUM(amount), 0) DESC \
                            LIMIT 1 \
                        )
                """;
        Query query = mock(Query.class);
        when(entityManager.createNativeQuery(sql, Seller.class)).thenReturn(query);
        when(query.setParameter("start", period.getStart())).thenReturn(query);
        when(query.setParameter("end", period.getEnd())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(seller);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc.perform(get("/api/sellers/best").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(period)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ilya"));
    }

    @Test
    public void getBestSeller_ReturnsError() throws Exception {

    }

}