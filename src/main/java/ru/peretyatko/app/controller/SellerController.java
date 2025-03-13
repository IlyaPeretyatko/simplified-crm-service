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
        return sellerService.getSeller(id);
    }

    @GetMapping("")
    public List<SellerResponse> getSellers() {
        return sellerService.getSellers();
    }

    @GetMapping("/{id}/transactions")
    public List<TransactionResponse> getTransactionsOfUser(@PathVariable long id) {
        return sellerService.getTransactionsOfUser(id);
    }

    @PostMapping("")
    public SellerResponse createSeller(@RequestBody SellerRequest sellerRequest) {
        return sellerService.createSeller(sellerRequest);
    }

    @PatchMapping("/{id}")
    public SellerResponse updateSeller(@PathVariable long id,
                                      @RequestBody SellerRequest sellerRequest) {
        return sellerService.updateSeller(id, sellerRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteSeller(@PathVariable long id) {
        sellerService.deleteSeller(id);
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


