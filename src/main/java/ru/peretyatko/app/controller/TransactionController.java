package ru.peretyatko.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.peretyatko.app.dto.transaction.TransactionPatchRequest;
import ru.peretyatko.app.dto.transaction.TransactionPostRequest;
import ru.peretyatko.app.dto.transaction.TransactionResponse;
import ru.peretyatko.app.service.TransactionService;
import ru.peretyatko.app.validator.transaction.TransactionValidator;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    private final TransactionValidator transactionValidator;

    @GetMapping("/{id}")
    public TransactionResponse getTransaction(@PathVariable long id) {
        return transactionService.getTransaction(id);
    }

    @GetMapping("")
    public List<TransactionResponse> getTransactions() {
        return transactionService.getTransactions();
    }

    @PostMapping("")
    public TransactionResponse createTransaction(@Valid @RequestBody TransactionPostRequest transactionPostRequest,
                                                 BindingResult bindingResult) {
        transactionValidator.validate(transactionPostRequest, bindingResult);
        return transactionService.createTransaction(transactionPostRequest);
    }

    @PatchMapping("/{id}")
    public TransactionResponse updateTransaction(@PathVariable long id,
                                                @Valid @RequestBody TransactionPatchRequest transactionPatchRequest,
                                                 BindingResult bindingResult) {
        transactionValidator.validate(transactionPatchRequest, bindingResult);
        return transactionService.updateTransaction(id, transactionPatchRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable long id) {
        transactionService.deleteTransaction(id);
    }

}
