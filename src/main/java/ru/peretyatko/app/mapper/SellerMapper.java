package ru.peretyatko.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.peretyatko.app.dto.seller.SellerPatchRequest;
import ru.peretyatko.app.dto.seller.SellerPostRequest;
import ru.peretyatko.app.dto.seller.SellerResponse;
import ru.peretyatko.app.model.Seller;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SellerMapper {
    Seller toSeller(SellerPostRequest sellerPostRequest);

    SellerResponse toSellerResponse(Seller seller);

    void updateSeller(SellerPatchRequest sellerPatchRequest, @MappingTarget Seller seller);
}
