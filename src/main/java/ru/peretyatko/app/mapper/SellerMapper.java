package ru.peretyatko.app.mapper;

import org.mapstruct.Mapper;
import ru.peretyatko.app.dto.seller.SellerRequest;
import ru.peretyatko.app.dto.seller.SellerResponse;
import ru.peretyatko.app.model.Seller;

@Mapper
public interface SellerMapper {
    Seller toSeller(SellerRequest sellerRequest);
    SellerResponse toSellerResponse(Seller seller);
}
