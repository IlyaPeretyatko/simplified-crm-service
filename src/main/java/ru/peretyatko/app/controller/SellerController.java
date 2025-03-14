package ru.peretyatko.app.controller;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.peretyatko.app.dto.seller.SellerPatchRequest;
import ru.peretyatko.app.dto.seller.SellerPostRequest;
import ru.peretyatko.app.dto.seller.SellerResponse;
import ru.peretyatko.app.dto.transaction.TransactionResponse;
import ru.peretyatko.app.service.SellerService;
import ru.peretyatko.app.dto.RangeDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import ru.peretyatko.app.validator.seller.SellerValidator;


@RestController
@RequestMapping("/api/sellers")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    private final SellerValidator sellerValidator;


    @GetMapping("/{id}")
    public SellerResponse getSeller(@PathVariable long id) {
        return sellerService.getSeller(id);
    }

    @GetMapping("")
    public List<SellerResponse> getSellers() {
        return sellerService.getSellers();
    }

    @GetMapping("/{id}/transactions")
    public List<TransactionResponse> getTransactionsOfSeller(@PathVariable long id) {
        return sellerService.getTransactionsOfSeller(id);
    }

    @PostMapping("")
    public SellerResponse createSeller(@Valid @RequestBody SellerPostRequest sellerPostRequest,
                                       BindingResult bindingResult) {
        sellerValidator.validate(sellerPostRequest, bindingResult);
        return sellerService.createSeller(sellerPostRequest);
    }

    @PatchMapping("/{id}")
    public SellerResponse updateSeller(@PathVariable long id,
                                      @Valid @RequestBody SellerPatchRequest sellerPatchRequest,
                                       BindingResult bindingResult) {
        sellerValidator.validate(sellerPatchRequest, bindingResult);
        return sellerService.updateSeller(id, sellerPatchRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteSeller(@PathVariable long id) {
        sellerService.deleteSeller(id);
    }


    @GetMapping("/best")
    public SellerResponse getBestSeller(@RequestBody RangeDate rangeDate) {
        return sellerService.getBestSeller(rangeDate);
    }

    @GetMapping("/sumLessThen/{sum}")
    public List<SellerResponse> getSellersSumLessThen(@PathVariable int sum,
                                                      @RequestBody RangeDate rangeDate) {
        return sellerService.getSellersSumLessThen(sum, rangeDate);
    }

}


