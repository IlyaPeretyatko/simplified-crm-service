package ru.peretyatko.app.controller;

import org.springframework.web.bind.annotation.*;
import ru.peretyatko.app.dto.seller.SellerRequest;
import ru.peretyatko.app.dto.seller.SellerResponse;
import ru.peretyatko.app.dto.transaction.TransactionResponse;
import ru.peretyatko.app.service.SellerService;
import ru.peretyatko.app.util.Period;
import java.util.List;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/sellers")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    @GetMapping("/{id}")
    public SellerResponse getSeller(@PathVariable long id) {
        return null;
    }

    @GetMapping("")
    public List<SellerResponse> getSellers() {
        return null;
    }

    @PostMapping("")
    public SellerResponse postSeller(@RequestBody SellerRequest sellerRequest) {
        return null;
    }

    @PatchMapping("/{id}")
    public SellerResponse patchSeller(@PathVariable long id, @RequestBody SellerRequest sellerRequest) {
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteSeller(@PathVariable long id) {

    }

    @GetMapping("/{id}/transactions")
    public List<TransactionResponse> getTransactionsOfUser(@PathVariable long id) {
        return null;
    }

    @GetMapping("/best")
    public SellerResponse getBestSeller(@RequestBody Period period) {
        return null;
    }

    @GetMapping("/sumLessThen/{sum}")
    public List<SellerResponse> getSellersSumLessThen(@PathVariable int sum) {
        return null;
    }

}


