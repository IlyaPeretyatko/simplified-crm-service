package ru.peretyatko.app.mapper;

import org.mapstruct.Mapper;
import ru.peretyatko.app.dto.seller.SellerPatchRequest;
import ru.peretyatko.app.dto.seller.SellerPostRequest;
import ru.peretyatko.app.dto.seller.SellerResponse;
import ru.peretyatko.app.model.Seller;

@Mapper
public interface SellerMapper {
    Seller toSeller(SellerPatchRequest sellerPatchRequest);
    Seller toSeller(SellerPostRequest sellerPostRequest);
    SellerResponse toSellerResponse(Seller seller);
}
