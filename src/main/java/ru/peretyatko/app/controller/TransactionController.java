package ru.peretyatko.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.peretyatko.app.dto.transaction.TransactionPatchRequest;
import ru.peretyatko.app.dto.transaction.TransactionPostRequest;
import ru.peretyatko.app.dto.transaction.TransactionResponse;
import ru.peretyatko.app.model.Transaction;
import ru.peretyatko.app.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/{id}")
    public Transaction getTransaction(@PathVariable long id) {
        return null;
    }

    @GetMapping("")
    public List<TransactionResponse> getTransactions() {
        return null;
    }

    @PostMapping("")
    public TransactionResponse postTransaction(@RequestBody TransactionPostRequest transactionPostRequest) {
        return null;
    }

    @PatchMapping("/{id}")
    public TransactionResponse patchTransaction(@PathVariable long id,
                                                @RequestBody TransactionPatchRequest transactionPatchRequest) {
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable long id) {

    }

}
