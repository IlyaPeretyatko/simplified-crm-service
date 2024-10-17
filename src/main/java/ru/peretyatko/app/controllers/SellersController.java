package ru.peretyatko.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.peretyatko.app.models.Seller;
import ru.peretyatko.app.models.Transaction;
import ru.peretyatko.app.services.SellerService;
import ru.peretyatko.app.util.Period;
import ru.peretyatko.app.util.SellerErrorResponse;
import ru.peretyatko.app.util.SellerNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/sellers")
public class SellersController {

    private final SellerService sellerService;

    @Autowired
    public SellersController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @GetMapping("")
    public List<Seller> getSellers() {
        return sellerService.findAll();
    }

    @GetMapping("/{id}")
    public Seller getSeller(@PathVariable long id) {
        return sellerService.findById(id);
    }

    @PostMapping("")
    public Seller postSeller(@RequestBody Seller seller) {
        return sellerService.add(seller);
    }

    @PatchMapping("/{id}")
    public Seller patchSeller(@PathVariable long id, @RequestBody Seller seller) {
        return sellerService.update(id, seller);
    }

    @DeleteMapping("/{id}")
    public void deleteSeller(@PathVariable long id) {
        sellerService.delete(id);
    }

    @GetMapping("/{id}/transactions")
    public List<Transaction> getTransactions(@PathVariable long id) {
        return sellerService.findTransactionsBySeller(id);
    }

    @GetMapping("/best")
    public Seller getBestSeller(@RequestBody Period period) {
        return sellerService.findBestSeller(period);
    }

    @GetMapping("/sumLessThen/{sum}")
    public List<Seller> getSellersSumLessThen(@PathVariable int sum) {
        return sellerService.findSellersSumLessThen(sum);
    }

    @ExceptionHandler
    private ResponseEntity<SellerErrorResponse> handleException(SellerNotFoundException e) {
        SellerErrorResponse sellerErrorResponse = new SellerErrorResponse("Seller wasn't found.", System.currentTimeMillis());
        return new ResponseEntity<>(sellerErrorResponse, HttpStatus.NOT_FOUND);
    }

}


