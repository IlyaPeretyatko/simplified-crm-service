package ru.peretyatko.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.peretyatko.app.models.Transaction;
import ru.peretyatko.app.services.TransactionService;
import ru.peretyatko.app.util.SellerErrorResponse;
import ru.peretyatko.app.util.SellerNotFoundException;
import ru.peretyatko.app.util.TransactionErrorResponse;
import ru.peretyatko.app.util.TransactionNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionsController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionsController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("")
    public List<Transaction> getTransactions() {
        return transactionService.findAll();
    }

    @GetMapping("/{id}")
    public Transaction getTransaction(@PathVariable long id) {
        return transactionService.findById(id);
    }

    @PostMapping("")
    public Transaction postTransaction(@RequestBody Transaction transaction) {
        return transactionService.add(transaction);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable long id) {
        transactionService.delete(id);
    }

    @PatchMapping("/{id}")
    public Transaction patchTransaction(@PathVariable long id, @RequestBody Transaction transaction) {
        return transactionService.update(id, transaction);
    }

    @ExceptionHandler
    private ResponseEntity<TransactionErrorResponse> handleException(TransactionNotFoundException e) {
        TransactionErrorResponse transactionErrorResponse = new TransactionErrorResponse("Transaction wasn't found.");
        return new ResponseEntity<>(transactionErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<SellerErrorResponse> handleException(SellerNotFoundException e) {
        SellerErrorResponse sellerErrorResponse = new SellerErrorResponse("Seller wasn't found.");
        return new ResponseEntity<>(sellerErrorResponse, HttpStatus.NOT_FOUND);
    }

}
