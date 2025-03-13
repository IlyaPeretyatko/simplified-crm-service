package ru.peretyatko.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.peretyatko.app.dto.transaction.TransactionPatchRequest;
import ru.peretyatko.app.dto.transaction.TransactionPostRequest;
import ru.peretyatko.app.dto.transaction.TransactionResponse;
import ru.peretyatko.app.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/{id}")
    public TransactionResponse getTransaction(@PathVariable long id) {
        return transactionService.getTransaction(id);
    }

    @GetMapping("")
    public List<TransactionResponse> getTransactions() {
        return transactionService.getTransactions();
    }

    @PostMapping("")
    public TransactionResponse createTransaction(@RequestBody TransactionPostRequest transactionPostRequest) {
        return transactionService.createTransaction(transactionPostRequest);
    }

    @PatchMapping("/{id}")
    public TransactionResponse updateTransaction(@PathVariable long id,
                                                @RequestBody TransactionPatchRequest transactionPatchRequest) {
        return transactionService.updateTransaction(id, transactionPatchRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable long id) {
        transactionService.deleteTransaction(id);
    }

}
